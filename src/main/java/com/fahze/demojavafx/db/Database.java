package com.fahze.demojavafx.db;

import org.sqlite.JDBC;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Database {
    private static final Logger logger = LogManager.getLogger(Database.class);

    /**
     * Location of database
     */
    private static final String location = DatabaseConfig.getDatabasePath();

    /**
     * Currently only table needed
     */
    private static final String requiredTable = "Expense";

    public static boolean isOK() {
        if (!checkDrivers()) {
            logger.error("SQLite JDBC Drivers could not be started");
            return false; //driver errors
        }

        if (!checkConnection()) {
            logger.error("Could not connect to database");
            return false; //can't connect to db
        }

        return createTablesIfNotExist(); //tables didn't exist
    }

    private static boolean checkDrivers() {
        try {
            Class.forName("org.sqlite.JDBC");
            DriverManager.registerDriver(new JDBC());
            return true;
        } catch (ClassNotFoundException | SQLException classNotFoundException) {
            logger.error("Could not start SQLite Drivers", classNotFoundException);
            return false;
        }
    }

    private static boolean checkConnection() {
        try (Connection connection = connect()) {
            return connection != null;
        } catch (SQLException e) {
            logger.error("Could not connect to database", e);
            return false;
        }
    }

    private static boolean createTablesIfNotExist() {
        String createExpenseTable =
                """
                CREATE TABLE IF NOT EXISTS expense(
                     period TEXT NOT NULL,
                     total REAL NOT NULL,
                     housing REAL NOT NULL,
                     food REAL NOT NULL,
                     outing REAL NOT NULL,
                     transport REAL NOT NULL,
                     travel REAL NOT NULL,
                     taxes REAL NOT NULL,
                     other REAL NOT NULL
                );
                """;

        String createIncomeTable =
                """
                CREATE TABLE IF NOT EXISTS income(
                     period TEXT NOT NULL,
                     total REAL NOT NULL,
                     salary REAL NOT NULL,
                     benefits REAL NOT NULL,
                     self_employment REAL NOT NULL,
                     passive REAL NOT NULL,
                     other REAL NOT NULL
                );
                """;

        try (Connection connection = Database.connect()) {
            PreparedStatement statement = connection.prepareStatement(createExpenseTable);
            statement.executeUpdate();

            statement = connection.prepareStatement(createIncomeTable);
            statement.executeUpdate();

            return true;
        } catch (SQLException exception) {
            logger.error("Could not create tables in database", exception);
            return false;
        }
    }

    public static Connection connect() {
        String dbPrefix = "jdbc:sqlite:";
        Connection connection;
        try {
            connection = DriverManager.getConnection(dbPrefix + location);
        } catch (SQLException exception) {
            logger.error("Could not connect to SQLite DB at " + location, exception);
            return null;
        }
        return connection;
    }
}
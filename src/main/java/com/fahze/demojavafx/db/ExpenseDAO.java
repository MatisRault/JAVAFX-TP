package com.fahze.demojavafx.db;

import com.fahze.demojavafx.Line;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class ExpenseDAO {
    private static final Logger logger = LogManager.getLogger(ExpenseDAO.class);

    public static ObservableList<Line> getAllExpenses() {
        ObservableList<Line> expenses = FXCollections.observableArrayList();
        String query = "SELECT * FROM expense";

        try (Connection connection = Database.connect();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            logger.info("Fetching all expenses from database");

            while (resultSet.next()) {
                Line expense = new Line(
                        resultSet.getString("period"),
                        resultSet.getFloat("total"),
                        resultSet.getFloat("housing"),
                        resultSet.getFloat("food"),
                        resultSet.getFloat("outing"),
                        resultSet.getFloat("transport"),
                        resultSet.getFloat("travel"),
                        resultSet.getFloat("taxes"),
                        resultSet.getFloat("other")
                );
                expenses.add(expense);
            }

            logger.info("Successfully retrieved {} expenses", expenses.size());
        } catch (SQLException exception) {
            logger.error("Error while getting expenses", exception);
        }
        return expenses;
    }

    public static boolean insertExpense(Line expense) {
        String query = "INSERT INTO expense (period, total, housing, food, outing, transport, travel, taxes, other) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = Database.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            logger.info("Attempting to insert new expense for period: {}", expense.getPeriod());

            statement.setString(1, expense.getPeriod());
            statement.setFloat(2, expense.getTotal());
            statement.setFloat(3, expense.getHousing());
            statement.setFloat(4, expense.getFood());
            statement.setFloat(5, expense.getOuting());
            statement.setFloat(6, expense.getTransport());
            statement.setFloat(7, expense.getTravel());
            statement.setFloat(8, expense.getTaxes());
            statement.setFloat(9, expense.getOther());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                logger.info("Successfully inserted expense for period: {}", expense.getPeriod());
                return true;
            } else {
                logger.warn("No rows affected when inserting expense for period: {}", expense.getPeriod());
                return false;
            }
        } catch (SQLException exception) {
            logger.error("Error while inserting expense for period: {}", expense.getPeriod(), exception);
            return false;
        }
    }
}
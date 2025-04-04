package com.fahze.demojavafx.db;

import com.fahze.demojavafx.Line;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExpenseDAO {

    public static ObservableList<Line> getAllExpenses() {
        ObservableList<Line> expenses = FXCollections.observableArrayList();
        String query = "SELECT * FROM expense";

        try (Connection connection = Database.connect();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

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
        } catch (SQLException exception) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Error while getting expenses: " + exception.getMessage());
        }
        return expenses;
    }

    public static boolean insertExpense(Line expense) {
        String query = "INSERT INTO expense (period, total, housing, food, outing, transport, travel, taxes, other) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = Database.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

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
            return rowsAffected > 0;
        } catch (SQLException exception) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Error while inserting expense: " + exception.getMessage());
            return false;
        }
    }
}
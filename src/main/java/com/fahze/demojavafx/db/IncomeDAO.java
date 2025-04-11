package com.fahze.demojavafx.db;

import com.fahze.demojavafx.Income;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class IncomeDAO {
    private static final Logger logger = LogManager.getLogger(IncomeDAO.class);

    public static ObservableList<Income> getAllIncomes() {
        ObservableList<Income> incomes = FXCollections.observableArrayList();
        String query = "SELECT * FROM income";

        try (Connection connection = Database.connect();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            logger.info("Fetching all incomes from database");

            while (resultSet.next()) {
                Income income = new Income(
                        resultSet.getString("period"),
                        resultSet.getFloat("total"),
                        resultSet.getFloat("salary"),
                        resultSet.getFloat("benefits"),
                        resultSet.getFloat("self_employment"),
                        resultSet.getFloat("passive"),
                        resultSet.getFloat("other")
                );
                incomes.add(income);
            }

            logger.info("Successfully retrieved {} incomes", incomes.size());

            // Si aucun revenu n'est trouvé, ajouter des données de test
            if (incomes.isEmpty()) {
                logger.info("No incomes found in database, adding sample data");
                addSampleData();
                return getAllIncomes(); // Rappel récursif après ajout des données
            }
        } catch (SQLException exception) {
            logger.error("Error while getting incomes", exception);
        }
        return incomes;
    }

    public static boolean insertIncome(Income income) {
        String query = "INSERT INTO income (period, total, salary, benefits, self_employment, passive, other) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = Database.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            logger.info("Attempting to insert new income for period: {}", income.getPeriod());

            statement.setString(1, income.getPeriod());
            statement.setFloat(2, income.getTotal());
            statement.setFloat(3, income.getSalary());
            statement.setFloat(4, income.getBenefits());
            statement.setFloat(5, income.getSelfEmployment());
            statement.setFloat(6, income.getPassive());
            statement.setFloat(7, income.getOther());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                logger.info("Successfully inserted income for period: {}", income.getPeriod());
                return true;
            } else {
                logger.warn("No rows affected when inserting income for period: {}", income.getPeriod());
                return false;
            }
        } catch (SQLException exception) {
            logger.error("Error while inserting income for period: {}", income.getPeriod(), exception);
            return false;
        }
    }

    // Méthode pour récupérer la somme des revenus par période
    public static Float getTotalIncomeForPeriod(String period) {
        String query = "SELECT total FROM income WHERE period = ?";
        try (Connection connection = Database.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, period);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getFloat("total");
            }
        } catch (SQLException exception) {
            logger.error("Error while getting total income for period: {}", period, exception);
        }
        return 0.0f;
    }

    /**
     * Ajoute des données d'exemple pour les tests
     */
    private static void addSampleData() {
        try {
            // Obtenir les périodes existantes des dépenses pour aligner les données
            ObservableList<String> periods = FXCollections.observableArrayList();
            String query = "SELECT DISTINCT period FROM expense";

            try (Connection connection = Database.connect();
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {

                while (resultSet.next()) {
                    periods.add(resultSet.getString("period"));
                }
            }

            logger.info("Found {} expense periods for sample data", periods.size());

            if (periods.isEmpty()) {
                // Si aucune période de dépense n'est trouvée, utiliser des périodes par défaut
                periods.add("01-2025");
                periods.add("02-2025");
                periods.add("03-2025");
            }

            // Ajouter des revenus d'exemple pour chaque période
            for (String period : periods) {
                Income sampleIncome = new Income(
                        period,
                        15000.0f,   // total beaucoup plus élevé que les dépenses moyennes
                        12000.0f,   // salaire
                        1000.0f,    // aides
                        1500.0f,    // auto-entreprise
                        400.0f,     // revenus passifs
                        100.0f      // autres
                );
                insertIncome(sampleIncome);
                logger.info("Added sample income for period: {}", period);
            }
        } catch (Exception e) {
            logger.error("Error adding sample data", e);
        }
    }
}
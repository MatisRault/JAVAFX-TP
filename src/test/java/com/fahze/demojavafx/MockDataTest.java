package com.fahze.demojavafx;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Test de manipulation des données sans base de données.
 * Ces tests vérifient que la logique de manipulation des données fonctionne correctement,
 * sans dépendre de la base de données.
 */
public class MockDataTest {

    private static final float DELTA = 0.001f;

    @Test
    public void testExpenseFiltering() {
        // Créer une liste de dépenses
        ObservableList<Line> expenses = FXCollections.observableArrayList();
        expenses.add(new Line("01-2025", 1000.0f, 500.0f, 200.0f, 100.0f, 50.0f, 75.0f, 50.0f, 25.0f));
        expenses.add(new Line("02-2025", 1200.0f, 600.0f, 240.0f, 120.0f, 60.0f, 90.0f, 60.0f, 30.0f));
        expenses.add(new Line("121", 500.0f, 250.0f, 100.0f, 50.0f, 25.0f, 37.5f, 25.0f, 12.5f));

        // Filtrer les dépenses pour exclure la période "121"
        ObservableList<Line> filteredExpenses = expenses.filtered(expense -> !expense.getPeriod().equals("121"));

        // Vérifier le résultat
        assertEquals(2, filteredExpenses.size());
        for (Line expense : filteredExpenses) {
            assertNotEquals("121", expense.getPeriod());
        }
    }

    @Test
    public void testIncomeTotals() {
        // Créer une liste de revenus
        ObservableList<Income> incomes = FXCollections.observableArrayList();
        incomes.add(new Income("01-2025", 2000.0f, 1500.0f, 200.0f, 200.0f, 50.0f, 50.0f));
        incomes.add(new Income("02-2025", 2200.0f, 1650.0f, 220.0f, 220.0f, 55.0f, 55.0f));

        // Calculer le total des revenus
        float totalIncome = 0.0f;
        for (Income income : incomes) {
            totalIncome += income.getTotal();
        }

        // Vérifier le résultat
        assertEquals(4200.0f, totalIncome, DELTA);
    }

    @Test
    public void testCurrencyCalculations() {
        // Créer des dépenses et des revenus
        Line expense = new Line("01-2025", 1000.0f, 500.0f, 200.0f, 100.0f, 50.0f, 75.0f, 50.0f, 25.0f);
        Income income = new Income("01-2025", 2000.0f, 1500.0f, 200.0f, 200.0f, 50.0f, 50.0f);

        // Définir un taux de change
        float exchangeRate = 1.09f;
        expense.setExchangeRate(exchangeRate);
        income.setExchangeRate(exchangeRate);

        // Convertir en USD
        expense.convertToUsd();
        income.convertToUsd();

        // Vérifier les valeurs converties
        assertEquals(1000.0f * exchangeRate, expense.getTotal(), DELTA);
        assertEquals(2000.0f * exchangeRate, income.getTotal(), DELTA);

        // Calculer le solde (revenu - dépense) en USD
        float balance = income.getTotal() - expense.getTotal();
        assertEquals((2000.0f - 1000.0f) * exchangeRate, balance, DELTA);
    }
}
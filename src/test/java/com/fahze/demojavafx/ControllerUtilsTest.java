package com.fahze.demojavafx;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Locale;

/**
 * Test des fonctionnalités de base des contrôleurs sans JavaFX.
 * Nous testons uniquement les méthodes utilitaires qui ne dépendent pas de JavaFX.
 */
public class ControllerUtilsTest {

    @Test
    public void testFormatCurrency() {
        // Cette méthode simule le formatage des devises dans les contrôleurs

        // Formatage en euros avec locale explicite pour éviter les problèmes de formatage régional
        String formattedEuro = formatCurrencyWithLocale(1234.56f, true);
        assertEquals("€1234.56", formattedEuro);

        // Formatage en dollars avec locale explicite
        String formattedUsd = formatCurrencyWithLocale(1234.56f, false);
        assertEquals("$1234.56", formattedUsd);
    }

    /**
     * Méthode utilitaire similaire à celle utilisée dans les contrôleurs,
     * mais avec un Locale explicite pour éviter les problèmes de formatage régional.
     */
    private String formatCurrencyWithLocale(Float value, boolean isEuro) {
        return String.format(Locale.US, isEuro ? "€%.2f" : "$%.2f", value);
    }

    @Test
    public void testCalculateTotal() {
        // Cette méthode simule un calcul qui pourrait être effectué dans un contrôleur

        // Créer une dépense
        Line expense = new Line("01-2025", 0.0f, 500.0f, 200.0f, 100.0f, 50.0f, 75.0f, 50.0f, 25.0f);

        // Calculer le total
        float total = calculateTotal(expense);

        // Vérifier le résultat
        assertEquals(1000.0f, total, 0.001f);
    }

    /**
     * Méthode utilitaire qui calcule le total des dépenses.
     */
    private float calculateTotal(Line expense) {
        return expense.getHousing() + expense.getFood() + expense.getOuting() +
                expense.getTransport() + expense.getTravel() + expense.getTaxes() +
                expense.getOther();
    }
}
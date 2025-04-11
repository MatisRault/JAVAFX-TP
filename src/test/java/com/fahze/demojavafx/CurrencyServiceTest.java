package com.fahze.demojavafx.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CurrencyServiceTest {

    private static final double DELTA = 0.001;
    private static final double DEFAULT_RATE = 1.09;

    @AfterEach
    public void resetRate() {
        // Remettre le taux par défaut après chaque test
        CurrencyService.setEurToUsdRate(DEFAULT_RATE);
    }

    @Test
    public void testConvertEurToUsd() {
        // Convertir un montant d'euros en dollars
        double eurAmount = 100.0;
        double usdAmount = CurrencyService.convertEurToUsd(eurAmount);

        // Vérifier la conversion
        assertEquals(eurAmount * DEFAULT_RATE, usdAmount, DELTA);
    }

    @Test
    public void testConvertUsdToEur() {
        // Convertir un montant de dollars en euros
        double usdAmount = 100.0;
        double eurAmount = CurrencyService.convertUsdToEur(usdAmount);

        // Vérifier la conversion
        assertEquals(usdAmount / DEFAULT_RATE, eurAmount, DELTA);
    }

    @Test
    public void testSetExchangeRate() {
        // Définir un nouveau taux
        double newRate = 1.2;
        CurrencyService.setEurToUsdRate(newRate);

        // Vérifier que le taux a bien été modifié
        double eurAmount = 100.0;
        double usdAmount = CurrencyService.convertEurToUsd(eurAmount);
        assertEquals(eurAmount * newRate, usdAmount, DELTA);
    }
}
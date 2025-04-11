package com.fahze.demojavafx;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class IncomeTest {

    private static final float DELTA = 0.001f;

    @Test
    public void testConstructorAndGetters() {
        // Créer un objet Income avec des valeurs connues
        Income income = new Income("01-2025", 2000.0f, 1500.0f,
                200.0f, 200.0f, 50.0f, 50.0f);

        // Vérifier les valeurs
        assertEquals("01-2025", income.getPeriod());
        assertEquals(2000.0f, income.getTotal(), DELTA);
        assertEquals(1500.0f, income.getSalary(), DELTA);
        assertEquals(200.0f, income.getBenefits(), DELTA);
        assertEquals(200.0f, income.getSelfEmployment(), DELTA);
        assertEquals(50.0f, income.getPassive(), DELTA);
        assertEquals(50.0f, income.getOther(), DELTA);

        // Vérifier que l'objet est initialement en euros
        assertTrue(income.isInEuro());
    }

    @Test
    public void testSetters() {
        // Créer un objet Income vide
        Income income = new Income();

        // Définir les valeurs
        income.setPeriod("01-2025");
        income.setTotal(2000.0f);
        income.setSalary(1500.0f);
        income.setBenefits(200.0f);
        income.setSelfEmployment(200.0f);
        income.setPassive(50.0f);
        income.setOther(50.0f);

        // Vérifier les valeurs
        assertEquals("01-2025", income.getPeriod());
        assertEquals(2000.0f, income.getTotal(), DELTA);
        assertEquals(1500.0f, income.getSalary(), DELTA);
        assertEquals(200.0f, income.getBenefits(), DELTA);
        assertEquals(200.0f, income.getSelfEmployment(), DELTA);
        assertEquals(50.0f, income.getPassive(), DELTA);
        assertEquals(50.0f, income.getOther(), DELTA);
    }

    @Test
    public void testCurrencyConversion() {
        // Créer un objet Income
        Income income = new Income("01-2025", 2000.0f, 1500.0f,
                200.0f, 200.0f, 50.0f, 50.0f);

        // Définir un taux de change
        float exchangeRate = 1.09f;
        income.setExchangeRate(exchangeRate);

        // Convertir en USD
        income.convertToUsd();

        // Vérifier que l'objet est maintenant en USD
        assertFalse(income.isInEuro());

        // Vérifier les valeurs converties
        assertEquals(2000.0f * exchangeRate, income.getTotal(), DELTA);
        assertEquals(1500.0f * exchangeRate, income.getSalary(), DELTA);

        // Reconvertir en euros
        income.convertToEuro();

        // Vérifier que l'objet est à nouveau en euros
        assertTrue(income.isInEuro());

        // Vérifier que les valeurs sont revenues à leurs valeurs initiales
        assertEquals(2000.0f, income.getTotal(), DELTA);
        assertEquals(1500.0f, income.getSalary(), DELTA);
    }
}
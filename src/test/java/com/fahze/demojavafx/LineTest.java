package com.fahze.demojavafx;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LineTest {

    private static final float DELTA = 0.001f;

    @Test
    public void testConstructorAndGetters() {
        // Créer un objet Line avec des valeurs connues
        Line line = new Line("01-2025", 1000.0f, 500.0f, 200.0f,
                100.0f, 50.0f, 75.0f, 50.0f, 25.0f);

        // Vérifier les valeurs
        assertEquals("01-2025", line.getPeriod());
        assertEquals(1000.0f, line.getTotal(), DELTA);
        assertEquals(500.0f, line.getHousing(), DELTA);
        assertEquals(200.0f, line.getFood(), DELTA);
        assertEquals(100.0f, line.getOuting(), DELTA);
        assertEquals(50.0f, line.getTransport(), DELTA);
        assertEquals(75.0f, line.getTravel(), DELTA);
        assertEquals(50.0f, line.getTaxes(), DELTA);
        assertEquals(25.0f, line.getOther(), DELTA);

        // Vérifier que l'objet est initialement en euros
        assertTrue(line.isInEuro());
    }

    @Test
    public void testSetters() {
        // Créer un objet Line vide
        Line line = new Line();

        // Définir les valeurs
        line.setPeriod("01-2025");
        line.setTotal(1000.0f);
        line.setHousing(500.0f);
        line.setFood(200.0f);
        line.setOuting(100.0f);
        line.setTransport(50.0f);
        line.setTravel(75.0f);
        line.setTaxes(50.0f);
        line.setOther(25.0f);

        // Vérifier les valeurs
        assertEquals("01-2025", line.getPeriod());
        assertEquals(1000.0f, line.getTotal(), DELTA);
        assertEquals(500.0f, line.getHousing(), DELTA);
        assertEquals(200.0f, line.getFood(), DELTA);
        assertEquals(100.0f, line.getOuting(), DELTA);
        assertEquals(50.0f, line.getTransport(), DELTA);
        assertEquals(75.0f, line.getTravel(), DELTA);
        assertEquals(50.0f, line.getTaxes(), DELTA);
        assertEquals(25.0f, line.getOther(), DELTA);
    }

    @Test
    public void testCurrencyConversion() {
        // Créer un objet Line
        Line line = new Line("01-2025", 1000.0f, 500.0f, 200.0f,
                100.0f, 50.0f, 75.0f, 50.0f, 25.0f);

        // Définir un taux de change
        float exchangeRate = 1.09f;
        line.setExchangeRate(exchangeRate);

        // Convertir en USD
        line.convertToUsd();

        // Vérifier que l'objet est maintenant en USD
        assertFalse(line.isInEuro());

        // Vérifier les valeurs converties
        assertEquals(1000.0f * exchangeRate, line.getTotal(), DELTA);
        assertEquals(500.0f * exchangeRate, line.getHousing(), DELTA);

        // Reconvertir en euros
        line.convertToEuro();

        // Vérifier que l'objet est à nouveau en euros
        assertTrue(line.isInEuro());

        // Vérifier que les valeurs sont revenues à leurs valeurs initiales
        assertEquals(1000.0f, line.getTotal(), DELTA);
        assertEquals(500.0f, line.getHousing(), DELTA);
    }
}
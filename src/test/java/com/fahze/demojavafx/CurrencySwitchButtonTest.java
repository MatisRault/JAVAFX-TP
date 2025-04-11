package com.fahze.demojavafx;

import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CurrencySwitchButtonTest {

    private CurrencySwitchButton button;

    @BeforeAll
    public static void setupJFX() {
        // Initialiser JavaFX pour les tests
        new JFXPanel();
    }

    @BeforeEach
    public void setUp() {
        button = new CurrencySwitchButton();
    }

    @Test
    public void testInitialState() {
        // Vérifier l'état initial
        assertTrue(button.isEuro());
    }

    @Test
    public void testSetOnAction() {
        // Vérifier que l'on peut définir un gestionnaire d'événements
        boolean[] actionPerformed = new boolean[1];
        button.setOnAction(event -> actionPerformed[0] = true);

        // Simuler un déclenchement de l'action
        button.onActionProperty().get().handle(null);

        // Vérifier que l'action a été exécutée
        assertTrue(actionPerformed[0]);
    }
}
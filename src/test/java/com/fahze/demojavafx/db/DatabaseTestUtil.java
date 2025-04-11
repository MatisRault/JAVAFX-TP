package com.fahze.demojavafx.db;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.AfterAllCallback;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseTestUtil {

    // Configurer une base de données en mémoire pour les tests
    public static void setupTestDatabase() throws Exception {
        // Remplacer le chemin de la base de données par une base en mémoire
        Field locationField = Database.class.getDeclaredField("location");
        locationField.setAccessible(true);
        locationField.set(null, ":memory:");

        // Créer les tables nécessaires
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite::memory:")) {
            Statement stmt = conn.createStatement();

            // Créer la table expense
            stmt.execute("CREATE TABLE IF NOT EXISTS expense(" +
                    "period TEXT NOT NULL, " +
                    "total REAL NOT NULL, " +
                    "housing REAL NOT NULL, " +
                    "food REAL NOT NULL, " +
                    "outing REAL NOT NULL, " +
                    "transport REAL NOT NULL, " +
                    "travel REAL NOT NULL, " +
                    "taxes REAL NOT NULL, " +
                    "other REAL NOT NULL);");

            // Créer la table income
            stmt.execute("CREATE TABLE IF NOT EXISTS income(" +
                    "period TEXT NOT NULL, " +
                    "total REAL NOT NULL, " +
                    "salary REAL NOT NULL, " +
                    "benefits REAL NOT NULL, " +
                    "self_employment REAL NOT NULL, " +
                    "passive REAL NOT NULL, " +
                    "other REAL NOT NULL);");
        } catch (SQLException e) {
            throw new RuntimeException("Impossible de configurer la base de données de test", e);
        }
    }
}

@ExtendWith(DatabaseExtension.class)
class DatabaseExtension implements BeforeAllCallback, AfterAllCallback {

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        DatabaseTestUtil.setupTestDatabase();
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        // Nettoyage après tous les tests
    }
}
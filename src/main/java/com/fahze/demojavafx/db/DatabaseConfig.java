package com.fahze.demojavafx.db;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DatabaseConfig {
    public static String getDatabasePath() {
        String osName = System.getProperty("os.name").toLowerCase();
        String userHome = System.getProperty("user.home");

        Path databasePath;

        if (osName.contains("win")) {
            // Windows: %APPDATA%
            databasePath = Paths.get(System.getenv("APPDATA"), "FinanceTracker", "database.db");
        } else if (osName.contains("mac")) {
            // MacOS: ~/Library/Application Support
            databasePath = Paths.get(userHome, "Library", "Application Support", "FinanceTracker", "database.db");
        } else {
            // Linux: ~/.config
            databasePath = Paths.get(userHome, ".config", "FinanceTracker", "database.db");
        }

        // Ensure directory exists
        File parentDir = databasePath.getParent().toFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }

        return databasePath.toString();
    }
}
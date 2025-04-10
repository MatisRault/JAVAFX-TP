package com.fahze.demojavafx;

import com.fahze.demojavafx.db.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;

public class HelloApplication extends Application {
    private static final Logger logger = LogManager.getLogger(HelloApplication.class);

    @Override
    public void start(Stage stage) throws IOException {
        // Vérifier que la base de données est OK
        if (!Database.isOK()) {
            logger.error("Could not initialize database. Application will exit.");
            System.exit(1);
        }

        logger.info("Starting FinanceTracker application");

        // Chargement de l'icône avec gestion d'erreur
        try (InputStream iconStream = getClass().getResourceAsStream("/icon.png")) {
            if (iconStream != null) {
                Image icon = new Image(iconStream);
                stage.getIcons().add(icon);
            } else {
                logger.warn("Icon not found. Using default icon.");
            }
        } catch (Exception e) {
            logger.error("Error loading icon", e);
        }

        // Chargement de l'écran du tableau de bord
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("dashboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(HelloApplication.class.getResource("css/style.css").toExternalForm());
        stage.setTitle("Finance Tracker - Tableau de bord");
        stage.setScene(scene);
        stage.show();

        logger.info("Application started successfully");
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
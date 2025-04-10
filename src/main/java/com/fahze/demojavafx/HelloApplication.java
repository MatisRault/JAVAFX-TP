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
        if (!Database.isOK()) {
            logger.error("La database n'a pas pu être chargée, fermeture de l'application.");
            System.exit(1);
        }

        logger.info("Lancement de l'application");

        try (InputStream iconStream = getClass().getResourceAsStream("/icon.png")) {
            if (iconStream != null) {
                Image icon = new Image(iconStream);
                stage.getIcons().add(icon);
            } else {
                logger.warn("Icon pas trouvée, remplacé par l'icon par défaut.");
            }
        } catch (Exception e) {
            logger.error("Erreur lors du chargement de l'icon", e);
        }

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
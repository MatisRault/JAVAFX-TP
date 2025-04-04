package com.fahze.demojavafx;

import com.fahze.demojavafx.db.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

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

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("table.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(HelloApplication.class.getResource("css/style.css").toExternalForm());
        stage.setTitle("Tableau de dépenses");
        stage.setScene(scene);
        stage.show();

        logger.info("Application started successfully");
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
package com.fahze.demojavafx;

import com.fahze.demojavafx.db.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Vérifier que la base de données est OK
        if (!Database.isOK()) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Could not initialize database. Application will exit.");
            System.exit(1);
        }

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("table.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(HelloApplication.class.getResource("css/style.css").toExternalForm());
        stage.setTitle("Tableau de dépenses");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
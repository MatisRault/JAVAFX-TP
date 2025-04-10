package com.fahze.demojavafx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.IOException;

public class HeaderController {

    @FXML
    private MenuItem dashboardMenuItem;

    @FXML
    private MenuItem expensesMenuItem;

    @FXML
    public void initialize() {
    }

    @FXML
    public void navigateToDashboard() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
        Stage stage = getStageFromMenuItem(dashboardMenuItem);
        stage.setScene(new Scene(loader.load()));
        stage.setTitle("Finance Tracker - Tableau de bord");
        stage.show();
    }

    @FXML
    public void navigateToExpenses() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("table.fxml"));
        Stage stage = getStageFromMenuItem(expensesMenuItem);
        stage.setScene(new Scene(loader.load()));
        stage.setTitle("Finance Tracker - Dépenses");
        stage.show();
    }

    private Stage getStageFromMenuItem(MenuItem menuItem) {
        try {
            MenuBar menuBar = (MenuBar) menuItem.getParentMenu().getParentPopup().getOwnerNode();
            return (Stage) menuBar.getScene().getWindow();
        } catch (Exception e) {
            return new Stage();
        }
    }
}
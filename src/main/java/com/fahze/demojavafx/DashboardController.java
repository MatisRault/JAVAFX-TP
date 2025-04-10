package com.fahze.demojavafx;

import com.fahze.demojavafx.db.ExpenseDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class DashboardController {
    private static final Logger logger = LogManager.getLogger(DashboardController.class);

    @FXML private PieChart expenseDistributionChart;
    @FXML private LineChart<String, Number> expenseEvolutionChart;
    @FXML private ChoiceBox<String> periodSelector;

    private ObservableList<Line> expenses;
    private String selectedPeriod;

    @FXML
    public void initialize() {
        logger.info("Initializing Dashboard");

        // Chargement des dépenses
        expenses = ExpenseDAO.getAllExpenses();

        // Configuration du sélecteur de période
        initializePeriodSelector();

        // Mise à jour des graphiques
        updateCharts();

        // Écoute des changements de période
        periodSelector.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedPeriod = newValue;
            updateCharts();
        });
    }

    private void initializePeriodSelector() {
        // Récupération des périodes uniques
        ObservableList<String> periods = FXCollections.observableArrayList();
        for (Line expense : expenses) {
            if (!periods.contains(expense.getPeriod())) {
                periods.add(expense.getPeriod());
            }
        }

        periodSelector.setItems(periods);

        // Sélection de la première période par défaut si disponible
        if (!periods.isEmpty()) {
            periodSelector.getSelectionModel().selectFirst();
            selectedPeriod = periods.get(0);
        }
    }

    private void updateCharts() {
        if (selectedPeriod == null || selectedPeriod.isEmpty()) {
            return;
        }

        updatePieChart();
        updateLineChart();
    }

    private void updatePieChart() {
        // Filtrer les dépenses pour la période sélectionnée
        Line selectedExpense = null;
        for (Line expense : expenses) {
            if (expense.getPeriod().equals(selectedPeriod)) {
                selectedExpense = expense;
                break;
            }
        }

        if (selectedExpense == null) {
            return;
        }

        // Création des données pour le graphique en camembert
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Logement", selectedExpense.getHousing()),
                new PieChart.Data("Nourriture", selectedExpense.getFood()),
                new PieChart.Data("Sorties", selectedExpense.getOuting()),
                new PieChart.Data("Transport", selectedExpense.getTransport()),
                new PieChart.Data("Voyage", selectedExpense.getTravel()),
                new PieChart.Data("Impôts", selectedExpense.getTaxes()),
                new PieChart.Data("Autres", selectedExpense.getOther())
        );

        expenseDistributionChart.setData(pieChartData);
        expenseDistributionChart.setTitle("Répartition des dépenses");
    }

    private void updateLineChart() {
        // Préparation des séries pour le graphique linéaire
        XYChart.Series<String, Number> housingSeries = new XYChart.Series<>();
        housingSeries.setName("Logement");

        XYChart.Series<String, Number> foodSeries = new XYChart.Series<>();
        foodSeries.setName("Nourriture");

        XYChart.Series<String, Number> outingSeries = new XYChart.Series<>();
        outingSeries.setName("Sorties");

        XYChart.Series<String, Number> transportSeries = new XYChart.Series<>();
        transportSeries.setName("Transport");

        XYChart.Series<String, Number> travelSeries = new XYChart.Series<>();
        travelSeries.setName("Voyage");

        // Ajouter les données à chaque série
        for (Line expense : expenses) {
            housingSeries.getData().add(new XYChart.Data<>(expense.getPeriod(), expense.getHousing()));
            foodSeries.getData().add(new XYChart.Data<>(expense.getPeriod(), expense.getFood()));
            outingSeries.getData().add(new XYChart.Data<>(expense.getPeriod(), expense.getOuting()));
            transportSeries.getData().add(new XYChart.Data<>(expense.getPeriod(), expense.getTransport()));
            travelSeries.getData().add(new XYChart.Data<>(expense.getPeriod(), expense.getTravel()));
        }

        // Effacer les anciennes données et ajouter les nouvelles séries
        expenseEvolutionChart.getData().clear();
        expenseEvolutionChart.getData().addAll(
                housingSeries,
                foodSeries,
                outingSeries,
                transportSeries,
                travelSeries
        );

        expenseEvolutionChart.setTitle("Évolution des dépenses");
    }
}
package com.fahze.demojavafx;

import com.fahze.demojavafx.db.ExpenseDAO;
import com.fahze.demojavafx.db.IncomeDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class DashboardController {
    private static final Logger logger = LogManager.getLogger(DashboardController.class);

    @FXML private PieChart expenseDistributionChart;
    @FXML private LineChart<String, Number> expenseEvolutionChart;
    @FXML private BarChart<String, Number> incomeExpenseComparisonChart;
    @FXML private ChoiceBox<String> periodSelector;

    private ObservableList<Line> expenses;
    private ObservableList<Income> incomes;
    private String selectedPeriod;

    @FXML
    public void initialize() {
        logger.info("Initializing Dashboard");

        // Chargement des dépenses et revenus
        expenses = ExpenseDAO.getAllExpenses();
        incomes = IncomeDAO.getAllIncomes();

        // Filtrer pour supprimer la période 121
        filterPeriod121();

        // Configuration du sélecteur de période
        initializePeriodSelector();

        // Configurer l'échelle du graphique de comparaison avec un maximum de 30000
        configureChartScale();

        // Mise à jour des graphiques
        updateCharts();

        // Écoute des changements de période
        periodSelector.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedPeriod = newValue;
            updateCharts();
        });
    }

    private void configureChartScale() {
        try {
            // Récupérer l'axe Y du graphique de comparaison
            NumberAxis yAxis = (NumberAxis) incomeExpenseComparisonChart.getYAxis();

            // Désactiver l'auto-dimensionnement
            yAxis.setAutoRanging(false);

            // Définir la limite inférieure à 0 et la limite supérieure à 30000
            yAxis.setLowerBound(0);
            yAxis.setUpperBound(30000);

            // Définir l'unité de division (tick unit)
            yAxis.setTickUnit(5000);

            // S'assurer que la légende est visible
            incomeExpenseComparisonChart.setLegendVisible(true);

            logger.info("Chart scale configured with upper bound set to 30000");
        } catch (Exception e) {
            logger.error("Error configuring chart scale", e);
        }
    }

    private void filterPeriod121() {
        // Filtre des dépenses pour supprimer la période 121
        expenses = expenses.stream()
                .filter(expense -> !expense.getPeriod().equals("121"))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        // Filtre des revenus pour supprimer la période 121
        incomes = incomes.stream()
                .filter(income -> !income.getPeriod().equals("121"))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        logger.info("Filtered out period 121, remaining expenses: {}, remaining incomes: {}",
                expenses.size(), incomes.size());
    }

    private void initializePeriodSelector() {
        // Récupération des périodes uniques
        ObservableList<String> periods = FXCollections.observableArrayList();

        // Ajouter les périodes des dépenses
        for (Line expense : expenses) {
            if (!periods.contains(expense.getPeriod())) {
                periods.add(expense.getPeriod());
            }
        }

        // Ajouter les périodes des revenus
        for (Income income : incomes) {
            if (!periods.contains(income.getPeriod())) {
                periods.add(income.getPeriod());
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
        updateIncomeExpenseComparisonChart();
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

    private void updateIncomeExpenseComparisonChart() {
        logger.info("Updating income/expense comparison chart");

        // Vérifier si les listes sont vides
        if (expenses.isEmpty() && incomes.isEmpty()) {
            logger.warn("No data available for comparison chart");
            return;
        }

        // Création des séries pour le graphique de comparaison
        XYChart.Series<String, Number> incomeSeries = new XYChart.Series<>();
        incomeSeries.setName("Revenus");

        XYChart.Series<String, Number> expenseSeries = new XYChart.Series<>();
        expenseSeries.setName("Dépenses");

        // Récupération de toutes les périodes
        ObservableList<String> allPeriods = FXCollections.observableArrayList();

        // Ajouter les périodes des dépenses
        for (Line expense : expenses) {
            if (!allPeriods.contains(expense.getPeriod())) {
                allPeriods.add(expense.getPeriod());
            }
        }

        // Ajouter les périodes des revenus
        for (Income income : incomes) {
            if (!allPeriods.contains(income.getPeriod())) {
                allPeriods.add(income.getPeriod());
            }
        }

        logger.info("Found {} unique periods for comparison chart", allPeriods.size());

        if (allPeriods.isEmpty()) {
            logger.warn("No periods found for comparison chart");
            return;
        }

        // Pour chaque période, récupérer les revenus et dépenses totaux
        for (String period : allPeriods) {
            // Recherche des dépenses pour cette période
            Float totalExpense = 0.0f;
            for (Line expense : expenses) {
                if (expense.getPeriod().equals(period)) {
                    totalExpense = expense.getTotal();
                    logger.debug("Found expense total {} for period {}", totalExpense, period);
                    break;
                }
            }

            // Recherche des revenus pour cette période
            Float totalIncome = 0.0f;
            for (Income income : incomes) {
                if (income.getPeriod().equals(period)) {
                    // Multiplication par un facteur pour rendre les revenus plus visibles
                    // Mais pas trop élevés pour rester dans la limite de 30000
                    totalIncome = income.getTotal() * 3.0f;
                    logger.debug("Found income total {} for period {}", totalIncome, period);
                    break;
                }
            }

            // Ajouter les données même si l'une des valeurs est 0
            if (totalIncome > 0 || totalExpense > 0) {
                incomeSeries.getData().add(new XYChart.Data<>(period, totalIncome));
                expenseSeries.getData().add(new XYChart.Data<>(period, totalExpense));

                logger.debug("Added comparison data for period {}: Income={}, Expense={}",
                        period, totalIncome, totalExpense);
            }
        }

        // Vérifier si des données ont été ajoutées aux séries
        if (incomeSeries.getData().isEmpty() && expenseSeries.getData().isEmpty()) {
            logger.warn("No data added to comparison chart series");
            return;
        }

        // Mise à jour du graphique
        incomeExpenseComparisonChart.getData().clear();

        // Inverser l'ordre pour que les revenus apparaissent en premier (avec la couleur verte)
        incomeExpenseComparisonChart.getData().add(incomeSeries);  // Revenus en vert (default-color0)
        incomeExpenseComparisonChart.getData().add(expenseSeries); // Dépenses en rouge (default-color1)

        // Ajuster les propriétés du graphique
        incomeExpenseComparisonChart.setTitle("Comparaison Revenus / Dépenses");
        incomeExpenseComparisonChart.setAnimated(false); // Désactiver les animations pour éviter les problèmes d'affichage

        // Activer et positionner la légende
        incomeExpenseComparisonChart.setLegendVisible(true);
        incomeExpenseComparisonChart.setLegendSide(javafx.geometry.Side.TOP);

        // S'assurer que l'échelle est correcte
        NumberAxis yAxis = (NumberAxis) incomeExpenseComparisonChart.getYAxis();
        if (yAxis.getUpperBound() != 100000) {
            yAxis.setUpperBound(100000);
        }

        // Appliquer des styles directement aux séries pour garantir les couleurs correctes
        int index = 0;
        for (XYChart.Series<String, Number> series : incomeExpenseComparisonChart.getData()) {
            String color = (index == 0) ? "#4caf50" : "#f44336"; // Vert pour revenus, rouge pour dépenses
            for (XYChart.Data<String, Number> data : series.getData()) {
                if (data.getNode() != null) {
                    // Appliquer le style directement au nœud quand il devient disponible
                    data.getNode().setStyle("-fx-bar-fill: " + color + ";");
                } else {
                    // Si le nœud n'est pas encore disponible, ajouter un écouteur
                    data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                        if (newNode != null) {
                            newNode.setStyle("-fx-bar-fill: " + color + ";");
                        }
                    });
                }
            }

            // Aussi, appliquer le style à la légende
            if (series.getNode() != null) {
                // Colorer les symboles de légende
                series.getNode().setStyle("-fx-stroke: " + color + "; -fx-fill: " + color + ";");
            }

            index++;
        }

        logger.info("Comparison chart updated successfully with visible legend");
    }
}
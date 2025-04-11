package com.fahze.demojavafx;

import com.fahze.demojavafx.db.IncomeDAO;
import com.fahze.demojavafx.service.CurrencyService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class IncomeController {
    private static final Logger logger = LogManager.getLogger(IncomeController.class);

    @FXML private TableView<Income> tableView;
    @FXML private TableColumn<Income, String> periodColumn;
    @FXML private TableColumn<Income, String> totalColumn;
    @FXML private TableColumn<Income, String> salaryColumn;
    @FXML private TableColumn<Income, String> benefitsColumn;
    @FXML private TableColumn<Income, String> selfEmploymentColumn;
    @FXML private TableColumn<Income, String> passiveColumn;
    @FXML private TableColumn<Income, String> otherColumn;
    @FXML private CurrencySwitchButton currencySwitch;

    private ObservableList<Income> data = FXCollections.observableArrayList();
    private boolean isEuroMode = true;
    private Float exchangeRate = 1.0f;

    @FXML
    public void initialize() {
        logger.info("Initializing IncomeController and setting up TableView");

        // Charger les données depuis la base de données
        data = IncomeDAO.getAllIncomes();

        // Configurer les cellules du tableau
        setupTableCells();

        // Set data
        tableView.setItems(data);

        // Initialiser le switch de devise
        setupCurrencySwitch();

        logger.info("TableView initialized with {} incomes", data.size());
    }

    private void setupTableCells() {
        periodColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPeriod()));

        totalColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(formatCurrency(cellData.getValue().getTotal(), isEuroMode)));

        salaryColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(formatCurrency(cellData.getValue().getSalary(), isEuroMode)));

        benefitsColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(formatCurrency(cellData.getValue().getBenefits(), isEuroMode)));

        selfEmploymentColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(formatCurrency(cellData.getValue().getSelfEmployment(), isEuroMode)));

        passiveColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(formatCurrency(cellData.getValue().getPassive(), isEuroMode)));

        otherColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(formatCurrency(cellData.getValue().getOther(), isEuroMode)));
    }

    private String formatCurrency(Float value, boolean isEuro) {
        return String.format(isEuro ? "€%.2f" : "$%.2f", value);
    }

    private void setupCurrencySwitch() {
        CompletableFuture<Double> exchangeRateFuture = CurrencyService.getExchangeRate();
        exchangeRateFuture.thenAccept(rate -> {
            exchangeRate = rate.floatValue();
            logger.info("Exchange rate set to: {}", exchangeRate);

            // Mettre à jour le taux de change pour toutes les lignes
            for (Income income : data) {
                income.setExchangeRate(exchangeRate);
            }
        });

        if (currencySwitch != null) {
            currencySwitch.setOnAction(event -> {
                isEuroMode = currencySwitch.isEuro();
                logger.info("Currency button clicked, switching to: {}", isEuroMode ? "EUR" : "USD");
                updateTableWithCurrency();
            });
        } else {
            logger.warn("Currency switch button not found in FXML");
        }
    }

    private void updateTableWithCurrency() {
        logger.info("Updating table with currency: {}", isEuroMode ? "EUR" : "USD");

        for (Income income : data) {
            if (isEuroMode) {
                if (!income.isInEuro()) {
                    income.convertToEuro();
                }
            } else {
                if (income.isInEuro()) {
                    income.convertToUsd();
                }
            }
        }

        setupTableCells();
        tableView.refresh();

        logger.info("Table updated with {} incomes in {}", data.size(), isEuroMode ? "EUR" : "USD");
    }

    @FXML
    public void openAddIncomeDialog() {
        try {
            logger.info("Opening Add Income Dialog");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("addIncomeDialog.fxml"));
            GridPane gridPane = loader.load();

            AddIncomeDialogController controller = loader.getController();

            // Create Dialog
            Dialog<Income> dialog = new Dialog<>();
            dialog.setTitle("Ajouter un revenu");
            dialog.setHeaderText("Remplissez les champs ci-dessous:");
            dialog.initModality(Modality.APPLICATION_MODAL);

            // Add Buttons
            ButtonType submitButton = new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(submitButton, cancelButton);

            dialog.getDialogPane().setContent(gridPane);

            // Handle submission
            dialog.setResultConverter(button -> {
                if (button == submitButton) {
                    return controller.getData();
                }
                return null;
            });

            // Show dialog
            Optional<Income> result = dialog.showAndWait();
            result.ifPresent(income -> {
                logger.info("Processing new income for period: {}", income.getPeriod());

                // Ajouter le revenu à la base de données
                if (IncomeDAO.insertIncome(income)) {
                    // Si l'insertion en base réussit, ajouter à la liste des revenus
                    data.add(income);

                    // Mettre à jour le tableau pour inclure le nouveau revenu
                    setupTableCells();
                    tableView.refresh();

                    logger.info("Successfully added income for period: {}", income.getPeriod());
                } else {
                    logger.warn("Failed to add income for period: {}", income.getPeriod());
                }
            });

        } catch (IOException e) {
            logger.error("Error opening Add Income Dialog", e);
        }
    }
}
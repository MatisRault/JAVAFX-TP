package com.fahze.demojavafx;

import com.fahze.demojavafx.db.ExpenseDAO;
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

public class HelloController {
    private static final Logger logger = LogManager.getLogger(HelloController.class);

    @FXML private TableView<Line> tableView;
    @FXML private TableColumn<Line, String> periodColumn;
    @FXML private TableColumn<Line, String> totalColumn;
    @FXML private TableColumn<Line, String> housingColumn;
    @FXML private TableColumn<Line, String> foodColumn;
    @FXML private TableColumn<Line, String> outingColumn;
    @FXML private TableColumn<Line, String> transportColumn;
    @FXML private TableColumn<Line, String> travelColumn;
    @FXML private TableColumn<Line, String> taxesColumn;
    @FXML private TableColumn<Line, String> otherColumn;
    @FXML private CurrencySwitchButton currencySwitch;

    private ObservableList<Line> data = FXCollections.observableArrayList();
    private boolean isEuroMode = true;

    @FXML
    public void initialize() {
        logger.info("Initializing HelloController and setting up TableView");

        // Charger les données depuis la base de données
        data = ExpenseDAO.getAllExpenses();

        // Configurer les cellules du tableau
        setupTableCells();

        // Set data
        tableView.setItems(data);

        // Initialiser le switch de devise
        setupCurrencySwitch();

        logger.info("TableView initialized with {} expenses", data.size());
    }

    private void setupTableCells() {
        // Configurer les cellules avec des formateurs personnalisés pour afficher la devise
        periodColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPeriod()));

        // Utiliser des cellFactories personnalisées pour les montants
        totalColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(formatCurrency(cellData.getValue().getTotal(), isEuroMode)));

        housingColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(formatCurrency(cellData.getValue().getHousing(), isEuroMode)));

        foodColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(formatCurrency(cellData.getValue().getFood(), isEuroMode)));

        outingColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(formatCurrency(cellData.getValue().getOuting(), isEuroMode)));

        transportColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(formatCurrency(cellData.getValue().getTransport(), isEuroMode)));

        travelColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(formatCurrency(cellData.getValue().getTravel(), isEuroMode)));

        taxesColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(formatCurrency(cellData.getValue().getTaxes(), isEuroMode)));

        otherColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(formatCurrency(cellData.getValue().getOther(), isEuroMode)));
    }

    private String formatCurrency(Float value, boolean isEuro) {
        return String.format(isEuro ? "€%.2f" : "$%.2f", value);
    }

    private void setupCurrencySwitch() {
        // Initialiser le service de devise
        CurrencyService.getExchangeRate().thenAccept(rate -> {
            CurrencyService.setEurToUsdRate(rate);
            logger.info("Exchange rate set to: {}", rate);
        });

        // Configurer le bouton de switch
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

        for (Line expense : data) {
            if (isEuroMode) {
                // Si on revient à l'euro, on reconvertit depuis USD
                if (!expense.isInEuro()) {
                    logger.debug("Converting expense {} from USD to EUR", expense.getPeriod());
                    expense.convertToEuro();
                }
            } else {
                // Si on passe au dollar, on convertit vers USD
                if (expense.isInEuro()) {
                    logger.debug("Converting expense {} from EUR to USD", expense.getPeriod());
                    expense.convertToUsd();
                }
            }
        }

        // Reconfigurer les cellules avec le nouveau format de devise
        setupTableCells();

        // Forcer le rafraîchissement complet du tableau
        tableView.refresh();

        logger.info("Table updated with {} expenses in {}", data.size(), isEuroMode ? "EUR" : "USD");
    }

    @FXML
    public void openAddLineDialog() {
        try {
            logger.info("Opening Add Line Dialog");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("addLineDialog.fxml"));
            GridPane gridPane = loader.load();

            AddLineDialogController controller = loader.getController();

            // Create Dialog
            Dialog<Line> dialog = new Dialog<>();
            dialog.setTitle("Ajouter une ligne");
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
            Optional<Line> result = dialog.showAndWait();
            result.ifPresent(expense -> {
                logger.info("Processing new expense for period: {}", expense.getPeriod());

                // Ajouter la dépense à la base de données
                if (ExpenseDAO.insertExpense(expense)) {
                    // Si l'insertion en base réussit, ajouter à la liste des dépenses
                    data.add(expense);

                    // Mettre à jour le tableau pour inclure la nouvelle dépense
                    setupTableCells();
                    tableView.refresh();

                    logger.info("Successfully added expense for period: {}", expense.getPeriod());
                } else {
                    logger.warn("Failed to add expense for period: {}", expense.getPeriod());
                }
            });

        } catch (IOException e) {
            logger.error("Error opening Add Line Dialog", e);
        }
    }

    @FXML
    protected void onAdd(ActionEvent event){
        logger.debug("Add button pressed");
        event.consume();
    }
}
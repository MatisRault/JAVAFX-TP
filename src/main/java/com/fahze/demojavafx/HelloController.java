package com.fahze.demojavafx;

import com.fahze.demojavafx.db.ExpenseDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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

    private ObservableList<Line> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        logger.info("Initializing HelloController and setting up TableView");

        // Link TableView columns to Line properties
        periodColumn.setCellValueFactory(new PropertyValueFactory<>("period"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
        housingColumn.setCellValueFactory(new PropertyValueFactory<>("housing"));
        foodColumn.setCellValueFactory(new PropertyValueFactory<>("food"));
        outingColumn.setCellValueFactory(new PropertyValueFactory<>("outing"));
        transportColumn.setCellValueFactory(new PropertyValueFactory<>("transport"));
        travelColumn.setCellValueFactory(new PropertyValueFactory<>("travel"));
        taxesColumn.setCellValueFactory(new PropertyValueFactory<>("taxes"));
        otherColumn.setCellValueFactory(new PropertyValueFactory<>("other"));

        // Charger les données depuis la base de données
        data = ExpenseDAO.getAllExpenses();

        // Set data
        tableView.setItems(data);

        logger.info("TableView initialized with {} expenses", data.size());
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
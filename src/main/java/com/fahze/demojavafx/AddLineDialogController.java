package com.fahze.demojavafx;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AddLineDialogController {
    @FXML private TextField periodField;
    @FXML private TextField totalField;
    @FXML private TextField housingField;
    @FXML private TextField foodField;
    @FXML private TextField outingField;
    @FXML private TextField transportField;
    @FXML private TextField travelField;
    @FXML private TextField taxesField;
    @FXML private TextField otherField;

    public Line getData() {
        return new Line(
                periodField.getText(),
                Float.parseFloat(totalField.getText()),
                Float.parseFloat(housingField.getText()),
                Float.parseFloat(foodField.getText()),
                Float.parseFloat(outingField.getText()),
                Float.parseFloat(transportField.getText()),
                Float.parseFloat(travelField.getText()),
                Float.parseFloat(taxesField.getText()),
                Float.parseFloat(otherField.getText())
        );
    }
}

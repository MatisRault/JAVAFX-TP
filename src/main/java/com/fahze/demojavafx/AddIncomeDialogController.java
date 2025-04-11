package com.fahze.demojavafx;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AddIncomeDialogController {
    @FXML private TextField periodField;
    @FXML private TextField totalField;
    @FXML private TextField salaryField;
    @FXML private TextField benefitsField;
    @FXML private TextField selfEmploymentField;
    @FXML private TextField passiveField;
    @FXML private TextField otherField;

    public Income getData() {
        return new Income(
                periodField.getText(),
                Float.parseFloat(totalField.getText()),
                Float.parseFloat(salaryField.getText()),
                Float.parseFloat(benefitsField.getText()),
                Float.parseFloat(selfEmploymentField.getText()),
                Float.parseFloat(passiveField.getText()),
                Float.parseFloat(otherField.getText())
        );
    }
}
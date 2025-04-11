package com.fahze.demojavafx;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class CurrencySwitchButton extends HBox {

    private Button button;
    private boolean isEuro = true;
    private final ObjectProperty<EventHandler<MouseEvent>> onAction = new SimpleObjectProperty<>();

    public CurrencySwitchButton() {
        initialize();
    }

    private void initialize() {
        button = new Button("€");
        button.setPrefWidth(40);
        button.getStyleClass().add("currency-switch-button");

        button.setOnMouseClicked(event -> {
            isEuro = !isEuro;
            button.setText(isEuro ? "€" : "$");
            if (onAction.get() != null) {
                onAction.get().handle(event);
            }
        });

        getChildren().add(button);
    }

    public boolean isEuro() {
        return isEuro;
    }

    public void setOnAction(EventHandler<MouseEvent> handler) {
        onAction.set(handler);
    }

    public EventHandler<MouseEvent> getOnAction() {
        return onAction.get();
    }

    public ObjectProperty<EventHandler<MouseEvent>> onActionProperty() {
        return onAction;
    }
}
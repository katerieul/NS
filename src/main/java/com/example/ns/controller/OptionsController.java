package com.example.ns.controller;

import com.example.ns.Options;
import com.example.ns.Scene;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

import java.io.IOException;

public class OptionsController {
    @FXML
    private ChoiceBox<String> choicebox;

    @FXML
    private void initialize() {
        choicebox.setItems(FXCollections.observableArrayList("White", "Gray", "Dark"));

        choicebox.getSelectionModel().select(Options.getInstance().theme.getValue());

        choicebox.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldValue, newValue) -> Options.getInstance().theme.setValue(newValue));
    }

    @FXML
    public void onBackButtonClick(ActionEvent actionEvent) throws IOException {
        Scene.MAIN_MENU.load(actionEvent);
    }
}

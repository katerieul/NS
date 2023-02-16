package com.example.ns.controller;

import com.example.ns.Scene;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class MainMenuController {
    @FXML
    public void onOptionsButtonClick(ActionEvent actionEvent) throws IOException {
        Scene.OPTIONS.load(actionEvent);
    }
    @FXML
    public void onStartButtonClick(ActionEvent actionEvent) throws IOException {
        Scene.SIMULATION.load(actionEvent);
    }
}
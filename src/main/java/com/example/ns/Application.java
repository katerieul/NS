package com.example.ns;

import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        Scene.MAIN_MENU.load(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}
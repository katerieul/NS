package com.example.ns;

import com.pixelduke.control.skin.FXSkins;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

public enum Scene {
    MAIN_MENU("main-menu", "Main Menu"),
    OPTIONS("options", "Options"),
    SIMULATION("simulation", "Simulation", true);

    final String fxml;
    final String title;
    final boolean fullscreen;
    static final int WIDTH = 400;
    static final int HEIGHT = 400;
    static final Image logoImage = new Image(Objects.requireNonNull(
            Scene.class.getResourceAsStream("assets/logo.png")));

    static Stage currentStage;

    Scene(String fxml, String title) {
        this(fxml, title, false);
    }

    Scene(String fxml, String title, boolean fullscreen) {
        this.fxml = getFxmlPath(fxml);
        this.title = title;
        this.fullscreen = fullscreen;
    }

    @Contract(pure = true)
    private static @NotNull String getFxmlPath(String fxml) {
        return "view/" + fxml + ".fxml";
    }

    private void setTheme(@NotNull String theme) {
        ObservableList<String> stylesheets = currentStage.getScene().getStylesheets();

        stylesheets.clear();
        stylesheets.add(Objects.requireNonNull(this.getClass().getResource(
                "stylesheets/themes/" + theme.toLowerCase() + ".css")).toExternalForm());
        stylesheets.add(Objects.requireNonNull(this.getClass().getResource(
                "stylesheets/style.css")).toExternalForm());
        stylesheets.add(FXSkins.getStylesheetURL());
    }

    public void load(@NotNull Stage stage) throws IOException {
        currentStage = stage;
        if (stage.getIcons().isEmpty())
            stage.getIcons().add(logoImage);
        javafx.scene.Scene scene = new javafx.scene.Scene(
                new FXMLLoader(Application.class.getResource(fxml)).load(), WIDTH, HEIGHT);

        stage.setScene(scene);
        stage.setMaximized(fullscreen);
        stage.setTitle(title + " - NS");

        Options.getInstance().theme.addListener((observableValue, oldValue, newValue) -> setTheme(newValue));
        setTheme(Options.getInstance().theme.getValue());

        stage.show();
    }

    public void load(@NotNull ActionEvent actionEvent) throws IOException {
        load((Stage) ((Node) actionEvent.getSource()).getScene().getWindow());
    }
}

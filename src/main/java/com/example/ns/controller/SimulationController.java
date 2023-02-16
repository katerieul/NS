package com.example.ns.controller;

import com.example.ns.Scene;
import com.example.ns.core.Simulation;
import javafx.animation.AnimationTimer;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.IOException;

public class SimulationController {

    @AllArgsConstructor
    private enum State {
        READY("Start"),
        RUNNING("Stop"),
        PAUSED("Continue");

        @Getter
        private final String startStopButtonText;
    }

    @FXML
    public Canvas canvas;
    @FXML
    public Pane pane;
    @FXML
    public Slider population_slider;
    @FXML
    public Slider food_slider;
    @FXML
    public Slider sector_slider;

    public Button startStopButton;

    private Simulation simulation;
    private AnimationTimer timer;
    private Long latestTime;
    private Property<State> state;

    @SneakyThrows
    private void handleTick(long time) {
        if (latestTime != null) {
            simulation.update(((double)(time - latestTime) / 1000_000) / 100);
            simulation.render(canvas.getGraphicsContext2D());
        }
        latestTime = time;
    }

    @FXML
    private void initialize() {
        state = new SimpleObjectProperty<>();
        state.addListener((observableValue, oldValue, newValue) ->
                startStopButton.setText(newValue.getStartStopButtonText()));
        canvas.widthProperty().bind(pane.widthProperty());
        canvas.heightProperty().bind(pane.heightProperty());
        canvas.widthProperty().addListener(this::onCanvasSizeChanged);
        canvas.heightProperty().addListener(this::onCanvasSizeChanged);
    }

    private void onCanvasSizeChanged(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
        if (canvas.widthProperty().get() != 0 && canvas.heightProperty().get() != 0)
            reset();
    }

    @FXML
    public void onBackButtonClick(ActionEvent actionEvent) throws IOException {
        Scene.MAIN_MENU.load(actionEvent);
    }

    @FXML
    public void onResetButtonClick(ActionEvent actionEvent) {
        reset();
    }

    private void reset() {
        if (timer != null)
            timer.stop();

        state.setValue(State.READY);
        simulation = new Simulation(canvas.widthProperty(), canvas.heightProperty());
        latestTime = null;
        timer = new AnimationTimer() {
            @Override
            public void handle(long time) {
                handleTick(time);
            }
        };
        simulation.render(canvas.getGraphicsContext2D());
    }

    private void start() {
        state.setValue(State.RUNNING);
        timer.start();
    }

    private void stop() {
        state.setValue(State.PAUSED);
        latestTime = null;
        timer.stop();
    }

    @FXML
    public void onStopStartButtonClick(ActionEvent actionEvent) {
        if (state.getValue() == State.RUNNING)
            stop();
        else
            start();
    }

    @FXML
    public void onFoodSliderChanged() {
        Simulation.FOOD_COUNT = (int) food_slider.getValue();
    }

    @FXML
    public void onPopulationSliderChanged() {
        Simulation.ORGANISM_COUNT = (int) population_slider.getValue();
    }

    @FXML
    public void onSectorSliderChanged() {
        Simulation.SECTORS = (int) sector_slider.getValue();
    }

}
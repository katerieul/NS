package com.example.ns.core;

import com.example.ns.Scene;
import com.example.ns.controller.SimulationController;
import com.example.ns.core.entity.Food;
import com.example.ns.core.entity.Organism;
import com.example.ns.core.math.Vec2;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Simulation {
    public static int FOOD_COUNT = 100;

    public static int SECTORS = Organism.NUMBER_OF_SECTORS;

    public static int ORGANISM_COUNT = 20;
    @Getter
    List<Organism> organisms = new ArrayList<>();
    @Getter
    List<Food> food = new ArrayList<>();
    public DoubleProperty width = new SimpleDoubleProperty();
    public DoubleProperty height = new SimpleDoubleProperty();

    public Simulation(@NotNull DoubleProperty widthProperty, @NotNull DoubleProperty heightProperty) {
        width.bind(widthProperty);
        height.bind(heightProperty);

        generateFood();
        generateOrganisms();
    }

    private void generateFood() {
        while (food.size() < FOOD_COUNT)
            food.add(new Food(this));
    }

    private void generateOrganisms() {
        while (organisms.size() < ORGANISM_COUNT)
            this.organisms.add(new Organism(this));
    }

    public void update(double deltaTime) {
        List<Sprite> sprites = Stream.concat(organisms.stream(), food.stream()).toList();
        sprites.forEach(x -> x.update(deltaTime));
        sprites.forEach(Sprite::postUpdate);

        organisms.removeIf(x -> !x.isAlive());
        food.removeIf(x -> !x.isAlive());
        generateFood();
    }

    public void render(GraphicsContext graphicsContext) {
        graphicsContext.setFill(Color.DARKGRAY);
        graphicsContext.fillRect(0, 0, width.get(), height.get());
        organisms.forEach(x -> {
            try {
                x.render(graphicsContext);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        food.forEach(x -> {
            try {
                x.render(graphicsContext);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void addOrganism(Organism organism) {
        organisms.add(organism);
    }
}

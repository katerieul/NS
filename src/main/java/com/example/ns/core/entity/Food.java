package com.example.ns.core.entity;

import com.example.ns.core.Simulation;
import com.example.ns.core.Sprite;
import com.example.ns.core.math.Vec2;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Random;

public class Food extends Sprite {
    private static final double MIN_ENERGY = 4;
    private static final double MAX_ENERGY = 10;
    private static final double RADIUS_NORMALIZATION_FACTOR = 1;

    private final double energy = new Random().nextDouble(MIN_ENERGY, MAX_ENERGY);

    public Food(Simulation simulation) {
        super(simulation, new Vec2(simulation.width.get() * Math.random(), simulation.height.get() * Math.random()));
    }

    private double calculateRadius() {
        return energy * RADIUS_NORMALIZATION_FACTOR;
    }

    @Override
    public double getCollisionRadius() {
        return calculateRadius();
    }

    @Override
    protected void draw(@NotNull GraphicsContext graphicsContext) {
        double radius = calculateRadius();
        graphicsContext.setFill(Color.BLUE);
        graphicsContext.fillOval(-radius, -radius, radius, radius);
    }

    @Override
    public void postUpdate() {
        Optional<Organism> luckyOrganism = simulation.getOrganisms().stream()
                .filter(Organism::isAlive).filter(this::collide).findFirst();
        if (luckyOrganism.isPresent()) {
            luckyOrganism.get().addEnergy(energy);
            die();
        }
    }
}

package com.example.ns.core.entity;

import com.example.ns.core.Simulation;
import com.example.ns.core.Sprite;
import com.example.ns.core.math.Direction;
import com.example.ns.core.math.Vec2;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Organism extends Sprite {
    // -energy at each iteration, +energy when eating
    // low energy - death
    // size mutates - bigger size - bigger energy loss
    // speed mutates

    public static final Image organismImage;

    static {
        try {
            organismImage = new Image(new FileInputStream("src/main/resources/com/example/ns/assets/organism.png"), 20, 20, false, false);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    private static int NUMBER_OF_ORGANISMS = 0;
    private static final double VISION_NORMALIZATION_FACTOR = 20;
    private static final double ENERGY_NORMALIZATION_FACTOR = .002;
    private static final double BASE_ENERGY = 10;
    private static final int BASE_SPEED = 500;

    private static final double BASE_SIZE = 6;
    public static final int NUMBER_OF_SECTORS = 8;

    private static final double MIN_SIZE = 2;
    private static final double MIN_SPEED = 5;

    @Getter
    private double energy;
    @Getter
    private double size;
    @Getter
    private final double speed;
    @Getter
    private final int generation;

    public Organism(Simulation simulation, Vec2 position, double size, double energy, double speed, int generation) {
        super(simulation, position);
        this.size = size;
        this.energy = energy;
        this.speed = speed;
        this.generation = generation;
        NUMBER_OF_ORGANISMS++;
    }

    public Organism(Simulation simulation) {
        this(simulation,
                new Vec2(simulation.width.get() * Math.random(), simulation.height.get() * Math.random()),
                BASE_SIZE, BASE_ENERGY, BASE_SPEED, 0);
    }


    private double calculateRadius() {
        return size;
    }

    @Override
    public double getCollisionRadius() {
        return calculateRadius();
    }

    @Override
    protected void draw(@NotNull GraphicsContext graphicsContext) {
        double radius = calculateRadius();
        graphicsContext.drawImage(organismImage, position.x, position.y);
        graphicsContext.setFill(Color.RED);
        graphicsContext.fillOval(-radius, -radius, radius, radius);
    }

    private double calculateVisionRadius() {
        return size * VISION_NORMALIZATION_FACTOR;
    }

    @Contract(" -> new")
    private @NotNull Direction calculateDirection() {

        double visionRadius = calculateVisionRadius();
        Optional<Food> food = simulation.getFood().stream()
                .filter(x -> x.getPosition().distanceTo(position) <= visionRadius).findAny();
        if (food.isPresent())
            return this.getPosition().relativeTo(food.get().getPosition());

        Integer[] dirs = new Integer[NUMBER_OF_SECTORS];
        for (int i = 0; i < NUMBER_OF_SECTORS; i++) {
            dirs[i] = 0;
        }
        List<Organism> others = simulation.getOrganisms();
        for (Organism other : others) {
            if (other.position != position) {
                double angle = Math.atan2(other.position.x - position.x, other.position.y - position.y);
                angle += Math.PI;
                dirs[(int) (Math.floor(angle * (NUMBER_OF_SECTORS - 1) / (2 * Math.PI)))]++;
            }
        }
        double dir = (Collections.min(Arrays.asList(dirs)) - 0.5) * Math.PI / NUMBER_OF_SECTORS;
        return new Direction(Math.cos(dir), Math.sin(dir));
    }

    private double calculateEnergyLoss() {
        return size / 100;
    }

    private double calculateRequiredEnergy() {
        return size * BASE_ENERGY/ 100;
    }

    private double calculateMutationEnergy() {
        return Math.pow(Math.log(generation + 2),3)*Math.pow(BASE_ENERGY, 3);
    }

    @Override
    public void postUpdate() {
        Optional<Organism> stronger = simulation.getOrganisms().stream()
                .filter(Sprite::isAlive)
                .filter(this::collide)
                .filter(x -> 2 * getSize() < x.getSize()).findAny();
        if (stronger.isPresent()) {
            stronger.get().addEnergy(energy);
            die();
        }
        if (calculateMutationEnergy() <= energy) {
            double delta = (Math.random() * 10 + 1) / ((Math.random() * Math.log(generation)) * Math.pow((Math.random() * 10), 3) + 1);
            double mutatedSize = Math.max(size * (1 + Math.pow(-1, Math.round(Math.random())) * delta), MIN_SIZE);
            double newSpeed = Math.max(speed * (1 + Math.pow(-1, Math.round(Math.random())) * delta), MIN_SPEED);

            simulation.addOrganism(new Organism(simulation, position, mutatedSize, energy / 2, newSpeed, generation + 1));
            simulation.addOrganism(new Organism(simulation, position, mutatedSize, energy / 2, newSpeed, generation + 1));

            die();
        }
    }

    @Override
    public void update(double deltaTime) { // in seconds
        position = position.translate(calculateDirection().scale(deltaTime * getSpeed()));
        energy -= calculateEnergyLoss() * deltaTime;
        if (energy < calculateRequiredEnergy())
            die();
    }

    public void addEnergy(double energy) {
        this.energy += energy;
    }
}

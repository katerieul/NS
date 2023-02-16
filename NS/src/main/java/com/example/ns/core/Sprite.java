package com.example.ns.core;

import com.example.ns.core.math.Vec2;
import javafx.scene.canvas.GraphicsContext;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public abstract class Sprite {
    protected final Simulation simulation;
    protected Vec2 position;

    @Getter
    private boolean isAlive = true;

    public Sprite(Simulation simulation, Vec2 position) {
        this.simulation = simulation;
        this.position = position;
    }

    public void render(@NotNull GraphicsContext graphicsContext) {
        graphicsContext.translate(position.x, position.y);
        draw(graphicsContext);
        graphicsContext.translate(-position.x, -position.y);
    }

    protected boolean collide(@NotNull Sprite anotherSprite) {
        return getPosition().distanceTo(anotherSprite.getPosition()) < getCollisionRadius() + anotherSprite.getCollisionRadius();
    }

    public Vec2 getPosition() {
        return position;
    }

    public void die() {
        isAlive = false;
    }

    public abstract double getCollisionRadius();

    protected abstract void draw(GraphicsContext graphicsContext);

    public void update(double deltaTime) {
        // do nothing by default
    }

    public void postUpdate() {
        // do nothing by default
    }
}

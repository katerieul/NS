package com.example.ns.core.math;

public class Direction extends Vec2 {
    public Direction(double x, double y) {
        super(x, y);
    }

    public Vec2 scale(double k) {
        return normalized().multiply(k);
    }
}

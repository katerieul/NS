package com.example.ns.core.math;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@Data
public class Vec2 {
    public double x, y;

    public Vec2 normalized() {
        if (x == 0 && y == 0)
            return new Vec2(0 , 0);
        return new Vec2(x / (x * x + y * y), y / (x * x + y * y));
    }

    public Vec2 translate(@NotNull Vec2 other) {
        return new Vec2(x + other.x, y + other.y);
    }

    public Direction relativeTo(@NotNull Vec2 other) {
        return new Direction(other.x - x, other.y - y);
    }

    public double distanceTo(@NotNull Vec2 other) {
        return Math.sqrt((x - other.x) * (x - other.x) + (y - other.y) * (y - other.y));
    }

    public Vec2 multiply(double k) {
        return new Vec2(x * k, y * k);
    }
}

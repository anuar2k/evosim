package me.anuar2k.engine.property;

import me.anuar2k.engine.util.Direction;

public class DirectionProperty implements Property {
    private Direction direction;

    public DirectionProperty(Direction initialDirection) {
        this.direction = initialDirection;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}

package me.anuar2k.engine.util;

public enum Direction {
    N(new Coord2D(0, 1), "north"),
    NE(new Coord2D(1, 1), "north-east"),
    E(new Coord2D(1, 0), "east"),
    SE(new Coord2D(1, -1), "south-east"),
    S(new Coord2D(0, -1), "south"),
    SW(new Coord2D(-1, -1), "south-west"),
    W(new Coord2D(-1, 0), "west"),
    NW(new Coord2D(-1, 1), "north-west");

    private final String prettyValue;
    private final Coord2D coordDelta;

    Direction(Coord2D coordDelta, String prettyValue) {
        this.prettyValue = prettyValue;
        this.coordDelta = coordDelta;
    }

    public Direction rotate(int delta) {
        return Direction.values()[Math.floorMod(this.ordinal() + delta, Direction.values().length)];
    }

    public Coord2D getCoordDelta() {
        return this.coordDelta;
    }

    @Override
    public String toString() {
        return this.prettyValue;
    }
}

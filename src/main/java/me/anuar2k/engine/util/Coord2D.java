package me.anuar2k.engine.util;

import me.anuar2k.engine.worldmap.WorldMap;

import java.util.Objects;

public final class Coord2D {
    public final int x;
    public final int y;
    private final int cachedHash;

    public Coord2D(int x, int y) {
        this.x = x;
        this.y = y;
        this.cachedHash = Objects.hash(x, y);
    }

    public Coord2D add(Coord2D other) {
        return new Coord2D(this.x + other.x, this.y + other.y);
    }

    public Coord2D wrapAround(int width, int height) {
        return new Coord2D(Math.floorMod(this.x, width), Math.floorMod(this.y, height));
    }

    public Coord2D wrapAround(WorldMap worldMap) {
        return this.wrapAround(worldMap.getWidth(), worldMap.getHeight());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Coord2D)) {
            return false;
        }

        Coord2D other = (Coord2D) obj;
        return this.x == other.x && this.y == other.y;
    }

    @Override
    public int hashCode() {
        return this.cachedHash;
    }
}

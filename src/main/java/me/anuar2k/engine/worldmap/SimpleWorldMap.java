package me.anuar2k.engine.worldmap;

import me.anuar2k.engine.entity.Entity;
import me.anuar2k.engine.util.Coord2D;
import me.anuar2k.engine.property.Property;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimpleWorldMap implements WorldMap {
    private final int width;
    private final int height;
    private final Map<Coord2D, Set<Entity>> entities = new HashMap<>();

    public SimpleWorldMap(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Width/length must be positive values");
        }

        this.width = width;
        this.height = height;

        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                this.entities.put(new Coord2D(x, y), new HashSet<>());
            }
        }
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public void notifyEntityAdded(Entity entity) {
        this.entities.get(entity.getPosition()).add(entity);
    }

    @Override
    public Stream<Entity> getEntities(Coord2D at) {
        return this.entities.get(at.wrapAround(this)).stream();
    }

    @Override
    public Stream<Entity> getEntities(Coord2D at, Class<? extends Property> withProperty) {
        return this.entities.get(at.wrapAround(this))
                .stream()
                .filter(e -> e.hasProperty(withProperty));
    }

    @Override
    public Stream<Entity> getEntities(Coord2D at, Set<Class<? extends Property>> withProperties) {
        return this.entities.get(at.wrapAround(this))
                .stream()
                .filter(e -> withProperties
                        .stream()
                        .allMatch(p -> e.hasProperty(p)));
    }

    @Override
    public void notifyEntityMoved(Entity entity, Coord2D from) {
        this.entities.get(from).remove(entity);
        this.entities.get(entity.getPosition()).add(entity);
    }

    @Override
    public void notifyEntityRemoved(Entity entity) {
        this.entities.get(entity.getPosition()).remove(entity);
    }
}

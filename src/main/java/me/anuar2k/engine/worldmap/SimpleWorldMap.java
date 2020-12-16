package me.anuar2k.engine.worldmap;

import me.anuar2k.engine.entity.Entity;
import me.anuar2k.engine.util.Coord2D;
import me.anuar2k.engine.property.Property;

import java.util.*;
import java.util.stream.Collectors;

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
    public void addEntity(Entity entity) {
        this.entities.get(entity.getPosition()).add(entity);
    }

    @Override
    public Collection<Entity> getEntities(Coord2D at) {
        return Collections.unmodifiableCollection(this.entities.get(at.wrapAround(this)));
    }

    @Override
    public Collection<Entity> getEntities(Coord2D at, Class<? extends Property> withProperty) {
        return this.entities.get(at.wrapAround(this))
                .stream()
                .filter(e -> e.hasProperty(withProperty))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Collection<Entity> getEntities(Coord2D at, Set<Class<? extends Property>> withProperties) {
        return this.entities.get(at.wrapAround(this))
                .stream()
                .filter(e -> withProperties
                        .stream()
                        .allMatch(p -> e.hasProperty(p)))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public void removeEntity(Entity entity) {
        this.entities.get(entity.getPosition()).remove(entity);
    }

    @Override
    public void notifyEntityMoved(Entity entity, Coord2D from, Coord2D to) {
        this.entities.get(from).remove(entity);
        this.entities.get(to).add(entity);
    }
}

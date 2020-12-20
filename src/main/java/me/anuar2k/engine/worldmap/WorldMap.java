package me.anuar2k.engine.worldmap;

import me.anuar2k.engine.entity.Entity;
import me.anuar2k.engine.util.Coord2D;
import me.anuar2k.engine.property.Property;

import java.util.Set;
import java.util.stream.Stream;

public interface WorldMap {
    public Stream<Entity> getEntities(Coord2D at);
    public Stream<Entity> getEntities(Coord2D at, Class<? extends Property> withProperty);
    public Stream<Entity> getEntities(Coord2D at, Set<Class<? extends Property>> withProperties);

    public int getWidth();
    public int getHeight();

    public void notifyEntityAdded(Entity entity);
    public void notifyEntityMoved(Entity entity, Coord2D from);
    public void notifyEntityRemoved(Entity entity);
}

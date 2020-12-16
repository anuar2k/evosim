package me.anuar2k.engine.worldmap;

import me.anuar2k.engine.entity.Entity;
import me.anuar2k.engine.util.Coord2D;
import me.anuar2k.engine.property.Property;

import java.util.Collection;
import java.util.Set;

public interface WorldMap {
    public Collection<Entity> getEntities(Coord2D at);
    public Collection<Entity> getEntities(Coord2D at, Class<? extends Property> withProperty);
    public Collection<Entity> getEntities(Coord2D at, Set<Class<? extends Property>> withProperties);

    public int getWidth();
    public int getHeight();

    public void addEntity(Entity entity);
    public void removeEntity(Entity entity);

    public void notifyEntityMoved(Entity entity, Coord2D from, Coord2D to);
}

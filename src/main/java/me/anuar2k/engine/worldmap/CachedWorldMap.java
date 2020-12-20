package me.anuar2k.engine.worldmap;

import me.anuar2k.engine.entity.Entity;
import me.anuar2k.engine.util.Coord2D;
import me.anuar2k.engine.property.Property;
import me.anuar2k.engine.property.PropertyWatcher;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

//TODO: consider implementing caching entities with given properties
public class CachedWorldMap implements WorldMap, PropertyWatcher {

    public CachedWorldMap(int width, int length, Set<Set<Class<? extends Property>>> cacheRules) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Stream<Entity> getEntities(Coord2D at) {
        return null;
    }

    @Override
    public Stream<Entity> getEntities(Coord2D at, Class<? extends Property> withProperty) {
        return null;
    }

    @Override
    public Stream<Entity> getEntities(Coord2D at, Set<Class<? extends Property>> withProperties) {
        return null;
    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public void notifyEntityAdded(Entity entity) {

    }

    @Override
    public void notifyEntityRemoved(Entity entity) {

    }

    @Override
    public void notifyPropertyAdded(Entity entity, Class<? extends Property> propertyClass) {

    }

    @Override
    public void notifyPropertyRemoved(Entity entity, Class<? extends Property> propertyClass) {

    }

    @Override
    public void notifyEntityMoved(Entity entity, Coord2D from) {

    }
}

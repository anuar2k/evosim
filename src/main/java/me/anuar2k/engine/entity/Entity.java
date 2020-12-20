package me.anuar2k.engine.entity;

import me.anuar2k.engine.util.Coord2D;
import me.anuar2k.engine.property.Property;
import me.anuar2k.engine.property.PropertyWatcher;
import me.anuar2k.engine.worldmap.WorldMap;

import java.util.*;

//TODO: consider extracting a interface
public class Entity {
    private final Map<Class<? extends Property>, Property> properties = new HashMap<>();
    private final WorldMap worldMap;
    private Coord2D position;
    private PropertyWatcher propertyWatcher = null;

    public Entity(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

    public Coord2D getPosition() {
        return this.position;
    }

    public void addToMap(Coord2D at) {
        this.position = at.wrapAround(this.worldMap);
        this.worldMap.notifyEntityAdded(this);
    }

    public void move(Coord2D to) {
        Coord2D from = this.position;
        this.position = to.wrapAround(this.worldMap);
        this.worldMap.notifyEntityMoved(this, from);
    }

    public boolean onMap() {
        return this.position != null;
    }

    public void removeFromMap() {
        this.worldMap.notifyEntityRemoved(this);
        this.position = null;
    }

    public void addProperty(Property property) {
        if (property == null) {
            throw new IllegalArgumentException("Property may not be null");
        }

        if (this.properties.put(property.getClass(), property) != null) {
            throw new IllegalArgumentException("Property of type " + property.getClass().getSimpleName() +  " is already present in the entity");
        }

        if (this.propertyWatcher != null) {
            this.propertyWatcher.notifyPropertyAdded(this, property.getClass());
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Property> T getProperty(Class<T> propertyClass) {
        T result = (T) this.properties.get(propertyClass);

        if (result == null) {
            throw new IllegalArgumentException("Property " + propertyClass.getSimpleName() + " is not present in the entity");
        }

        return result;
    }

    public boolean hasProperty(Class<? extends Property> propertyClass) {
        return this.properties.containsKey(propertyClass);
    }

    public Collection<Property> getAllProperties() {
        return Collections.unmodifiableCollection(this.properties.values());
    }

    public void removeProperty(Class<? extends Property> propertyClass) {
        this.properties.remove(propertyClass);

        if (this.propertyWatcher != null) {
            this.propertyWatcher.notifyPropertyRemoved(this, propertyClass);
        }
    }

    public void attachPropertyWatcher(PropertyWatcher propertyWatcher) {
        this.propertyWatcher = propertyWatcher;
    }

    public void detachPropertyWatcher() {
        this.propertyWatcher = null;
    }
}

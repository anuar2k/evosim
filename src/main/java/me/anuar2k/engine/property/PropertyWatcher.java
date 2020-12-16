package me.anuar2k.engine.property;

import me.anuar2k.engine.entity.Entity;

public interface PropertyWatcher {
    public void notifyPropertyAdded(Entity entity, Class<? extends Property> propertyClass);
    public void notifyPropertyRemoved(Entity entity, Class<? extends Property> propertyClass);
}

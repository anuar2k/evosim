package me.anuar2k.engine.rule;

import me.anuar2k.engine.util.Coord2D;
import me.anuar2k.engine.property.EnergyProperty;
import me.anuar2k.engine.worldmap.WorldMap;

public class DeathRule implements Rule {
    public DeathRule() {

    }

    @Override
    public void applyRule(WorldMap worldMap) {
        for (int x = 0; x < worldMap.getWidth(); x++) {
            for (int y = 0; y < worldMap.getHeight(); y++) {
                worldMap.getEntities(new Coord2D(x, y), EnergyProperty.class)
                        .stream()
                        .filter(e -> e.getProperty(EnergyProperty.class).getEnergy() <= 0)
                        .forEach(e -> worldMap.removeEntity(e));
            }
        }
    }
}

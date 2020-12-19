package me.anuar2k.engine.system;

import me.anuar2k.engine.util.Coord2D;
import me.anuar2k.engine.property.EnergyProperty;
import me.anuar2k.engine.worldmap.WorldMap;

import java.util.stream.Collectors;

public class DeathRule implements Rule {
    private final WorldMap worldMap;

    public DeathRule(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

    @Override
    public void init() {

    }

    @Override
    public void tick() {
        for (int x = 0; x < this.worldMap.getWidth(); x++) {
            for (int y = 0; y < this.worldMap.getHeight(); y++) {
                this.worldMap.getEntities(new Coord2D(x, y), EnergyProperty.class)
                        .filter(e -> e.getProperty(EnergyProperty.class).getEnergy() <= 0)
                        .collect(Collectors.toList())
                        .forEach(e -> this.worldMap.removeEntity(e));
            }
        }
    }
}

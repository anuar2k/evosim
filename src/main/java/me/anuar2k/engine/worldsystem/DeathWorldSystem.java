package me.anuar2k.engine.worldsystem;

import me.anuar2k.engine.util.Coord2D;
import me.anuar2k.engine.property.EnergyProperty;
import me.anuar2k.engine.worldmap.WorldMap;

import java.util.stream.Collectors;

public class DeathWorldSystem implements WorldSystem {
    private WorldMap worldMap = null;
    public DeathWorldSystem() {
        
    }

    @Override
    public void init(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

    @Override
    public void tick() {
        for (int x = 0; x < this.worldMap.getWidth(); x++) {
            for (int y = 0; y < this.worldMap.getHeight(); y++) {
                this.worldMap.getEntities(new Coord2D(x, y), EnergyProperty.class)
                        .filter(e -> e.getProperty(EnergyProperty.class).getEnergy() <= 0)
                        .collect(Collectors.toList())
                        .forEach(e -> e.removeFromMap());
            }
        }
    }
}

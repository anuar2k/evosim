package me.anuar2k.engine.gamesystem;

import me.anuar2k.engine.util.Coord2D;
import me.anuar2k.engine.property.EnergyProperty;
import me.anuar2k.engine.worldmap.WorldMap;

import java.util.stream.Collectors;

public class DeathGameSystem implements GameSystem {
    public DeathGameSystem() {
        
    }

    @Override
    public void init(WorldMap worldMap) {

    }

    @Override
    public void tick(WorldMap worldMap) {
        for (int x = 0; x < worldMap.getWidth(); x++) {
            for (int y = 0; y < worldMap.getHeight(); y++) {
                worldMap.getEntities(new Coord2D(x, y), EnergyProperty.class)
                        .filter(e -> e.getProperty(EnergyProperty.class).getEnergy() <= 0)
                        .collect(Collectors.toList())
                        .forEach(e -> e.removeFromMap());
            }
        }
    }
}

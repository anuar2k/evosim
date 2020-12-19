package me.anuar2k.engine.system;

import me.anuar2k.engine.util.RandSource;
import me.anuar2k.engine.worldmap.WorldMap;

public class AnimalBreedSystem implements System {
    private final WorldMap worldMap;
    private final RandSource randSource;
    private final double minEnergy;

    public AnimalBreedSystem(WorldMap worldMap, RandSource randSource, double minEnergy) {
        this.worldMap = worldMap;
        this.randSource = randSource;
        this.minEnergy = minEnergy;
    }

    @Override
    public void init() {

    }

    @Override
    public void tick() {

    }
}

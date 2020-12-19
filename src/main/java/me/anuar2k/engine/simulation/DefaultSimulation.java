package me.anuar2k.engine.simulation;

import me.anuar2k.engine.system.*;
import me.anuar2k.engine.system.System;
import me.anuar2k.engine.util.RandSource;
import me.anuar2k.engine.worldmap.SimpleWorldMap;
import me.anuar2k.engine.worldmap.WorldMap;

import java.util.List;

public class DefaultSimulation implements Simulation {
    private final WorldMap worldMap;
    private final List<System> systems;

    public DefaultSimulation(RandSource randSource,
                             int width,
                             int height,
                             double startEnergy,
                             double moveEnergy,
                             double plantEnergy,
                             int jungleWidth,
                             int jungleHeight) {
        this.worldMap = new SimpleWorldMap(width, height);

        this.systems = List.of(new AnimalMoveSystem(this.worldMap, randSource, moveEnergy),
                               new DeathSystem(this.worldMap),
                               new AnimalFeedSystem(this.worldMap),
                               new AnimalBreedSystem(this.worldMap, randSource, startEnergy / 2),
                               new PlantGrowthSystem(this.worldMap, randSource, plantEnergy, jungleWidth, jungleHeight));

        for (System system : this.systems) {
            system.init();
        }
    }

    @Override
    public void tick() {
        for (System system : this.systems) {
            system.tick();
        }
    }
}

package me.anuar2k.engine.simulation;

import me.anuar2k.engine.entity.Entity;
import me.anuar2k.engine.property.*;
import me.anuar2k.engine.worldsystem.*;
import me.anuar2k.engine.worldsystem.WorldSystem;
import me.anuar2k.engine.util.Coord2D;
import me.anuar2k.engine.util.Direction;
import me.anuar2k.engine.util.Genome;
import me.anuar2k.engine.util.RandSource;
import me.anuar2k.engine.worldmap.SimpleWorldMap;
import me.anuar2k.engine.worldmap.WorldMap;
import me.anuar2k.gui.config.Config;

import java.util.ArrayList;
import java.util.List;

public class DefaultSimulation implements Simulation {
    private final WorldMap worldMap;
    private final RandSource randSource;
    private final List<WorldSystem> worldSystems;

    public DefaultSimulation(List<WorldSystem> injectedWorldSystems,
                             RandSource randSource,
                             int width,
                             int height,
                             double startEnergy,
                             double moveEnergy,
                             double plantEnergy,
                             int jungleWidth,
                             int jungleHeight) {
        this.worldMap = new SimpleWorldMap(width, height);
        this.randSource = randSource;
        this.worldSystems = new ArrayList<>();

        List<WorldSystem> defaultWorldSystems = List.of(new AnimalMoveWorldSystem(randSource, moveEnergy),
                               new DeathWorldSystem(),
                               new AnimalFeedWorldSystem(),
                               new AnimalBreedWorldSystem(randSource, startEnergy / 2),
                               new PlantGrowthWorldSystem(randSource, plantEnergy, jungleWidth, jungleHeight));

        this.worldSystems.addAll(defaultWorldSystems);
        this.worldSystems.addAll(injectedWorldSystems);

        for (WorldSystem worldSystem : this.worldSystems) {
            worldSystem.init(this.worldMap);
        }

        this.spawnAnimal(new Coord2D(0, 0), startEnergy);
        this.spawnAnimal(new Coord2D(0, jungleHeight - 1), startEnergy);
        this.spawnAnimal(new Coord2D(jungleWidth - 1, 0), startEnergy);
        this.spawnAnimal(new Coord2D(jungleWidth - 1, jungleHeight - 1), startEnergy);
    }

    public DefaultSimulation(List<WorldSystem> injectedWorldSystems,
                             RandSource randSource,
                             Config config) {
        this(injectedWorldSystems,
                randSource,
                config.getMapWidth(),
                config.getMapHeight(),
                config.getStartEnergy(),
                config.getMoveEnergy(),
                config.getPlantEnergy(),
                config.getJungleWidth(),
                config.getJungleHeight());
    }

    private void spawnAnimal(Coord2D position, double startEnergy) {
        Entity newAnimal = new Entity(this.worldMap);
        newAnimal.addProperty(AnimalProperty.INSTANCE);
        newAnimal.addProperty(new EnergyProperty(startEnergy));
        newAnimal.addProperty(new GenomeProperty(Genome.random(this.randSource)));
        newAnimal.addProperty(new DirectionProperty(Direction.random(this.randSource)));
        newAnimal.addProperty(new ChildrenProperty());

        newAnimal.addToMap(position);
    }

    @Override
    public void tick() {
        for (WorldSystem worldSystem : this.worldSystems) {
            worldSystem.tick();
        }

//        System.out.println("-----------------------------------------------");
//        for (int x = 0; x < this.worldMap.getWidth(); x++) {
//            for (int y = 0; y < this.worldMap.getHeight(); y++) {
//                Coord2D cell = new Coord2D(x, y);
//                boolean hasAnimal = this.worldMap.getEntities(cell, AnimalProperty.class).findAny().isPresent();
//                boolean hasPlant = this.worldMap.getEntities(cell, PlantProperty.class).findAny().isPresent();
//                boolean both = hasAnimal && hasPlant;
//
//                if (both) {
//                    System.out.print('X');
//                }
//                else if (hasAnimal) {
//                    System.out.print('A');
//                }
//                else if (hasPlant) {
//                    System.out.print('P');
//                }
//                else {
//                    System.out.print('*');
//                }
//            }
//            System.out.print('\n');
//        }
    }
}

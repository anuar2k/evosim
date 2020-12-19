package me.anuar2k.engine.simulation;

import me.anuar2k.engine.entity.Entity;
import me.anuar2k.engine.property.*;
import me.anuar2k.engine.system.*;
import me.anuar2k.engine.system.Rule;
import me.anuar2k.engine.util.Coord2D;
import me.anuar2k.engine.util.Direction;
import me.anuar2k.engine.util.Genome;
import me.anuar2k.engine.util.RandSource;
import me.anuar2k.engine.worldmap.SimpleWorldMap;
import me.anuar2k.engine.worldmap.WorldMap;

import java.util.List;

public class DefaultSimulation implements Simulation {
    private final WorldMap worldMap;
    private final RandSource randSource;
    private final List<Rule> rules;

    public DefaultSimulation(RandSource randSource,
                             int width,
                             int height,
                             double startEnergy,
                             double moveEnergy,
                             double plantEnergy,
                             int jungleWidth,
                             int jungleHeight) {
        this.worldMap = new SimpleWorldMap(width, height);
        this.randSource = randSource;

        this.rules = List.of(new AnimalMoveRule(this.worldMap, randSource, moveEnergy),
                               new DeathRule(this.worldMap),
                               new AnimalFeedRule(this.worldMap),
                               new AnimalBreedRule(this.worldMap, randSource, startEnergy / 2),
                               new PlantGrowthRule(this.worldMap, randSource, plantEnergy, jungleWidth, jungleHeight));

        for (Rule rule : this.rules) {
            rule.init();
        }

        this.spawnAnimal(new Coord2D(0, 0), startEnergy);
        this.spawnAnimal(new Coord2D(0, jungleHeight - 1), startEnergy);
        this.spawnAnimal(new Coord2D(jungleWidth - 1, 0), startEnergy);
        this.spawnAnimal(new Coord2D(jungleWidth - 1, jungleHeight - 1), startEnergy);
    }

    private void spawnAnimal(Coord2D position, double startEnergy) {
        Entity newAnimal = new Entity(this.worldMap, position);
        newAnimal.addProperty(new AnimalProperty());
        newAnimal.addProperty(new EnergyProperty(startEnergy));
        newAnimal.addProperty(new GenomeProperty(Genome.random(this.randSource)));
        newAnimal.addProperty(new DirectionProperty(Direction.N.rotate(this.randSource.next())));
        this.worldMap.addEntity(newAnimal);
    }

    @Override
    public void tick() {
        for (Rule rule : this.rules) {
            rule.tick();
        }

        System.out.println("-----------------------------------------------");
        for (int x = 0; x < this.worldMap.getWidth(); x++) {
            for (int y = 0; y < this.worldMap.getHeight(); y++) {
                Coord2D cell = new Coord2D(x, y);
                boolean hasAnimal = !this.worldMap.getEntities(cell, AnimalProperty.class).findAny().isEmpty();
                boolean hasPlant = !this.worldMap.getEntities(cell, PlantProperty.class).findAny().isEmpty();
                boolean both = hasAnimal && hasPlant;

                if (both) {
                    System.out.print('X');
                }
                else if (hasAnimal) {
                    System.out.print('A');
                }
                else if (hasPlant) {
                    System.out.print('P');
                }
                else {
                    System.out.print('*');
                }
            }
            System.out.print('\n');
        }
    }
}

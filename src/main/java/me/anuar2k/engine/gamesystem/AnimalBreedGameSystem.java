package me.anuar2k.engine.gamesystem;

import me.anuar2k.engine.entity.Entity;
import me.anuar2k.engine.property.*;
import me.anuar2k.engine.util.*;
import me.anuar2k.engine.worldmap.WorldMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AnimalBreedGameSystem implements GameSystem {
    private final RandSource randSource;
    private final double minEnergy;

    public AnimalBreedGameSystem(RandSource randSource, double minEnergy) {
        this.randSource = randSource;
        this.minEnergy = minEnergy;
    }

    @Override
    public void init(WorldMap worldMap) {

    }

    @Override
    public void tick(WorldMap worldMap) {
        List<Pair<Entity, Coord2D>> childrenToDisplace = new ArrayList<>();
        for (int x = 0; x < worldMap.getWidth(); x++) {
            for (int y = 0; y < worldMap.getHeight(); y++) {
                Coord2D cell = new Coord2D(x, y);

                List<Entity> parents = worldMap.getEntities(cell, AnimalProperty.class)
                        .map(animal -> new Pair<>(animal, animal.getProperty(EnergyProperty.class).getEnergy()))
                        .filter(pair -> pair.right >= this.minEnergy)
                        .sorted((p1, p2) -> Double.compare(p2.right, p1.right))
                        .map(pair -> pair.left)
                        .limit(2)
                        .collect(Collectors.toList());

                if (parents.size() == 2) {
                    Entity parent1 = parents.get(0);
                    Entity parent2 = parents.get(1);
                    EnergyProperty parent1Energy = parent1.getProperty(EnergyProperty.class);
                    EnergyProperty parent2Energy = parent2.getProperty(EnergyProperty.class);
                    ChildrenProperty parent1Children = parent1.getProperty(ChildrenProperty.class);
                    ChildrenProperty parent2Children = parent1.getProperty(ChildrenProperty.class);
                    Genome parent1Genome = parent1.getProperty(GenomeProperty.class).getGenome();
                    Genome parent2Genome = parent2.getProperty(GenomeProperty.class).getGenome();

                    double parent1GivenEnergy = parent1Energy.getEnergy() / 4;
                    double parent2GivenEnergy = parent2Energy.getEnergy() / 4;

                    double childEnergy = parent1GivenEnergy + parent2GivenEnergy;
                    Genome childGenome = Genome.cross(this.randSource, parent1Genome, parent2Genome);
                    Direction childDirection = Direction.random(this.randSource);

                    Entity child = new Entity(worldMap);
                    ChildrenProperty childChildren = new ChildrenProperty();
                    child.addProperty(AnimalProperty.INSTANCE);
                    child.addProperty(new EnergyProperty(childEnergy));
                    child.addProperty(new GenomeProperty(childGenome));
                    child.addProperty(new DirectionProperty(childDirection));
                    child.addProperty(childChildren);
                    parent1Children.addChild(childChildren);
                    parent2Children.addChild(childChildren);

                    childrenToDisplace.add(new Pair<>(child, cell));

                    parent1Energy.adjustEnergy(-parent1GivenEnergy);
                    parent2Energy.adjustEnergy(-parent2GivenEnergy);
                }
            }
        }

        for (Pair<Entity, Coord2D> childToDisplace : childrenToDisplace) {
            Entity child = childToDisplace.left;
            Coord2D centerPosition = childToDisplace.right;

            List<Coord2D> availableNeighbours = Arrays.stream(Direction.values())
                    .map(direction -> direction.getCoordDelta())
                    .filter(delta -> worldMap.getEntities(centerPosition.add(delta)).findAny().isEmpty())
                    .collect(Collectors.toList());

            Coord2D randomDelta;
            if (availableNeighbours.size() > 0) {
                randomDelta = availableNeighbours.get(Math.floorMod(this.randSource.next(), availableNeighbours.size()));
            }
            else {
                randomDelta = Direction.random(this.randSource).getCoordDelta();
            }

            child.addToMap(centerPosition.add(randomDelta));
        }
    }
}

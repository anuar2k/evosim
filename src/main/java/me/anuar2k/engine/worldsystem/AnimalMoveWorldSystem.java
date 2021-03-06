package me.anuar2k.engine.worldsystem;

import me.anuar2k.engine.entity.Entity;
import me.anuar2k.engine.property.AnimalProperty;
import me.anuar2k.engine.property.DirectionProperty;
import me.anuar2k.engine.property.EnergyProperty;
import me.anuar2k.engine.property.GenomeProperty;
import me.anuar2k.engine.util.Coord2D;
import me.anuar2k.engine.util.Genome;
import me.anuar2k.engine.util.RandSource;
import me.anuar2k.engine.worldmap.WorldMap;

import java.util.ArrayList;
import java.util.List;

public class AnimalMoveWorldSystem implements WorldSystem {
    private WorldMap worldMap = null;
    private final RandSource randSource;
    private final double moveEnergy;

    public AnimalMoveWorldSystem(RandSource randSource, double moveEnergy) {
        this.randSource = randSource;
        this.moveEnergy = moveEnergy;
    }

    @Override
    public void init(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

    @Override
    public void tick() {
        List<Entity> toMove = new ArrayList<>();

        for (int x = 0; x < this.worldMap.getWidth(); x++) {
            for (int y = 0; y < this.worldMap.getHeight(); y++) {
                this.worldMap.getEntities(new Coord2D(x, y), AnimalProperty.class).forEach(animal -> {
                    EnergyProperty energy = animal.getProperty(EnergyProperty.class);
                    energy.adjustEnergy(-this.moveEnergy);

                    if (energy.getEnergy() > 0) {
                        toMove.add(animal);
                    }
                    else {
                        energy.setEnergy(0);
                    }
                });
            }
        }

        for (Entity animal : toMove) {
            this.rotateAnimal(animal);
            this.moveAnimal(animal);
        }
    }

    private void rotateAnimal(Entity animal) {
        Genome genome = animal.getProperty(GenomeProperty.class).getGenome();
        DirectionProperty direction = animal.getProperty(DirectionProperty.class);

        int rotation = genome.getGenes()[Math.floorMod(this.randSource.next(), Genome.GENOME_LENGTH)];
        direction.setDirection(direction.getDirection().rotate(rotation));
    }

    private void moveAnimal(Entity animal) {
        DirectionProperty direction = animal.getProperty(DirectionProperty.class);
        animal.move(animal.getPosition().add(direction.getDirection().getCoordDelta()));
    }
}

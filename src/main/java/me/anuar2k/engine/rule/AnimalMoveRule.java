package me.anuar2k.engine.rule;

import me.anuar2k.engine.entity.Entity;
import me.anuar2k.engine.property.AnimalProperty;
import me.anuar2k.engine.property.DirectionProperty;
import me.anuar2k.engine.property.EnergyProperty;
import me.anuar2k.engine.property.GenomeProperty;
import me.anuar2k.engine.util.Coord2D;
import me.anuar2k.engine.util.RandSource;
import me.anuar2k.engine.worldmap.WorldMap;

import java.util.ArrayList;
import java.util.List;

public class AnimalMoveRule implements Rule {
    private final RandSource randSource;

    public AnimalMoveRule(RandSource randSource) {
        this.randSource = randSource;
    }

    @Override
    public void applyRule(WorldMap worldMap) {
        List<Entity> toMove = new ArrayList<>();

        for (int x = 0; x < worldMap.getWidth(); x++) {
            for (int y = 0; y < worldMap.getHeight(); y++) {
                for (Entity animal : worldMap.getEntities(new Coord2D(x, y), AnimalProperty.class)) {
                    EnergyProperty energy = animal.getProperty(EnergyProperty.class);
                    energy.setEnergy(energy.getEnergy() - 1);

                    if (energy.getEnergy() > 0) {
                        toMove.add(animal);
                    }
                }
            }
        }

        for (Entity animal : toMove) {
            this.rotateAnimal(animal);
            this.moveAnimal(animal);
        }
    }

    private void rotateAnimal(Entity animal) {
        GenomeProperty genome = animal.getProperty(GenomeProperty.class);
        DirectionProperty direction = animal.getProperty(DirectionProperty.class);

        int rotation = genome.getGenes()[Math.floorMod(this.randSource.next(), GenomeProperty.GENOME_LENGTH)];
        direction.setDirection(direction.getDirection().rotate(rotation));
    }

    private void moveAnimal(Entity animal) {
        DirectionProperty direction = animal.getProperty(DirectionProperty.class);
        animal.move(animal.getPosition().add(direction.getDirection().getCoordDelta()));
    }
}

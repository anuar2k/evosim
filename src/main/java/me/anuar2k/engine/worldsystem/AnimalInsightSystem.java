package me.anuar2k.engine.worldsystem;

import me.anuar2k.engine.entity.Entity;
import me.anuar2k.engine.property.AnimalProperty;
import me.anuar2k.engine.property.GenomeProperty;
import me.anuar2k.engine.util.Coord2D;
import me.anuar2k.engine.worldmap.WorldMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnimalInsightSystem implements WorldSystem {
    private WorldMap worldMap = null;

    @Override
    public void init(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

    @Override
    public void tick() {

    }

    public Entity getAnimalForCoord(Coord2D at) {
        return this.worldMap.getEntities(at.wrapAround(this.worldMap), AnimalProperty.class).findFirst().orElse(null);
    }

    public List<Entity> getAnimalsWithDominatingGenome(byte[] genes) {
        List<Entity> result = new ArrayList<>();

        for (int x = 0; x < this.worldMap.getWidth(); x++) {
            for (int y = 0; y < this.worldMap.getHeight(); y++) {
                this.worldMap.getEntities(new Coord2D(x, y), AnimalProperty.class)
                        .filter(animal -> Arrays.equals(animal.getProperty(GenomeProperty.class).getGenome().getGenes(), genes))
                        .forEach(animal -> result.add(animal));
            }
        }

        return result;
    }
}

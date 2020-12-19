package me.anuar2k.engine.system;

import me.anuar2k.engine.property.AnimalProperty;
import me.anuar2k.engine.property.EnergyProperty;
import me.anuar2k.engine.property.PlantProperty;
import me.anuar2k.engine.util.Coord2D;
import me.anuar2k.engine.worldmap.WorldMap;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AnimalFeedSystem implements System {
    private final WorldMap worldMap;

    public AnimalFeedSystem(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

    @Override
    public void init() {

    }

    @Override
    public void tick() {
        for (int x = 0; x < this.worldMap.getWidth(); x++) {
            for (int y = 0; y < this.worldMap.getHeight(); y++) {
                Coord2D currentCell = new Coord2D(x, y);

                this.worldMap.getEntities(currentCell, PlantProperty.class)
                    .findFirst()
                    .ifPresent(plant -> this.worldMap.getEntities(currentCell, AnimalProperty.class)
                        .map(animal -> animal.getProperty(EnergyProperty.class).getEnergy())
                        .max(Comparator.naturalOrder())
                        .ifPresent(maxAnimalEnergy -> {
                            List<EnergyProperty> props = this.worldMap.getEntities(currentCell, AnimalProperty.class)
                                .map(animal -> animal.getProperty(EnergyProperty.class))
                                .filter(prop -> prop.getEnergy() == maxAnimalEnergy)
                                .collect(Collectors.toList());

                            double plantEnergy = plant.getProperty(PlantProperty.class).getPlantEnergy();
                            double energyPerAnimal = plantEnergy / props.size();
                            for (EnergyProperty prop : props) {
                                prop.adjustEnergy(energyPerAnimal);
                            }

                            this.worldMap.removeEntity(plant);
                        })
                    );
            }
        }
    }
}

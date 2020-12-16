package me.anuar2k.engine.rule;

import me.anuar2k.engine.property.PlantProperty;
import me.anuar2k.engine.util.Coord2D;
import me.anuar2k.engine.worldmap.WorldMap;

public class AnimalFeedRule implements Rule {
    public AnimalFeedRule() {

    }

    @Override
    public void applyRule(WorldMap worldMap) {
        for (int x = 0; x < worldMap.getWidth(); x++) {
            for (int y = 0; y < worldMap.getHeight(); y++) {
                worldMap.getEntities(new Coord2D(x, y), PlantProperty.class)
                        .stream()
                        .findFirst()
                        .ifPresent(plant -> {
                            double energy = plant.getProperty(PlantProperty.class).getPlantEnergy();

                            /**
                             * rest of code
                             */
                        });
            }
        }
    }
}

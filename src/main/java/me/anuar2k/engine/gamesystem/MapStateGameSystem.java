package me.anuar2k.engine.gamesystem;

import javafx.scene.paint.Color;
import me.anuar2k.engine.entity.Entity;
import me.anuar2k.engine.property.AnimalProperty;
import me.anuar2k.engine.property.EnergyProperty;
import me.anuar2k.engine.property.JungleProperty;
import me.anuar2k.engine.property.PlantProperty;
import me.anuar2k.engine.util.Coord2D;
import me.anuar2k.engine.util.MapState;
import me.anuar2k.engine.util.Pair;
import me.anuar2k.engine.worldmap.WorldMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MapStateGameSystem implements GameSystem {
    private int epochNo = 0;
    private MapState mapState;
    private final HashMap<Entity, TrackedAnimalData> animalTracker = new HashMap<>();
    private final List<Integer> lifeLengths = new ArrayList<>();

    private final Color desertColor = new Color(250, 225, 135, 1);
    private final Color jungleColor = new Color(130, 201, 107, 1);
    private final Color plantColor = new Color(41, 138, 32, 1);
    private final Color[] animalColors = new Color[] {
        new Color(0, 0, 0, 1),
        new Color(51, 26, 0, 1),
        new Color(102, 51, 0, 1),
        new Color(153, 76, 0, 1),
        new Color(204, 102, 0, 1),
        new Color(255, 128, 0, 1)
    };

    public MapStateGameSystem() {

    }

    @Override
    public void init(WorldMap worldMap) {

    }

    @Override
    public void tick(WorldMap worldMap) {
        this.epochNo++;
    }

    private MapState processMap(WorldMap worldMap) {
        int animalCount = 0;
        double maxEnergy = 0;

        double totalEnergy = 0;
        double totalChildrenCount = 0;
        Color[][] cellColors = new Color[worldMap.getWidth()][worldMap.getHeight()];

        for (int x = 0; x < worldMap.getWidth(); x++) {
            for (int y = 0; y < worldMap.getHeight(); y++) {
                Pair<Integer, Color> prioritizedColor = new Pair<>(0, this.desertColor);

                for (Entity entity : worldMap.getEntities(new Coord2D(x, y)).collect(Collectors.toList())) {
                    if (entity.hasProperty(JungleProperty.class)) {
                        if (prioritizedColor.left < 1) {
                            prioritizedColor = new Pair<>(1, this.jungleColor);
                        }
                    }
                    else if (entity.hasProperty(PlantProperty.class)) {
                        if (prioritizedColor.left < 2) {
                            prioritizedColor = new Pair<>(2, this.plantColor);
                        }
                    }
                    else if (entity.hasProperty(AnimalProperty.class)) {
                        animalCount++;
                        if (this.animalTracker.containsKey(entity)) {
                            this.animalTracker.get(entity).stillAlive = true;
                        }
                        else {
                            this.animalTracker.put(entity, new TrackedAnimalData(this.epochNo));
                        }

                        double animalEnergy = entity.getProperty(EnergyProperty.class).getEnergy();
                        if (animalEnergy > maxEnergy) {
                            maxEnergy = animalEnergy;
                        }
                    }
                }

                cellColors[x][y] = prioritizedColor.right;
            }
        }

        List<Pair<Entity, TrackedAnimalData>> deadAnimals = new ArrayList<>();
        for (Map.Entry<Entity, TrackedAnimalData> entry : this.animalTracker.entrySet()) {
            TrackedAnimalData tad = entry.getValue();
            if (tad.stillAlive) {
                tad.stillAlive = false;
            }
            else {
                deadAnimals.add(new Pair<>(entry.getKey(), tad));
            }
        }

        for (Pair<Entity, TrackedAnimalData> deadAnimal : deadAnimals) {
            int lifeLength = this.epochNo - deadAnimal.right.bornAt;
            this.lifeLengths.add(lifeLength);
            this.animalTracker.remove(deadAnimal.left);
        }

        for (Entity animal : this.animalTracker.keySet()) {
            double animalEnergy = animal.getProperty(EnergyProperty.class).getEnergy();
            int colorIndex = Math.max(0, Math.min(this.animalColors.length - 1, (int)(animalEnergy / maxEnergy * this.animalColors.length)));
            cellColors[animal.getPosition().x][animal.getPosition().y] = this.animalColors[colorIndex];
        }

        double totalLifeLength = this.lifeLengths.stream().map(l -> (double)l).reduce((double) 0, (acc, val) -> acc + val);

        if (animalCount == 0) {
            return new MapState(this.epochNo,
                    cellColors,
                    animalCount,
                    null,
                    0,
                    0,
                    0);
        }
        else {
            return new MapState(this.epochNo,
                    cellColors,
                    animalCount,
                    null,
                    totalEnergy / animalCount,
                    totalLifeLength / animalCount,
                    totalChildrenCount / animalCount);
        }
    }

    private static class TrackedAnimalData {
        public final int bornAt;
        public boolean stillAlive;
        public TrackedAnimalData(int bornAt) {
            this.bornAt = bornAt;
            this.stillAlive = true;
        }
    }
}

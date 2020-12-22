package me.anuar2k.engine.worldsystem;

import javafx.scene.paint.Color;
import me.anuar2k.engine.entity.Entity;
import me.anuar2k.engine.property.*;
import me.anuar2k.engine.util.ByteArrayWrapper;
import me.anuar2k.engine.util.Coord2D;
import me.anuar2k.engine.util.MapState;
import me.anuar2k.engine.util.Pair;
import me.anuar2k.engine.worldmap.WorldMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MapStateWorldSystem implements WorldSystem {
    private static final Color desertColor = Color.rgb(250, 225, 135);
    private static final Color jungleColor = Color.rgb(130, 201, 107);
    private static final Color plantColor = Color.rgb(41, 138, 32);
    private static final Color[] animalColors = new Color[] {
            Color.rgb(0, 0, 0),
            Color.rgb(51, 26, 0),
            Color.rgb(102, 51, 0),
            Color.rgb(153, 76, 0),
            Color.rgb(204, 102, 0),
            Color.rgb(255, 128, 0)
    };

    private WorldMap worldMap;
    private int epochNo = 0;
    private MapState mapState = null;
    private final HashMap<Entity, TrackedAnimalData> animalTracker = new HashMap<>();
    private final List<Integer> lifeLengths = new ArrayList<>();

    @Override
    public void init(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

    @Override
    public void tick() {
        this.epochNo++;
        this.mapState = this.processMap();
    }

    public MapState getMapState() {
        return this.mapState;
    }

    private MapState processMap() {
        int animalCount = 0;
        double maxEnergy = 0;

        Color[][] cellColors = new Color[this.worldMap.getWidth()][this.worldMap.getHeight()];

        for (int x = 0; x < this.worldMap.getWidth(); x++) {
            for (int y = 0; y < this.worldMap.getHeight(); y++) {
                Pair<Integer, Color> prioritizedColor = new Pair<>(0, MapStateWorldSystem.desertColor);

                for (Entity entity : this.worldMap.getEntities(new Coord2D(x, y)).collect(Collectors.toList())) {
                    if (entity.hasProperty(JungleProperty.class)) {
                        if (prioritizedColor.left < 1) {
                            prioritizedColor = new Pair<>(1, MapStateWorldSystem.jungleColor);
                        }
                    }
                    else if (entity.hasProperty(PlantProperty.class)) {
                        if (prioritizedColor.left < 2) {
                            prioritizedColor = new Pair<>(2, MapStateWorldSystem.plantColor);
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

        double totalEnergy = 0;
        double totalChildrenCount = 0;
        byte[] dominantGenome = null;
        int dominantGenomeCount = 0;
        Map<ByteArrayWrapper, Integer> genomeCount = new HashMap<>();

        for (Entity animal : this.animalTracker.keySet()) {
            double animalEnergy = animal.getProperty(EnergyProperty.class).getEnergy();
            byte[] animalGenome = animal.getProperty(GenomeProperty.class).getGenome().getGenes();
            ByteArrayWrapper wrappedGenome = new ByteArrayWrapper(animalGenome);

            totalEnergy += animalEnergy;
            totalChildrenCount += animal.getProperty(ChildrenProperty.class).getChildren().size();

            int colorIndex = Math.max(0,
                                      Math.min(MapStateWorldSystem.animalColors.length - 1,
                                               (int)(animalEnergy / maxEnergy * MapStateWorldSystem.animalColors.length)));
            cellColors[animal.getPosition().x][animal.getPosition().y] = MapStateWorldSystem.animalColors[colorIndex];

            int currGenomeCount = genomeCount.getOrDefault(wrappedGenome, 0);
            currGenomeCount++;
            if (currGenomeCount > dominantGenomeCount) {
                dominantGenome = wrappedGenome.array;
                dominantGenomeCount = currGenomeCount;
            }
            genomeCount.put(wrappedGenome, currGenomeCount);
        }

        double totalLifeLength = this.lifeLengths.stream().map(l -> (double)l).reduce((double) 0, (acc, val) -> acc + val);

        if (animalCount == 0) {
            return new MapState(this.worldMap.getWidth(),
                    this.worldMap.getHeight(),
                    this.epochNo,
                    cellColors,
                    animalCount,
                    null,
                    0,
                    0,
                    0);
        }
        else {
            double averageEnergy = totalEnergy / animalCount;
            double averageLifeLength = this.lifeLengths.size() != 0 ? totalLifeLength / this.lifeLengths.size() : 0;
            double averageChildrenCount = totalChildrenCount / animalCount;
            return new MapState(this.worldMap.getWidth(),
                    this.worldMap.getHeight(),
                    this.epochNo,
                    cellColors,
                    animalCount,
                    dominantGenome,
                    averageEnergy,
                    averageLifeLength,
                    averageChildrenCount);
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

package me.anuar2k.engine.util;

import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapState {
    private transient final int width;
    private transient final int height;
    private transient final Color[][] cellColors;
    private transient final int epochNo;
    private final int animalCount;
    private final byte[] dominantGenome;
    private final double averageEnergy;
    private final double averageLifeLength;
    private final double averageChildrenCount;

    public MapState(int width,
                    int height,
                    int epochNo,
                    Color[][] cellColors,
                    int animalCount,
                    byte[] dominantGenome,
                    double averageEnergy,
                    double averageLifeLength,
                    double averageChildrenCount) {
        this.width = width;
        this.height = height;
        this.epochNo = epochNo;
        this.cellColors = cellColors;
        this.animalCount = animalCount;
        this.dominantGenome = dominantGenome;
        this.averageEnergy = averageEnergy;
        this.averageLifeLength = averageLifeLength;
        this.averageChildrenCount = averageChildrenCount;
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

    public int getEpochNo() {
        return this.epochNo;
    }

    public Color[][] getCellColors() {
        return this.cellColors;
    }

    public int getAnimalCount() {
        return this.animalCount;
    }

    public byte[] getDominantGenome() {
        return this.dominantGenome;
    }

    public double getAverageEnergy() {
        return this.averageEnergy;
    }

    public double getAverageLifeLength() {
        return this.averageLifeLength;
    }

    public double getAverageChildrenCount() {
        return this.averageChildrenCount;
    }

    public static MapState average(List<MapState> mapStates) {
        Map<ByteArrayWrapper, Integer> genomeCount = new HashMap<>();
        byte[] topGenome = null;
        int topGenomeCount = 0;

        int totalAnimalCount = 0;
        double totalAverageEnergy = 0;
        double totalAverageLifeLength = 0;
        double totalAverageChildrenCount = 0;

        for (MapState mapState : mapStates) {
            totalAnimalCount += mapState.getAnimalCount();
            totalAverageEnergy += mapState.getAverageEnergy();
            totalAverageLifeLength += mapState.getAverageLifeLength();
            totalAverageChildrenCount += mapState.getAverageChildrenCount();

            if (mapState.getDominantGenome() != null) {
                ByteArrayWrapper wrappedGenome = new ByteArrayWrapper(mapState.getDominantGenome());
                int currGenomeCount = genomeCount.getOrDefault(wrappedGenome, 0);
                currGenomeCount++;
                if (currGenomeCount > topGenomeCount) {
                    topGenome = wrappedGenome.array;
                    topGenomeCount = currGenomeCount;
                }
                genomeCount.put(wrappedGenome, currGenomeCount);
            }
        }

        return new MapState(0,
                0,
                0,
                null,
                totalAnimalCount / mapStates.size(),
                topGenome,
                totalAverageEnergy / mapStates.size(),
                totalAverageLifeLength / mapStates.size(),
                totalAverageChildrenCount / mapStates.size());
    }
}

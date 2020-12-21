package me.anuar2k.engine.util;

import javafx.scene.paint.Color;

public class MapState {
    private final int epochNo;
    private final Color[][] cellColors;
    private final int animalCount;
    private final int[] dominantGenome;
    private final double averageEnergy;
    private final double averageLifeLength;
    private final double averageChildrenCount;

    public MapState(int epochNo,
                    Color[][] cellColors,
                    int animalCount,
                    int[] dominantGenome,
                    double averageEnergy,
                    double averageLifeLength,
                    double averageChildrenCount) {
        this.epochNo = epochNo;
        this.cellColors = cellColors;
        this.animalCount = animalCount;
        this.dominantGenome = dominantGenome;
        this.averageEnergy = averageEnergy;
        this.averageLifeLength = averageLifeLength;
        this.averageChildrenCount = averageChildrenCount;
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

    public int[] getDominantGenome() {
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
}

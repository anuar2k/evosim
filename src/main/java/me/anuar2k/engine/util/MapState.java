package me.anuar2k.engine.util;

import javafx.scene.paint.Color;

public class MapState {
    private transient final int width;
    private transient final int height;
    private transient final Color[][] cellColors;
    private final int epochNo;
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
}

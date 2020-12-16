package me.anuar2k.engine.property;

public class PlantProperty implements Property {
    private final double plantEnergy;

    public PlantProperty(double plantEnergy) {
        this.plantEnergy = plantEnergy;
    }

    public double getPlantEnergy() {
        return this.plantEnergy;
    }
}

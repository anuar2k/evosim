package me.anuar2k.engine.property;

public class EnergyProperty implements Property {
    private double energy;

    public EnergyProperty(double startEnergy) {
        this.energy = startEnergy;
    }

    public double getEnergy() {
        return this.energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public void adjustEnergy(double delta) {
        this.energy += delta;
    }
}

package me.anuar2k.gui.config;

public class Config {
    private int mapWidth;
    private int mapHeight;
    private int jungleWidth;
    private int jungleHeight;
    private double startEnergy;
    private double moveEnergy;
    private double plantEnergy;
    private int startingAnimalCount;

    public Config() {

    }

    public int getMapWidth() {
        return this.mapWidth;
    }

    public void setMapWidth(int mapWidth) {
        this.mapWidth = mapWidth;
    }

    public int getMapHeight() {
        return this.mapHeight;
    }

    public void setMapHeight(int mapHeight) {
        this.mapHeight = mapHeight;
    }

    public int getJungleWidth() {
        return this.jungleWidth;
    }

    public void setJungleWidth(int jungleWidth) {
        this.jungleWidth = jungleWidth;
    }

    public int getJungleHeight() {
        return this.jungleHeight;
    }

    public void setJungleHeight(int jungleHeight) {
        this.jungleHeight = jungleHeight;
    }

    public double getStartEnergy() {
        return this.startEnergy;
    }

    public void setStartEnergy(double startEnergy) {
        this.startEnergy = startEnergy;
    }

    public double getMoveEnergy() {
        return this.moveEnergy;
    }

    public void setMoveEnergy(double moveEnergy) {
        this.moveEnergy = moveEnergy;
    }

    public double getPlantEnergy() {
        return this.plantEnergy;
    }

    public void setPlantEnergy(double plantEnergy) {
        this.plantEnergy = plantEnergy;
    }

    public int getStartingAnimalCount() {
        return this.startingAnimalCount;
    }

    public void setStartingAnimalCount(int startingAnimalCount) {
        this.startingAnimalCount = startingAnimalCount;
    }
}

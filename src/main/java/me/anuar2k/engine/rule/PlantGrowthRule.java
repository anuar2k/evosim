package me.anuar2k.engine.rule;

import me.anuar2k.engine.util.RandSource;
import me.anuar2k.engine.worldmap.WorldMap;

public class PlantGrowthRule implements Rule {
    RandSource randSource;
    private final double plantEnergy;
    private final double jungleRatio;

    public PlantGrowthRule(RandSource randSource, double plantEnergy, double jungleRatio) {
        this.randSource = randSource;
        this.plantEnergy = plantEnergy;
        this.jungleRatio = jungleRatio;
    }

    @Override
    public void applyRule(WorldMap worldMap) {

    }
}

package me.anuar2k.engine.rule;

import me.anuar2k.engine.util.RandSource;
import me.anuar2k.engine.worldmap.WorldMap;

public class AnimalBreedRule implements Rule {
    private RandSource randSource;
    private double minEnergy;

    public AnimalBreedRule(RandSource randSource, double minEnergy) {
        this.randSource = randSource;
        this.minEnergy = minEnergy;
    }

    @Override
    public void applyRule(WorldMap worldMap) {

    }
}

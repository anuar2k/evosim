package me.anuar2k.engine.simulation;

import me.anuar2k.engine.rule.*;
import me.anuar2k.engine.util.RandSource;
import me.anuar2k.engine.worldmap.SimpleWorldMap;
import me.anuar2k.engine.worldmap.WorldMap;

import java.util.List;

public class DefaultSimulation implements Simulation {
    private final WorldMap worldMap;
    private final List<Rule> rules;

    public DefaultSimulation(RandSource randSource,
                             int width,
                             int height,
                             double startEnergy,
                             double moveEnergy,
                             double plantEnergy,
                             double jungleRatio) {
        this.worldMap = new SimpleWorldMap(width, height);
        this.rules = List.of(new DeathRule(),
                             new AnimalMoveRule(randSource),
                             new AnimalFeedRule(),
                             new AnimalBreedRule(randSource, startEnergy / 2),
                             new PlantGrowthRule(randSource, plantEnergy, jungleRatio));
    }

    @Override
    public void tick() {
        for (Rule rule : this.rules) {
            rule.applyRule(this.worldMap);
        }
    }
}

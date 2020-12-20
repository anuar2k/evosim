package me.anuar2k.engine.rule;

import me.anuar2k.engine.worldmap.WorldMap;

public interface Rule {
    void init(WorldMap worldMap);
    void tick(WorldMap worldMap);
}

package me.anuar2k.engine.worldsystem;

import me.anuar2k.engine.worldmap.WorldMap;

public interface WorldSystem {
    void init(WorldMap worldMap);
    void tick(WorldMap worldMap);
}

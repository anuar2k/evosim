package me.anuar2k.engine.gamesystem;

import me.anuar2k.engine.worldmap.WorldMap;

public interface GameSystem {
    void init(WorldMap worldMap);
    void tick(WorldMap worldMap);
}

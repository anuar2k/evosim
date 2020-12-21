package me.anuar2k.engine.worldsystem;

import me.anuar2k.engine.entity.Entity;
import me.anuar2k.engine.property.JungleProperty;
import me.anuar2k.engine.property.PlantProperty;
import me.anuar2k.engine.util.Coord2D;
import me.anuar2k.engine.util.RandSource;
import me.anuar2k.engine.worldmap.WorldMap;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PlantGrowthWorldSystem implements WorldSystem {
    private final RandSource randSource;
    private final double plantEnergy;
    private final int jungleWidth;
    private final int jungleHeight;
    private final List<Coord2D> jungleCells = new ArrayList<>();
    private final List<Coord2D> nonJungleCells = new ArrayList<>();

    public PlantGrowthWorldSystem(RandSource randSource, double plantEnergy, int jungleWidth, int jungleHeight) {
        this.randSource = randSource;
        this.plantEnergy = plantEnergy;
        this.jungleWidth = jungleWidth;
        this.jungleHeight = jungleHeight;
    }

    @Override
    public void init(WorldMap worldMap) {
        if (this.jungleWidth > worldMap.getWidth() || this.jungleHeight > worldMap.getHeight()) {
            throw new IllegalArgumentException("Jungle can't be larger than map");
        }

        int jungleWidthStart = (worldMap.getWidth() - this.jungleWidth) / 2;
        int jungleWidthEnd = jungleWidthStart + this.jungleWidth;
        int jungleHeightStart = (worldMap.getHeight() - this.jungleHeight) / 2;
        int jungleHeightEnd = jungleWidthEnd + this.jungleHeight;

        for (int x = 0; x < worldMap.getWidth(); x++) {
            for (int y = 0; y < worldMap.getWidth(); y++) {
                Coord2D cell = new Coord2D(x, y);

                if (x >= jungleWidthStart && x < jungleWidthEnd && y >= jungleHeightStart && y < jungleHeightEnd) {
                    this.jungleCells.add(cell);
                    Entity jungle = new Entity(worldMap);
                    jungle.addProperty(new JungleProperty());
                }
                else {
                    this.nonJungleCells.add(cell);
                }
            }
        }
    }

    @Override
    public void tick(WorldMap worldMap) {
        List<Coord2D> emptyJungleCells = this.getEmptyCells(worldMap, this.jungleCells);
        List<Coord2D> emptyNonJungleCells = this.getEmptyCells(worldMap, this.nonJungleCells);

        this.spawnPlant(worldMap, emptyJungleCells);
        this.spawnPlant(worldMap, emptyNonJungleCells);

    }

    private void spawnPlant(WorldMap worldMap, List<Coord2D> emptyCells) {
        if (emptyCells.size() > 0) {
            int cellNo = Math.floorMod(this.randSource.next(), emptyCells.size());

            Entity plant = new Entity(worldMap);
            plant.addProperty(new PlantProperty(this.plantEnergy));

            plant.addToMap(emptyCells.get(cellNo));
        }
    }

    private List<Coord2D> getEmptyCells(WorldMap worldMap, List<Coord2D> cells) {
        return cells.stream().filter(cell -> worldMap.getEntities(cell, PlantProperty.class).findAny().isEmpty())
                .collect(Collectors.toList());
    }
}

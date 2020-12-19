package me.anuar2k.engine.system;

import me.anuar2k.engine.entity.Entity;
import me.anuar2k.engine.property.PlantProperty;
import me.anuar2k.engine.util.Coord2D;
import me.anuar2k.engine.util.RandSource;
import me.anuar2k.engine.worldmap.WorldMap;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PlantGrowthSystem implements System {
    private final WorldMap worldMap;
    private final RandSource randSource;
    private final double plantEnergy;
    private final int jungleWidth;
    private final int jungleHeight;
    private final List<Coord2D> jungleCells = new ArrayList<>();
    private final List<Coord2D> nonJungleCells = new ArrayList<>();

    public PlantGrowthSystem(WorldMap worldMap, RandSource randSource, double plantEnergy, int jungleWidth, int jungleHeight) {
        this.worldMap = worldMap;
        this.randSource = randSource;
        this.plantEnergy = plantEnergy;
        this.jungleWidth = jungleWidth;
        this.jungleHeight = jungleHeight;
    }

    @Override
    public void init() {
        if (this.jungleWidth > this.worldMap.getWidth() || this.jungleHeight > this.worldMap.getHeight()) {
            throw new IllegalArgumentException("Jungle can't be larger than map");
        }

        int jungleWidthStart = (this.worldMap.getWidth() - this.jungleWidth) / 2;
        int jungleWidthEnd = jungleWidthStart + this.jungleWidth;
        int jungleHeightStart = (this.worldMap.getHeight() - this.jungleHeight) / 2;
        int jungleHeightEnd = jungleWidthEnd + this.jungleHeight;

        for (int x = 0; x < this.worldMap.getWidth(); x++) {
            for (int y = 0; y < this.worldMap.getWidth(); y++) {
                Coord2D cell = new Coord2D(x, y);

                //TODO: add sprite property
                if (x >= jungleWidthStart && x < jungleWidthEnd && y >= jungleHeightStart && y < jungleHeightEnd) {
                    this.jungleCells.add(cell);
                }
                else {
                    this.nonJungleCells.add(cell);
                }
            }
        }
    }

    @Override
    public void tick() {
        List<Coord2D> emptyJungleCells = this.getCellsWithoutPlants(this.worldMap, this.jungleCells);
        List<Coord2D> emptyNonJungleCells = this.getCellsWithoutPlants(this.worldMap, this.nonJungleCells);

        this.spawnPlant(this.worldMap, emptyJungleCells);
        this.spawnPlant(this.worldMap, emptyNonJungleCells);

    }

    private void spawnPlant(WorldMap worldMap, List<Coord2D> emptyCells) {
        if (emptyCells.size() > 0) {
            int cellNo = Math.floorMod(this.randSource.next(), emptyCells.size());
            Entity plant = new Entity(worldMap, emptyCells.get(cellNo));
            plant.addProperty(new PlantProperty(this.plantEnergy));
            worldMap.addEntity(plant);
        }
    }

    private List<Coord2D> getCellsWithoutPlants(WorldMap worldMap, List<Coord2D> cells) {
        return cells.stream().filter(cell -> worldMap.getEntities(cell, PlantProperty.class).findFirst().isEmpty())
                .collect(Collectors.toList());
    }
}

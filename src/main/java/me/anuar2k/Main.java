package me.anuar2k;

import me.anuar2k.engine.simulation.DefaultSimulation;
import me.anuar2k.engine.simulation.Simulation;
import me.anuar2k.engine.util.RandomRandSource;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Simulation s = new DefaultSimulation(new RandomRandSource(),
                10,
                10,
                50,
                1,
                30,
                3,
                3);
        while (true) {
            s.tick();
            Thread.sleep(2000);
        }
    }
}

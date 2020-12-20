package me.anuar2k;

import me.anuar2k.engine.simulation.DefaultSimulation;
import me.anuar2k.engine.simulation.Simulation;
import me.anuar2k.engine.util.RandomRandSource;
import me.anuar2k.gui.Main;

import java.util.List;

public class MainProxy {
    public static void main(String[] args) {
        Main.main(args);
//        Simulation s = new DefaultSimulation(List.of(),
//                new RandomRandSource(),
//                10,
//                10,
//                50,
//                1,
//                30,
//                3,
//                3);
//        while (true) {
//            long time = System.currentTimeMillis();
//            s.tick();
//            System.out.println(System.currentTimeMillis() - time);
//            Thread.sleep(250);
//        }
    }
}

package me.anuar2k;

import me.anuar2k.engine.worldsystem.MapStateWorldSystem;
import me.anuar2k.engine.simulation.DefaultSimulation;
import me.anuar2k.engine.simulation.Simulation;
import me.anuar2k.engine.util.RandomRandSource;
import me.anuar2k.gui.Main;

import java.util.Arrays;
import java.util.List;

public class MainProxy {
    public static void main(String[] args) throws Exception {
        Main.main(args);
//        MapStateWorldSystem msgs = new MapStateWorldSystem();
//        Simulation s = new DefaultSimulation(List.of(msgs),
//                new RandomRandSource(),
//                5,
//                5,
//                20,
//                1,
//                20,
//                2,
//                2);
//        while (true) {
//            long time = System.currentTimeMillis();
//            s.tick();
//            System.out.println(System.currentTimeMillis() - time);
//            System.out.println(Arrays.toString(msgs.getMapState().getDominantGenome()));
//            System.out.println(msgs.getMapState().getAnimalCount());
//            System.out.println(msgs.getMapState().getAverageChildrenCount());
//            System.out.println(msgs.getMapState().getAverageLifeLength());
//            System.out.println(msgs.getMapState().getAverageEnergy());
//            System.out.println(msgs.getMapState().getEpochNo());
//            Thread.sleep(250);
//        }
    }
}

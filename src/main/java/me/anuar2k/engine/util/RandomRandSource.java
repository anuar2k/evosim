package me.anuar2k.engine.util;

import java.util.Random;

public class RandomRandSource implements RandSource {
    private final Random random = new Random();

    public RandomRandSource() {

    }

    @Override
    public int next() {
        return this.random.nextInt();
    }
}

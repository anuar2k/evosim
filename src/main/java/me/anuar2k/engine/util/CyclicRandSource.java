package me.anuar2k.engine.util;

import java.util.List;

public class CyclicRandSource implements RandSource {
    private final List<Integer> randList;
    private int nextIdx = 0;

    public CyclicRandSource(List<Integer> randList) {
        if (randList.size() == 0) {
            throw new IllegalArgumentException("randList must contain at least one integer");
        }
        this.randList = randList;
    }

    @Override
    public int next() {
        int result = this.randList.get(this.nextIdx);
        this.nextIdx = (this.nextIdx + 1) % this.randList.size();
        return result;
    }
}

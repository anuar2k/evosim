package me.anuar2k.engine.property;

import me.anuar2k.engine.util.Genome;

public class GenomeProperty implements Property {
    private final Genome genome;

    public GenomeProperty(Genome genome) {
        this.genome = genome;
    }

    public Genome getGenome() {
        return this.genome;
    }
}

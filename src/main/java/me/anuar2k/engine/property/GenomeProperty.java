package me.anuar2k.engine.property;

import me.anuar2k.engine.util.RandSource;

public class GenomeProperty implements Property {
    public static final int GENOME_LENGTH = 32;
    private final int[] genes;

    private GenomeProperty(int[] genes) {
        if (genes.length != GenomeProperty.GENOME_LENGTH) {
            throw new IllegalArgumentException("Genome must be 32 values long");
        }

        for (int gene : genes) {
            if (gene < 0 || gene > 7) {
                throw new IllegalArgumentException("Genes must have values in range [0, 7]");
            }
        }

        if (GenomeProperty.getMissingGene(genes) != null) {
            throw new IllegalArgumentException("All genes must be present at least once");
        }

        this.genes = genes;
    }

    public static GenomeProperty fromGenes(int[] genes) {
        return new GenomeProperty(genes);
    }

    public static GenomeProperty cross(RandSource randSource, GenomeProperty parent1, GenomeProperty parent2) {
        int div1 = GenomeProperty.chooseFromRange(0, GenomeProperty.GENOME_LENGTH - 2, randSource.next());
        int div2 = GenomeProperty.chooseFromRange(div1, GenomeProperty.GENOME_LENGTH - 1, randSource.next());

        int[] resultGenes = new int[GenomeProperty.GENOME_LENGTH];

        for (int i = 0; i < div2; i++) {
            resultGenes[i] = parent1.getGenes()[i];
        }

        for (int i = div2; i < GenomeProperty.GENOME_LENGTH; i++) {
            resultGenes[i] = parent2.getGenes()[i];
        }

        Integer missingGene = getMissingGene(resultGenes);
        int forcedGeneIndex = 0;

        while (missingGene != null) {
            resultGenes[forcedGeneIndex] = missingGene;
            forcedGeneIndex++;
            missingGene = getMissingGene(resultGenes);
        }

        return new GenomeProperty(resultGenes);
    }

    public int[] getGenes() {
        return this.genes;
    }

    private static Integer getMissingGene(int[] genes) {
        boolean[] genePresent = new boolean[8];

        for (int gene : genes) {
            genePresent[gene] = true;
        }

        for (int i = 0; i < 8; i++) {
            if (!genePresent[i]) {
                return i;
            }
        }

        return null;
    }

    private static int chooseFromRange(int from, int to, int chooseFactor) {
        if (to <= from) {
            throw new IllegalArgumentException("to must be greater than from");
        }

        int offset = Math.floorMod(chooseFactor, to - from);
        return from + offset;
    }
}

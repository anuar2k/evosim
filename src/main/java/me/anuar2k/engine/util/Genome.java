package me.anuar2k.engine.util;

public class Genome {
    public static final int GENOME_LENGTH = 32;
    private final byte[] genes;

    private Genome(byte[] genes) {
        if (genes.length != Genome.GENOME_LENGTH) {
            throw new IllegalArgumentException("Genome must be 32 values long");
        }

        for (byte gene : genes) {
            if (gene < 0 || gene > 7) {
                throw new IllegalArgumentException("Genes must have values in range [0, 7]");
            }
        }

        if (Genome.getMissingGene(genes) != null) {
            throw new IllegalArgumentException("All genes must be present at least once");
        }

        this.genes = genes;
    }

    public static Genome fromGenes(byte[] genes) {
        return new Genome(genes);
    }

    public static Genome random(RandSource randSource) {
        byte[] genes = new byte[Genome.GENOME_LENGTH];
        for (int i = 0; i < Genome.GENOME_LENGTH; i++) {
            genes[i] = (byte)Math.floorMod(randSource.next(), 8);
        }

        Genome.fixGenes(genes);

        return new Genome(genes);
    }

    public static Genome cross(RandSource randSource, Genome parent1, Genome parent2) {
        int div1 = Genome.chooseFromRange(0, Genome.GENOME_LENGTH - 2, randSource.next());
        int div2 = Genome.chooseFromRange(div1, Genome.GENOME_LENGTH - 1, randSource.next());

        byte[] resultGenes = new byte[Genome.GENOME_LENGTH];

        for (int i = 0; i < div2; i++) {
            resultGenes[i] = parent1.getGenes()[i];
        }

        for (int i = div2; i < Genome.GENOME_LENGTH; i++) {
            resultGenes[i] = parent2.getGenes()[i];
        }

        Genome.fixGenes(resultGenes);

        return new Genome(resultGenes);
    }

    public byte[] getGenes() {
        return this.genes;
    }

    private static void fixGenes(byte[] genes) {
        Byte missingGene = getMissingGene(genes);
        int forcedGeneIndex = 0;

        while (missingGene != null) {
            genes[forcedGeneIndex] = missingGene;
            forcedGeneIndex++;
            missingGene = getMissingGene(genes);
        }
    }

    private static Byte getMissingGene(byte[] genes) {
        boolean[] genePresent = new boolean[8];

        for (byte gene : genes) {
            genePresent[gene] = true;
        }

        for (byte i = 0; i < 8; i++) {
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


package pacman.controllers.examples.po.NN.NEAT;

import org.junit.jupiter.api.Test;
import pacman.controllers.examples.po.NN.NEAT.Frame.NetworkFrame;
import pacman.controllers.examples.po.NN.NEAT.Frame.NetworkPanel;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class GenomeTest {

    @Test
    void compareTest() {
        ArrayList<Genome> genomes = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Genome g = new Genome();
            g.setScore((int)(Math.random() * 100));
            genomes.add(g);
        }
        Collections.sort(genomes);

        boolean isSorted = true;
        for (int i = 0; i < genomes.size(); i++) {
            try {
                Genome current = genomes.get(i);
                Genome next = genomes.get(i + 1);

                if (current.getScore() > next.getScore()) {
                    isSorted = false;
                }
            }
            catch (Exception e) {
                if (!(e.getClass().getCanonicalName() == "java.lang.IndexOutOfBoundsException")) {
                    e.printStackTrace();
                }
            }
        }

        assertEquals(true, isSorted);
    }

    @Test
    void crossoverTest() {
        Neat neat = new Neat(3, 2, 100);
        Genome g1 = neat.emptyGenome();
        g1.mutate(10);
        Genome g2 = g1.deepCopy();
        g2.mutate(10);

        Genome child = Genome.crossover(g1, g2);

        NetworkFrame frame1 = new NetworkFrame(new NetworkPanel(g1));
        NetworkFrame frame2 = new NetworkFrame(new NetworkPanel(g2));
        NetworkFrame frame3 = new NetworkFrame(new NetworkPanel(child));
        frame1.update();
        frame2.update();
        frame3.update();
        frame1.setTitle("Parent 1");
        frame2.setTitle("Parent 2");
        frame3.setTitle("Child");

        while(true) {
            int x = 0;
        }
    }

}
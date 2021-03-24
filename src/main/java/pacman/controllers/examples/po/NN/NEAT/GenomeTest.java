package pacman.controllers.examples.po.NN.NEAT;

import org.junit.jupiter.api.Test;
import pacman.controllers.examples.po.NN.NEAT.Calculations.Connection;
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

                if (current.getScore() < next.getScore()) {
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
    void crossoverTest() throws InterruptedException {
        Neat neat = new Neat(3, 2, 100);
        Genome g1 = neat.emptyGenome(0);
        g1.mutate(20);
        Genome g2 = g1.deepCopy();
        g2.mutate(2);

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

        //Thread.sleep(1000000);
    }

    @Test
    void deepCopyNodeTest() {
        NodeGene n1 = new NodeGene(10);
        NodeGene n2 = new NodeGene(15);
        NodeGene n1_deep = n1.deepCopy();
        NodeGene n2_deep = n2.deepCopy();

        assertEquals(n1.innovationNumber, n1_deep.innovationNumber);
        assertEquals(n2.innovationNumber, n2_deep.innovationNumber);
    }

    @Test
    void deepCopyConnectionTest() {
        ConnectionGene c1 = new ConnectionGene(new NodeGene(1), new NodeGene(2));
        ConnectionGene c2 = new ConnectionGene(new NodeGene(3), new NodeGene(4));
        ConnectionGene c1_deep = c1.deepCopy();
        ConnectionGene c2_deep = c2.deepCopy();

        assertEquals(c1.innovationNumber, c1_deep.innovationNumber);
        assertEquals(c2.innovationNumber, c2_deep.innovationNumber);
        assertEquals(c1.getFromNode().innovationNumber, c1_deep.getFromNode().innovationNumber);
        assertEquals(c1.getToNode().innovationNumber, c1_deep.getToNode().innovationNumber);
        assertEquals(c2.getFromNode().innovationNumber, c2_deep.getFromNode().innovationNumber);
        assertEquals(c2.getToNode().innovationNumber, c2_deep.getToNode().innovationNumber);
    }
}
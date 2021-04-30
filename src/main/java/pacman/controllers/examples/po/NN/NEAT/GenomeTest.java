package pacman.controllers.examples.po.NN.NEAT;

import org.junit.jupiter.api.Test;
import pacman.controllers.examples.po.NN.NEAT.Frame.NetworkFrame;
import pacman.controllers.examples.po.NN.NEAT.Frame.NetworkPanel;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.Bidi;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class GenomeTest {

    public MathContext mc = new MathContext(16);

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

    @Test
    void triangleSprawlTest() {
        Genome genome = new Genome();

        NodeGene inputNode = new NodeGene(0, 0.0, 0.5);
        NodeGene o1 = new NodeGene(1, 1.0, 1.0);
        NodeGene o2 = new NodeGene(2, 1.0, 0.0);
        ConnectionGene c1 = new ConnectionGene(inputNode, o1);
        ConnectionGene c2 = new ConnectionGene(inputNode, o2);

        ArrayList<ConnectionGene> cons = new ArrayList<>();
        cons.add(c1);
        cons.add(c2);

        genome.setConnections(cons);

        BigDecimal sprawl = genome.calculateSprawlOfNodes(inputNode, o1, o2);

        assertEquals(new BigDecimal(0.9272952180016121, mc), sprawl);
    }

    @Test
    void triangleSprawlTest2() {
        Genome genome = new Genome();

        NodeGene inputNode = new NodeGene(0, 0.0, 0.5);
        NodeGene o1 = new NodeGene(1, 0.5, 1.0);
        NodeGene o2 = new NodeGene(2, 0.5, 0.0);
        ConnectionGene c1 = new ConnectionGene(inputNode, o1);
        ConnectionGene c2 = new ConnectionGene(inputNode, o2);

        ArrayList<ConnectionGene> cons = new ArrayList<>();
        cons.add(c1);
        cons.add(c2);

        genome.setConnections(cons);

        BigDecimal sprawl = genome.calculateSprawlOfNodes(inputNode, o1, o2);

        assertEquals(new BigDecimal(1.570796326794896, mc), sprawl);
    }

    @Test
    void inputLayerSprawl() {
        Genome genome = new Genome();

        /* Input layer */
        NodeGene n1 = new NodeGene(0, 0.1, 0.8);
        NodeGene n2 = new NodeGene(1, 0.1, 0.5);
        NodeGene n3 = new NodeGene(2, 0.1, 0.7);

        /* Hidden Layer */
        NodeGene h1 = new NodeGene(3, 0.2, 0.2);
        NodeGene h2 = new NodeGene(4, 0.3, 0.8);
        NodeGene h3 = new NodeGene(5, 0.5, 0.5);

        /* Output Layer */
        NodeGene o1 = new NodeGene(6, 0.9, 0.2);
        NodeGene o2 = new NodeGene(7, 0.9, 0.8);

        ArrayList<NodeGene> nodes = new ArrayList<>();
        nodes.add(n1); nodes.add(n2); nodes.add(n3);
        nodes.add(h1); nodes.add(h2); nodes.add(h3);
        nodes.add(o1); nodes.add(o2);
        genome.setNodes(nodes);

        // Create connections
        ConnectionGene con1 = new ConnectionGene(n1, h1);
        ConnectionGene con2 = new ConnectionGene(n2, h2);
        //ConnectionGene con3 = new ConnectionGene(n3, h3);
        ConnectionGene con4 = new ConnectionGene(h1, o1);
        ConnectionGene con5 = new ConnectionGene(h2, o1);
        ConnectionGene con6 = new ConnectionGene(h3, o2);
        ConnectionGene con7 = new ConnectionGene(n1, h2);
        //ConnectionGene con8 = new ConnectionGene(n2, o1);
        //ConnectionGene con9 = new ConnectionGene(n3, h2);

        ArrayList<ConnectionGene> cons = new ArrayList<>();
        cons.add(con1); cons.add(con2); //cons.add(con3);
        cons.add(con4); cons.add(con5); cons.add(con6);
        cons.add(con7); //cons.add(con8); //cons.add(con9);
        genome.setConnections(cons);

        NetworkFrame frame1 = new NetworkFrame(new NetworkPanel(genome));
        frame1.update();

        BigDecimal sprawl = genome.calculateInputLayerSprawl();

//        try {
//            Thread.sleep(1000000);
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }

        assertEquals(1.40564764938027, sprawl.doubleValue());
    }

    @Test
    void hiddenLayerSprawl() {
        Genome genome = new Genome();

        /* Input layer */
        NodeGene n1 = new NodeGene(0, 0.1, 0.8);
        NodeGene n2 = new NodeGene(1, 0.1, 0.5);
        NodeGene n3 = new NodeGene(2, 0.1, 0.7);

        /* Hidden Layer */
        NodeGene h1 = new NodeGene(3, 0.2, 0.2);
        NodeGene h2 = new NodeGene(4, 0.3, 0.8);
        NodeGene h3 = new NodeGene(5, 0.5, 0.5);

        /* Output Layer */
        NodeGene o1 = new NodeGene(6, 0.9, 0.2);
        NodeGene o2 = new NodeGene(7, 0.9, 0.8);

        ArrayList<NodeGene> nodes = new ArrayList<>();
        nodes.add(n1); nodes.add(n2); nodes.add(n3);
        nodes.add(h1); nodes.add(h2); nodes.add(h3);
        nodes.add(o1); nodes.add(o2);
        genome.setNodes(nodes);

        // Create connections
        ConnectionGene con1 = new ConnectionGene(n1, h1);
        ConnectionGene con2 = new ConnectionGene(n2, h2);
        //ConnectionGene con3 = new ConnectionGene(n3, h3);
        ConnectionGene con4 = new ConnectionGene(h1, o1);
        ConnectionGene con5 = new ConnectionGene(h2, o1);
        ConnectionGene con6 = new ConnectionGene(h3, o2);
        ConnectionGene con7 = new ConnectionGene(n1, h2);
        ConnectionGene con8 = new ConnectionGene(h1, h3);
        //ConnectionGene con9 = new ConnectionGene(n3, h2);

        ArrayList<ConnectionGene> cons = new ArrayList<>();
        cons.add(con1); cons.add(con2); //cons.add(con3);
        cons.add(con4); cons.add(con5); cons.add(con6);
        cons.add(con7); cons.add(con8); //cons.add(con9);
        genome.setConnections(cons);

        NetworkFrame frame1 = new NetworkFrame(new NetworkPanel(genome));
        frame1.update();

//        try {
//            Thread.sleep(1000000);
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }

        BigDecimal sprawl = genome.calculateHiddenLayerSprawl();

        assertEquals(0.7853981633974486, sprawl.doubleValue());
    }

    @Test
    void hiddenLayerSprawl2() {
        Genome genome = new Genome();

        /* Input layer */
        NodeGene n1 = new NodeGene(0, 0.1, 0.8);
        NodeGene n2 = new NodeGene(1, 0.1, 0.5);
        NodeGene n3 = new NodeGene(2, 0.1, 0.7);

        /* Hidden Layer */
        NodeGene h1 = new NodeGene(3, 0.2, 0.2);
        NodeGene h2 = new NodeGene(4, 0.3, 0.8);
        NodeGene h3 = new NodeGene(5, 0.5, 0.5);

        /* Output Layer */
        NodeGene o1 = new NodeGene(6, 0.9, 0.2);
        NodeGene o2 = new NodeGene(7, 0.9, 0.8);

        ArrayList<NodeGene> nodes = new ArrayList<>();
        nodes.add(n1); nodes.add(n2); nodes.add(n3);
        nodes.add(h1); nodes.add(h2); nodes.add(h3);
        nodes.add(o1); nodes.add(o2);
        genome.setNodes(nodes);

        // Create connections
        ConnectionGene con1 = new ConnectionGene(n1, h1);
        ConnectionGene con2 = new ConnectionGene(n2, h2);
        //ConnectionGene con3 = new ConnectionGene(n3, h3);
        ConnectionGene con4 = new ConnectionGene(h1, o1);
        ConnectionGene con5 = new ConnectionGene(h2, o1);
        ConnectionGene con6 = new ConnectionGene(h3, o2);
        ConnectionGene con7 = new ConnectionGene(n1, h2);
        ConnectionGene con8 = new ConnectionGene(h1, h3);
        ConnectionGene con9 = new ConnectionGene(h2, o2);

        ArrayList<ConnectionGene> cons = new ArrayList<>();
        cons.add(con1); cons.add(con2); //cons.add(con3);
        cons.add(con4); cons.add(con5); cons.add(con6);
        cons.add(con7); cons.add(con8); cons.add(con9);
        genome.setConnections(cons);

        NetworkFrame frame1 = new NetworkFrame(new NetworkPanel(genome));
        frame1.update();

//        try {
//            Thread.sleep(1000000);
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }

        BigDecimal sprawl = genome.calculateHiddenLayerSprawl();

        assertEquals(1.570796326794897, sprawl.doubleValue());
    }

    @Test
    void sprawlWithDisabledConnections() {
        Genome genome = new Genome();

        /* Input layer */
        NodeGene n1 = new NodeGene(0, 0.1, 0.8);
        NodeGene n2 = new NodeGene(1, 0.1, 0.5);
        NodeGene n3 = new NodeGene(2, 0.1, 0.7);

        /* Hidden Layer */
        NodeGene h1 = new NodeGene(3, 0.2, 0.2);
        NodeGene h2 = new NodeGene(4, 0.3, 0.8);
        NodeGene h3 = new NodeGene(5, 0.5, 0.5);

        /* Output Layer */
        NodeGene o1 = new NodeGene(6, 0.9, 0.2);
        NodeGene o2 = new NodeGene(7, 0.9, 0.8);
        NodeGene o3 = new NodeGene(8, 0.9, 0.7);

        ArrayList<NodeGene> nodes = new ArrayList<>();
        nodes.add(n1); nodes.add(n2); nodes.add(n3);
        nodes.add(h1); nodes.add(h2); nodes.add(h3);
        nodes.add(o1); nodes.add(o2); nodes.add(o3);
        genome.setNodes(nodes);

        // Create connections
        ConnectionGene con1 = new ConnectionGene(n1, h1);
        ConnectionGene con2 = new ConnectionGene(n2, h2);
        ConnectionGene con3 = new ConnectionGene(n1, h3);
        ConnectionGene con4 = new ConnectionGene(h1, o1);
        ConnectionGene con5 = new ConnectionGene(h2, o1);
        ConnectionGene con6 = new ConnectionGene(h3, o2);
        ConnectionGene con7 = new ConnectionGene(n1, h2);
        ConnectionGene con8 = new ConnectionGene(h1, h3);
        ConnectionGene con9 = new ConnectionGene(h2, o2);
        ConnectionGene con10 = new ConnectionGene(h2, o3);
        ConnectionGene con11 = new ConnectionGene(h3, o1);

        con1.setEnabled(false);
        con9.setEnabled(false);

        ArrayList<ConnectionGene> cons = new ArrayList<>();
        cons.add(con1); cons.add(con2); cons.add(con3);
        cons.add(con4); cons.add(con5); cons.add(con6);
        cons.add(con7); cons.add(con8); cons.add(con9);
        cons.add(con10); cons.add(con11);
        genome.setConnections(cons);

        NetworkFrame frame1 = new NetworkFrame(new NetworkPanel(genome));
        frame1.update();

//        try {
//            Thread.sleep(1000000);
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }

        BigDecimal inputSprawl = genome.calculateInputLayerSprawl();
        BigDecimal hiddenSprawl = genome.calculateHiddenLayerSprawl();

        // Check input layer sprawl
        assertEquals(0.6435011087932847, inputSprawl.doubleValue());

        // Check hidden layer sprawl
        assertEquals(2.692649866966839, hiddenSprawl.doubleValue());
    }

    @Test
    void calculateReachOfNode() {
        NodeGene n1 = new NodeGene(0, 0.0, 0.0);
        NodeGene n2 = new NodeGene(1, 1.0, 0.0);
        NodeGene n3 = new NodeGene(2, 1.0, 1.0);
        ArrayList<NodeGene> nodes = new ArrayList<>();
        nodes.add(n1); nodes.add(n2); nodes.add(n3);

        ConnectionGene con1 = new ConnectionGene(n1, n2);
        ConnectionGene con2 = new ConnectionGene(n1, n3);
        ArrayList<ConnectionGene> cons = new ArrayList<>();
        cons.add(con1); cons.add(con2);

        Genome genome = new Genome();
        genome.setNodes(nodes); genome.setConnections(cons);

        assertEquals(1 + Math.sqrt(2.0), genome.calculateInputLayerReach().doubleValue());
    }

    @Test
    void calculateReachOfTwoNodes() {
        NodeGene n1 = new NodeGene(0, 0.0, 0.0);
        NodeGene n2 = new NodeGene(1, 1.0, 0.0);
        NodeGene n3 = new NodeGene(2, 1.0, 1.0);
        NodeGene n4 = new NodeGene(3, 0.0, 1.0);
        ArrayList<NodeGene> nodes = new ArrayList<>();
        nodes.add(n1); nodes.add(n2); nodes.add(n3); nodes.add(n4);

        ConnectionGene con1 = new ConnectionGene(n1, n2); // 1
        ConnectionGene con2 = new ConnectionGene(n1, n3); // sqrt 2
        ConnectionGene con3 = new ConnectionGene(n4, n2); // 1
        ConnectionGene con4 = new ConnectionGene(n4, n3); // sqrt 2
        ArrayList<ConnectionGene> cons = new ArrayList<>();
        cons.add(con1); cons.add(con2); cons.add(con3); cons.add(con4);

        Genome genome = new Genome();
        genome.setNodes(nodes); genome.setConnections(cons);

        assertEquals(1 + Math.sqrt(2.0) + 1 + Math.sqrt(2), genome.calculateInputLayerReach().doubleValue());
    }

    @Test
    void fullReachTest() {
        NodeGene n1 = new NodeGene(0, 0.0, 0.0);
        NodeGene n2 = new NodeGene(1, 1.0, 0.0);
        NodeGene n3 = new NodeGene(2, 1.0, 1.0);
        NodeGene n4 = new NodeGene(3, 0.0, 1.0);
        NodeGene n5 = new NodeGene(4, 0.5, 0.5);
        NodeGene n6 = new NodeGene(5, 0.3, 0.3);
        ArrayList<NodeGene> nodes = new ArrayList<>();
        nodes.add(n1); nodes.add(n2); nodes.add(n3); nodes.add(n4);
        nodes.add(n5); nodes.add(n6);

        ConnectionGene con1 = new ConnectionGene(n1, n2); // 1
        ConnectionGene con2 = new ConnectionGene(n1, n3); // sqrt 2
        ConnectionGene con3 = new ConnectionGene(n4, n2); // 1
        ConnectionGene con4 = new ConnectionGene(n4, n3); // sqrt 2
        ConnectionGene con5 = new ConnectionGene(n1, n5); // sqrt(0.5^2 + 0.5^2)
        ConnectionGene con6 = new ConnectionGene(n5, n2); // sqrt(0.5^2 + 0.5^2)
        ConnectionGene con7 = new ConnectionGene(n4, n6); // sqrt(0.3^2 + 0.7^2)
        ConnectionGene con8 = new ConnectionGene(n6, n3); // sqrt(0.7^2 + 0.7^2)
        ArrayList<ConnectionGene> cons = new ArrayList<>();
        cons.add(con1); cons.add(con2); cons.add(con3); cons.add(con4);
        cons.add(con5); cons.add(con6); cons.add(con7); cons.add(con8);

        Genome genome = new Genome();
        genome.setNodes(nodes); genome.setConnections(cons);

        assertEquals(6.297111216519128,
                genome.calculateInputLayerReach().doubleValue());

        assertEquals(Math.sqrt(Math.pow(0.5, 2) + Math.pow(0.5, 2))
                + Math.sqrt(Math.pow(0.7, 2) + Math.pow(0.7, 2)),
                genome.calculateHiddenLayerReach().doubleValue());

        con8.setEnabled(false);

        assertEquals(Math.sqrt(Math.pow(0.5, 2) + Math.pow(0.5, 2)),
                genome.calculateHiddenLayerReach().doubleValue());

        con1.setEnabled(false);

        assertEquals(6.297111216519129 - 1,
                genome.calculateInputLayerReach().doubleValue());
    }

    @Test
    void vectorSumTest() {
        NodeGene n1 = new NodeGene(0, 0.0, 0.0);
        NodeGene n2 = new NodeGene(1, 0.5, 0.0);
        NodeGene n3 = new NodeGene(2, 0.0, 0.5);
        NodeGene n4 = new NodeGene(3, 1.0, 1.0);
        NodeGene n5 = new NodeGene(4, 1.0, 0.5);
        ArrayList<NodeGene> nodes = new ArrayList<>();
        nodes.add(n1); nodes.add(n2);
        nodes.add(n3);
        nodes.add(n4);
        nodes.add(n5);

        ConnectionGene con1 = new ConnectionGene(n1, n2);
        ConnectionGene con2 = new ConnectionGene(n3, n2);
        ConnectionGene con3 = new ConnectionGene(n2, n4);
        ConnectionGene con4 = new ConnectionGene(n2, n5);
//        ConnectionGene con5 = new ConnectionGene(n1, n5);
//        ConnectionGene con6 = new ConnectionGene(n5, n2);
//        ConnectionGene con7 = new ConnectionGene(n4, n6);
//        ConnectionGene con8 = new ConnectionGene(n6, n3);
        ArrayList<ConnectionGene> cons = new ArrayList<>();
        cons.add(con1);
        cons.add(con2);
        cons.add(con3);
        cons.add(con4);
//        cons.add(con5); cons.add(con6); cons.add(con7); cons.add(con8);

        Genome genome = new Genome();
        genome.setNodes(nodes); genome.setConnections(cons);

        double inputWeight = con1.getWeight() + con2.getWeight();
        double hiddenWeight = con3.getWeight() + con4.getWeight();

        assertEquals(inputWeight, genome.calculateInputVectorSum());

        assertEquals(hiddenWeight, genome.calculateHiddenVectorSum());
    }
}
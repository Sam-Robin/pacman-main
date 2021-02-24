package pacman.controllers.examples.po.NN.NEAT;

import pacman.controllers.examples.po.NN.NEAT.Calculations.Calculator;

import java.util.ArrayList;
import java.util.Random;

public class Genome {

    private ArrayList<ConnectionGene> connections;
    private ArrayList<NodeGene> nodes;
    private Neat neat;
    private Calculator calculator;

    public Genome() {
        connections = new ArrayList<>();
        nodes = new ArrayList<>();
        neat = new Neat();
        this.calculator = new Calculator(this);
    }

    public Genome(Neat neat) {
        this.neat = neat;
        connections = new ArrayList<>();
        nodes = new ArrayList<>();
        this.calculator = new Calculator(this);
    }

    /**
     * Runs one set of data through the network
     * @param data
     */
    public double[] calculate(double... data) throws Exception {
        if (calculator != null) {
            return calculator.calculate(data);
        }

        return null;
    }

    public void update() {
        this.calculator = new Calculator(this);
    }

    /**
     * Mutate a network's topology by adding a connection, adding a node or changing a neuron's weights
     */
    public void mutate() {
        // Mutate a link
        if (neat.getPROBABILITY_MUTATE_LINK() > Math.random()) {
            mutate_link();
        }
        // Mutate a node
        if (neat.getPROBABILITY_MUTATE_NODE() > Math.random()) {
            mutate_node();
        }
        // Mutate a node's isEnabled status
        if (neat.getPROBABILITY_MUTATE_TOGGLE_LINK() > Math.random()) {
            mutate_link_toggle();
        }
        // Mutate a weight on a connection by a random amount
        if (neat.getPROBABILITY_MUTATE_WEIGHT_RANDOM() > Math.random()) {
            mutate_weight_random();
        }
        // Mutate a weight on a connection by a shift
        if (neat.getPROBABILITY_MUTATE_WEIGHT_SHIFT() > Math.random()) {
            mutate_weight_shift();
        }
    }

    public void mutate_link() {
        for (int i = 0; i < 100; i++) {
            NodeGene a = getRandomNodeGene();
            NodeGene b = getRandomNodeGene();

            // Do not allow a node to connect to itself
            if (a.getX() == b.getX()) {
                continue;
            }

            ConnectionGene con;

            if (a.getX() < b.getX()) {
                con = new ConnectionGene(a, b);
            }
            else {
                con = new ConnectionGene(b, a);
            }

            // If this connection already exists...
            if (connections.contains(con)) {
                // Disregard it
                continue;
            }

            con = neat.getConnection(con.getFromNode(), con.getToNode());
            con.setWeight((Math.random() * 2 - 1) * neat.getWEIGHT_RANDOM_STRENGTH());

            connections.add(con);
            return;
        }
    }

    public void mutate_node() {
        ConnectionGene con = getRandomConnectionGene();

        if (con == null) {
            return;
        }

        NodeGene fromNode = con.getFromNode();
        NodeGene toNode = con.getToNode();

        NodeGene middle = neat.getNode();
        middle.setX((fromNode.getX() + toNode.getX()) / 2);
        middle.setY((fromNode.getY() + toNode.getY()) / 2 + Math.random() * 0.1 - 0.05);

        // Create connection from fromNode to middle
        ConnectionGene con1 = neat.getConnection(fromNode, middle);

        // Create connection from middle to toNode
        ConnectionGene con2 = neat.getConnection(middle, toNode);

        con1.setWeight(1);
        con2.setWeight(con.getWeight());
        con2.setEnabled(con.isEnabled());

        connections.remove(con);
        connections.add(con1);
        connections.add(con2);

        nodes.add(middle);
    }

    public void mutate_weight_shift() {
        ConnectionGene con = getRandomConnectionGene();

        if (con != null) {
            con.setWeight(con.getWeight() + (Math.random() * 2 - 1) * neat.getWEIGHT_SHIFT_STRENGTH());
        }
    }

    public void mutate_weight_random() {
        ConnectionGene con = getRandomConnectionGene();

        if (con != null) {
            con.setWeight((Math.random() * 2 - 1) * neat.getWEIGHT_RANDOM_STRENGTH());
        }
    }

    public void mutate_link_toggle() {
        ConnectionGene con = getRandomConnectionGene();

        if (con != null) {
            con.setEnabled(!con.isEnabled());
        }
    }

    /**
     * Gets a random ConnectionGene in the connections list
     * @return
     */
    private ConnectionGene getRandomConnectionGene() {
        Random r = new Random();
        int min = 0;
        int max = connections.size() - 1;
        int index = r.nextInt(max - min) + min;
        return  connections.get(index);
    }

    private NodeGene getRandomNodeGene() {
        Random r = new Random();
        int min = 0;
        int max = nodes.size() - 1;
        int index = r.nextInt(max - min) + min;
        return  nodes.get(index);
    }

    /**
     * Create a new genome by combining two together
     * @param g1   fittest parent
     * @param g2   less fit parent
     * @return
     */
    public static Genome crossover(Genome g1, Genome g2) {
        Neat neat = g1.getNeat();
        Genome child = g1.getNeat().emptyGenome();
        int i_g1 = 0;
        int i_g2 = 0;

        // Iterate through both the first and second genomes
        while (i_g1 < g1.getConnections().size() && i_g2 < g2.getConnections().size()) {
            ConnectionGene gene1 = g1.getConnections().get(i_g1);
            ConnectionGene gene2 = g1.getConnections().get(i_g2);
            int in1 = gene1.getInnovationNumber();
            int in2 = gene2.getInnovationNumber();

            // If both genes have the same innovation number...
            if (in1 == in2) {
                if (Math.random() > 0.5) {
                    // Take the first gene and put it in the child
                    child.getConnections().add(neat.getConnection(gene1));
                }
                else {
                    // Take the second gene and put it in the child
                    child.getConnections().add(neat.getConnection(gene2));
                }
            }
            // If the innovation number of the node in genome 1 is higher than that in genome 2...
            else if (in1 > in2) {
                // Disjoint gene in genome 2
                i_g2++;
            }
            // Otherwise in1 < in2...
            else {
                // Disjoint gene in genome 1
                child.getConnections().add(neat.getConnection(gene1));
                i_g1++;
            }
        }

        while (i_g1 < g1.getConnections().size()) {
            ConnectionGene gene1 = g1.getConnections().get(i_g1);
            child.getConnections().add(neat.getConnection(gene1));

            i_g1++;
        }

        for (ConnectionGene c : child.getConnections()) {
            child.getNodes().add(c.getFromNode());
            child.getNodes().add(c.getToNode());
        }

        return child;
    }

    /**
     * Determines the likeness of two genomes
     * @param g2
     * @return
     */
    public double distance(Genome g2) {
        Genome g1 = this;
        int highestInnovation_gene1 = 0;
        int highestInnovation_gene2 = 0;
        int i_g1 = 0;
        int i_g2 = 0;
        int disjoints = 0;
        int excesses = 0;
        double weightDifference = 0;
        int similarities = 0;

        if (g1.getConnections().size() != 0) {
            highestInnovation_gene1 = g1.getConnections().get(g1.getConnections().size()-1).getInnovationNumber();
        }

        if (g2.getConnections().size() != 0) {
            highestInnovation_gene2 = g2.getConnections().get(g2.getConnections().size()-1).getInnovationNumber();
        }

        // If the highest innovation number in genome 2 is greater than the highest in genome 1...
        if (highestInnovation_gene1 < highestInnovation_gene2) {
            // Swap the genomes
            Genome g = g1;
            g1 = g2;
            g2 = g;
        }

        // Iterate through both the first and second genomes
        while (i_g1 < g1.getConnections().size() && i_g2 < g2.getConnections().size()) {
            // Get the i'th gene from each genome
            ConnectionGene gene1 = g1.getConnections().get(i_g1);
            ConnectionGene gene2 = g2.getConnections().get(i_g2);
            int in1 = gene1.getInnovationNumber();
            int in2 = gene2.getInnovationNumber();

            // If the genes are of the same innovation number...
            if (in1 == in2) {
                // They are similar genes
                i_g1++;
                i_g2++;
                similarities++;
                weightDifference += Math.abs(gene1.getWeight() - gene2.getWeight());
            }
            // If the innovation number of the node in genome 1 is higher than that in genome 2...
            else if (in1 > in2) {
                // Disjoint gene in genome 2
                i_g2++;
                disjoints++;
            }
            // Otherwise in1 < in2...
            else {
                // Disjoint gene in genome 1
                i_g1++;
                disjoints++;
            }
        }

        weightDifference /= similarities;
        excesses = g1.getConnections().size() - i_g1;
        double N = Math.max(g1.getConnections().size(), g2.getConnections().size());
        if (N < 20) {
            N = 1;
        }

        return neat.getC1() * (disjoints / N) +
                neat.getC2() * (excesses / N) +
                neat.getC3() * weightDifference;
    }

    public ArrayList<ConnectionGene> getConnections() {
        return connections;
    }

    public void setConnections(ArrayList<ConnectionGene> connections) {
        this.connections = connections;
    }

    public ArrayList<NodeGene> getNodes() {
        return nodes;
    }

    public void setNodes(ArrayList<NodeGene> nodes) {
        this.nodes = nodes;
    }

    public Neat getNeat() {
        return neat;
    }

    public void setNeat(Neat neat) {
        this.neat = neat;
    }
}

package pacman.controllers.examples.po.NN.NEAT;

import pacman.controllers.examples.po.NN.NEAT.Calculations.Calculator;
import pacman.controllers.examples.po.NN.NEAT.Calculations.Connection;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Random;

public class Genome implements Comparable {

    private ArrayList<ConnectionGene> connections;
    private ArrayList<NodeGene> nodes;
    private Neat neat;
    private double score;
    private Calculator calculator;
    private double inputLayerSprawl;
    private double hiddenLayerSprawl;

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

    public Genome(ArrayList<ConnectionGene> connections, ArrayList<NodeGene> nodes,
                  Neat neat, double score) {
        this.connections = connections;
        this.nodes = nodes;
        this.neat = neat;
        this.score = score;
    }

    /**
     * Copy constructor
     * @param other
     */
    public Genome(Genome other) {
        ArrayList<ConnectionGene> otherCons = new ArrayList<>();
        ArrayList<NodeGene> otherNodes = new ArrayList<>();

        try {
            for (ConnectionGene con : connections) {
                otherCons.add(con.deepCopy());
            }
        }
        catch (Exception e) {
                e.printStackTrace();
            }

        for (NodeGene node : nodes) {
            otherNodes.add(node.deepCopy());
        }

        Genome g = new Genome(otherCons, otherNodes, other.getNeat(),
                other.getScore());
        this.nodes = otherNodes;
        this.connections = otherCons;
        this.neat = other.getNeat();
        this.score = other.getScore();
    }

    public void update() {
        this.calculator = new Calculator(this);
    }

    public double[] calculate(double... input) throws Exception {
        if (this.calculator == null) {
            this.calculator = new Calculator(this);
        }
        return this.calculator.calculate(input);
    }

    private BigDecimal calculateReachOfNode(NodeGene node) {
        ArrayList<ConnectionGene> cons = new ArrayList<>();
        double sum = 0.0;

        // Get all connections where this node is the fromNode
        for (ConnectionGene connection : connections) {
            if (connection.getFromNode() == node && connection.isEnabled()) {
                cons.add(connection);
            }
        }

        // Iterate through all the connections
        for (ConnectionGene connection : cons) {
            // Get the positions of the nodes
            double Ax = connection.getFromNode().getX();
            double Ay = connection.getFromNode().getY();
            double Bx = connection.getToNode().getX();
            double By = connection.getToNode().getY();

            // Calculate length of connection using pythagoras
            double hyp = Math.sqrt(Math.pow(Bx - Ax, 2) + Math.pow(By - Ay, 2));
            sum += hyp;
        }

        return new BigDecimal(sum);
    }

    public BigDecimal calculateInputLayerReach() {
        ArrayList<NodeGene> inputNodes = findInputNodes();
        double sum = 0.0;

        for (NodeGene node : inputNodes) {
            sum += calculateReachOfNode(node).doubleValue();
        }

        return new BigDecimal(sum);
    }

    public BigDecimal calculateHiddenLayerReach() {
        ArrayList<NodeGene> hiddenNodes = findHiddenNodes();
        double sum = 0.0;

        for (NodeGene node : hiddenNodes) {
            sum += calculateReachOfNode(node).doubleValue();
        }

        return new BigDecimal(sum);
    }

    /**
     * Calculate the sprawl between 3 nodes
     * @param n1
     * @param n2
     * @param n3
     */
    public BigDecimal calculateSprawlOfNodes(NodeGene n1, NodeGene n2, NodeGene n3) {
        boolean AB_found = false;
        boolean AC_found = false;
        // Check there is a connection between n1 and n2
        for (ConnectionGene connection : connections) {
            if (connection.getFromNode() == n1 && connection.getToNode() == n2) {
                AB_found = true;
            }
        }

        // Check there is a connection between n1 and n3
        for (ConnectionGene connection : connections) {
            if (connection.getFromNode() == n1 && connection.getToNode() == n3) {
                AC_found = true;
            }
        }

        if (!AB_found || !AC_found) {
            return new BigDecimal(-1);
        }

        MathContext mc = new MathContext(16);

        // Get distance between n1 and n2
        double AB_xDiff = n1.getX() - n2.getX();
        double AB_yDiff = n1.getY() - n2.getY();
        double AB_distance = Math.sqrt(Math.pow(AB_xDiff, 2) + Math.pow(AB_yDiff, 2));

        // Get distance between n1 and n3
        double AC_xDiff = n1.getX() - n3.getX();
        double AC_yDiff = n1.getY() - n3.getY();
        double AC_distance = Math.sqrt(Math.pow(AC_xDiff, 2) + Math.pow(AC_yDiff, 2));

        // Get distance between n2 and n3
        double BC_xDiff = n2.getX() - n3.getX();
        double BC_yDiff = n2.getY() - n3.getY();
        double BC_distance = Math.sqrt(Math.pow(BC_xDiff, 2) + Math.pow(BC_yDiff, 2));

        // Calculate angle
        double numerator = Math.pow(AB_distance, 2) + Math.pow(AC_distance, 2) - Math.pow(BC_distance, 2);
        double denominator = 2 * AB_distance * AC_distance;
        double angle = Math.acos(numerator / denominator);

        return new BigDecimal(angle, mc);
    }

    public ArrayList<NodeGene> findInputNodes() {
        double inputLayerX = 1.0;

        // Find where the input layer is located on the x axis
        for (NodeGene node : nodes) {
            if (node.getX() < inputLayerX) {
                inputLayerX = node.getX();
            }
        }

        // Get the input nodes
        ArrayList<NodeGene> inputNodes = new ArrayList<>();
        for (NodeGene node: nodes) {
            if (node.getX() <= inputLayerX) {
                inputNodes.add(node);
            }
        }

        return inputNodes;
    }

    private ArrayList<NodeGene> findOutputNodes() {
        double outputLayerX = 0.0;

        // Find where the output layer is located on the x axis
        for (NodeGene node : nodes) {
            if (node.getX() > outputLayerX) {
                outputLayerX = node.getX();
            }
        }

        // Get the output nodes
        ArrayList<NodeGene> outputNodes = new ArrayList<>();
        for (NodeGene node : nodes) {
            if (node.getX() >= outputLayerX) {
                outputNodes.add(node);
            }
        }

        return outputNodes;
    }

    public ArrayList<NodeGene> findHiddenNodes() {
        ArrayList<NodeGene> hiddenNodes = new ArrayList<>(nodes);
        hiddenNodes.removeAll(findInputNodes());
        hiddenNodes.removeAll(findOutputNodes());

        return hiddenNodes;
    }

    /**
     * Calculates the average sprawl of the input layer
     * @return
     */
    public BigDecimal calculateInputLayerSprawl() {
        double sprawl = 0.0;
        double inputLayerX = 1.0;

        // Find where the input layer is located on the x axis
        for (NodeGene node : nodes) {
            if (node.getX() < inputLayerX) {
                inputLayerX = node.getX();
            }
        }

        // Get the input nodes
        ArrayList<NodeGene> inputNodes = findInputNodes();

        // Iterate through all input nodes
        for (NodeGene node : inputNodes) {
            // Get the node's enabled connections
            ArrayList<ConnectionGene> cons = new ArrayList<>();
            for (ConnectionGene connection : connections) {
                if (connection.getFromNode() == node && connection.isEnabled()) {
                    cons.add(connection);
                }
            }

            // If there is more than one connection...
            if (cons.size() > 1) {
                // Get the B node
                NodeGene bNode = new NodeGene(0, 0.0, 0.0);
                for (ConnectionGene connection : cons) {
                    if (connection.getToNode().getY() > bNode.getY()) {
                        bNode = connection.getToNode();
                    }
                }

                // Get the C node
                NodeGene cNode = new NodeGene(0, 0.0, 1.0);
                for (ConnectionGene connection : cons) {
                    if (connection.getToNode().getY() < cNode.getY()) {
                        cNode = connection.getToNode();
                    }
                }

                // Get the sprawl between the 3 nodes
                sprawl += calculateSprawlOfNodes(node, bNode, cNode).doubleValue();
            }
        }

        return new BigDecimal(sprawl, new MathContext(16));
    }

    /**
     * Calculates the average sprawl of the hidden layer
     * @return
     */
    public BigDecimal calculateHiddenLayerSprawl() {
        double sprawl = 0.0;

        // All the hidden nodes are the subset without input and output nodes, so...
        ArrayList<NodeGene> hiddenNodes = findHiddenNodes();

        // Iterate through all hidden nodes
        for (NodeGene node : hiddenNodes) {
            // Get the node's connections
            ArrayList<ConnectionGene> cons = new ArrayList<>();
            for (ConnectionGene connection : connections) {
                if (connection.getFromNode() == node && connection.isEnabled()) {
                    cons.add(connection);
                }
            }

            // If there is more than one connection...
            if (cons.size() > 1) {
                // Get the B node (highest node on Y axis)
                NodeGene bNode = new NodeGene(0, 0.0, 0.0);
                for (ConnectionGene connection : cons) {
                    if (connection.getToNode().getY() > bNode.getY()) {
                        bNode = connection.getToNode();
                    }
                }

                // Get the C node (lowest node on Y axis)
                NodeGene cNode = new NodeGene(0, 0.0, 1.0);
                for (ConnectionGene connection : cons) {
                    if (connection.getToNode().getY() < cNode.getY()) {
                        cNode = connection.getToNode();
                    }
                }

                // Get the sprawl between the 3 nodes
                sprawl += calculateSprawlOfNodes(node, bNode, cNode).doubleValue();
            }
        }

        return new BigDecimal(sprawl, new MathContext(16));
    }

    public ArrayList<ConnectionGene> findEnabledConnections() {
        ArrayList<ConnectionGene> cons = new ArrayList<>();
        for (ConnectionGene connection : connections) {
            if (connection.isEnabled()) {
                cons.add(connection);
            }
        }
        return cons;
    }

    public double calculateInputVectorSum() {
        double sum = 0.0;
        ArrayList<NodeGene> inputNodes = findInputNodes();
        ArrayList<ConnectionGene> inputConnections = new ArrayList<>();

        for (NodeGene node : inputNodes) {
            for (ConnectionGene con : connections) {
                if (con.getFromNode() == node && con.isEnabled()) {
                    inputConnections.add(con);
                }
            }
        }

        for (ConnectionGene con : inputConnections) {
            sum += con.getWeight();
        }

        return sum;
    }

    public double calculateHiddenVectorSum() {
        double sum = 0.0;
        ArrayList<NodeGene> hiddenNodes = findHiddenNodes();
        ArrayList<ConnectionGene> hiddenConnections = new ArrayList<>();

        for (NodeGene node : hiddenNodes) {
            for (ConnectionGene con : connections) {
                if (con.getFromNode() == node && con.isEnabled()) {
                    hiddenConnections.add(con);
                }
            }
        }

        for (ConnectionGene con : hiddenConnections) {
            sum += con.getWeight();
        }

        return sum;
    }

    /**
     * Mutate a network's topology by adding a connection, adding a node or changing a neuron's weights
     * @param count     how many times to mutate
     */
    public void mutate(int count) {
        for (int i = 0; i < count; i++) {
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
        Genome child = neat.emptyGenome(0);
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
                i_g1++;
                i_g2++;
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

        // Remove any duplicates
        ArrayList<ConnectionGene> newList = new ArrayList<>();

        for (ConnectionGene con : child.getConnections()) {
            if (!newList.contains(con)) {
                newList.add(neat.getConnection(con));
            }
        }

        child.setConnections(newList);

        for (ConnectionGene c : child.getConnections()) {
            NodeGene fromNode = c.getFromNode();
            NodeGene toNode = c.getToNode();
            ArrayList<NodeGene> nodes = child.getNodes();

            // Make sure nodes aren't being added twice
            if (!nodes.contains(fromNode)) {
                child.getNodes().add(fromNode);
            }
            if (!nodes.contains(toNode)) {
                child.getNodes().add(toNode);
            }
        }

        // Mutate the child
        child.mutate(1);

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

    /**
     * Returns a deep copy of itself
     * @return
     */
    public Genome deepCopy() {
        ArrayList<ConnectionGene> newCons = new ArrayList<>();
        ArrayList<NodeGene> newNodes = new ArrayList<>();

        // Deep copy the connection genes
        for (ConnectionGene con : connections) {
            newCons.add(con.deepCopy());
        }

        // Deep copy the node genes
        for (NodeGene node : nodes) {
            newNodes.add(node.deepCopy());
        }

        return new Genome(newCons, newNodes, this.neat, this.score);
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

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public int compareTo(Object o) {
        double diff = (((Genome) o).getScore() - this.score);
        return (int)((((Genome) o).getScore() - this.score) * 1000);
    }
}

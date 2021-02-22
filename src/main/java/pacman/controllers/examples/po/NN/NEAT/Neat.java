package pacman.controllers.examples.po.NN.NEAT;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class used to perform NEAT on neural networks
 *
 * @author Sam Robinson
 */
public class Neat {

    private HashMap<ConnectionGene, ConnectionGene> connections;
    private ArrayList<NodeGene> nodes;
    public static final int MAX_NODES = (int) Math.pow(2,20);
    private int inputSize;
    private int outputSize;
    private int maxClients;
    private double C1 = 1, C2 = 1, C3 = 1;
    private double WEIGHT_SHIFT_STRENGTH = 0.3, WEIGHT_RANDOM_STRENGTH = 1;
    private double PROBABILITY_MUTATE_LINK = 0.4;
    private double PROBABILITY_MUTATE_NODE = 0.4;
    private double PROBABILITY_MUTATE_WEIGHT_SHIFT = 0.4;
    private double PROBABILITY_MUTATE_WEIGHT_RANDOM = 0.4;
    private double PROBABILITY_MUTATE_TOGGLE_LINK = 0.4;

    public Neat() {
        connections = new HashMap<>();
        nodes = new ArrayList<>();
    }

    public Neat(int inputSize, int outputSize, int maxClients) {
        connections = new HashMap<>();
        nodes = new ArrayList<>();
        this.reset(inputSize, outputSize, maxClients);
    }

    public Genome emptyGenome() {
        Genome g = new Genome(this);

        for (int i = 0; i < inputSize + outputSize; i++) {
            g.getNodes().add(getNode(i + 1));
        }

        return g;
    }

    public void reset(int inputSize, int outputSize, int maxClients) {
        this.inputSize = inputSize;
        this.outputSize = outputSize;
        this.maxClients = maxClients;

        connections.clear();
        nodes.clear();

        for (int i = 0; i < inputSize; i++) {
            NodeGene n = getNode();
            n.setX(0.1);
            n.setY((i + 1) / (inputSize + 0.1));
        }

        for (int i = 0; i < outputSize; i++) {
            NodeGene n = getNode();
            n.setX(0.9);
            n.setY((i + 1) / (inputSize + 0.1));
        }
    }

    /**
     * Creates a new node and returns it
     * @return
     */
    public NodeGene getNode() {
        NodeGene n = new NodeGene(nodes.size() + 1);
        nodes.add(n);
        return n;
    }

    /**
     * Finds a node in the list and returns it. If it is not in the list, then a new node is made and returned
     * @param id
     * @return
     */
    public NodeGene getNode(int id) {
        if (id <= nodes.size()) {
            return nodes.get(id - 1);
        }
        return getNode();
    }

    public static ConnectionGene getConnection(ConnectionGene con) {
        ConnectionGene c = new ConnectionGene(con.getFromNode(), con.getToNode());
        c.setWeight(con.getWeight());
        c.setEnabled(con.isEnabled());
        return c;
    }

    public ConnectionGene getConnection(NodeGene node1, NodeGene node2) {
        ConnectionGene connectionGene = new ConnectionGene(node1, node2);

        if (connections.containsKey(connectionGene)) {
            connectionGene.setInnovationNumber(connections.get(connectionGene).getInnovationNumber());
        }
        else {
            connectionGene.setInnovationNumber(connections.size() + 1);
            connections.put(connectionGene, connectionGene);
        }

        return connectionGene;
    }


    public HashMap<ConnectionGene, ConnectionGene> getConnections() {
        return connections;
    }

    public void setConnections(HashMap<ConnectionGene, ConnectionGene> connections) {
        this.connections = connections;
    }

    public double getC1() {
        return C1;
    }

    public void setC1(double c1) {
        C1 = c1;
    }

    public double getC2() {
        return C2;
    }

    public void setC2(double c2) {
        C2 = c2;
    }

    public double getC3() {
        return C3;
    }

    public void setC3(double c3) {
        C3 = c3;
    }

    public double getWEIGHT_SHIFT_STRENGTH() {
        return WEIGHT_SHIFT_STRENGTH;
    }

    public void setWEIGHT_SHIFT_STRENGTH(double WEIGHT_SHIFT_STRENGTH) {
        this.WEIGHT_SHIFT_STRENGTH = WEIGHT_SHIFT_STRENGTH;
    }

    public double getWEIGHT_RANDOM_STRENGTH() {
        return WEIGHT_RANDOM_STRENGTH;
    }

    public void setWEIGHT_RANDOM_STRENGTH(double WEIGHT_RANDOM_STRENGTH) {
        this.WEIGHT_RANDOM_STRENGTH = WEIGHT_RANDOM_STRENGTH;
    }

    public double getPROBABILITY_MUTATE_LINK() {
        return PROBABILITY_MUTATE_LINK;
    }

    public void setPROBABILITY_MUTATE_LINK(double PROBABILITY_MUTATE_LINK) {
        this.PROBABILITY_MUTATE_LINK = PROBABILITY_MUTATE_LINK;
    }

    public double getPROBABILITY_MUTATE_NODE() {
        return PROBABILITY_MUTATE_NODE;
    }

    public void setPROBABILITY_MUTATE_NODE(double PROBABILITY_MUTATE_NODE) {
        this.PROBABILITY_MUTATE_NODE = PROBABILITY_MUTATE_NODE;
    }

    public double getPROBABILITY_MUTATE_WEIGHT_SHIFT() {
        return PROBABILITY_MUTATE_WEIGHT_SHIFT;
    }

    public void setPROBABILITY_MUTATE_WEIGHT_SHIFT(double PROBABILITY_MUTATE_WEIGHT_SHIFT) {
        this.PROBABILITY_MUTATE_WEIGHT_SHIFT = PROBABILITY_MUTATE_WEIGHT_SHIFT;
    }

    public double getPROBABILITY_MUTATE_WEIGHT_RANDOM() {
        return PROBABILITY_MUTATE_WEIGHT_RANDOM;
    }

    public void setPROBABILITY_MUTATE_WEIGHT_RANDOM(double PROBABILITY_MUTATE_WEIGHT_RANDOM) {
        this.PROBABILITY_MUTATE_WEIGHT_RANDOM = PROBABILITY_MUTATE_WEIGHT_RANDOM;
    }

    public double getPROBABILITY_MUTATE_TOGGLE_LINK() {
        return PROBABILITY_MUTATE_TOGGLE_LINK;
    }

    public void setPROBABILITY_MUTATE_TOGGLE_LINK(double PROBABILITY_MUTATE_TOGGLE_LINK) {
        this.PROBABILITY_MUTATE_TOGGLE_LINK = PROBABILITY_MUTATE_TOGGLE_LINK;
    }

    public int getMaxClients() {
        return maxClients;
    }

    public void setMaxClients(int maxClients) {
        this.maxClients = maxClients;
    }
}

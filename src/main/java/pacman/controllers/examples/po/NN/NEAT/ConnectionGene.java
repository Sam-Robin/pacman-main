package pacman.controllers.examples.po.NN.NEAT;

import pacman.controllers.examples.po.NN.Neuron;

import java.util.Random;

/**
 * Represents a connection between two Neuron objects
 */
public class ConnectionGene extends Gene {

    private NodeGene fromNode, toNode;
    private double weight;
    private boolean enabled = true;

    public ConnectionGene(NodeGene fromNode, NodeGene toNode) {
        this.fromNode = fromNode;
        this.toNode = toNode;
        randomWeight();
    }

    public ConnectionGene(NodeGene fromNode, NodeGene toNode,
                          double weight, boolean enabled, int innovationNumber) {
        super(innovationNumber);
        this.fromNode = fromNode;
        this.toNode = toNode;
        this.weight = weight;
        this.enabled = enabled;
    }

    private void randomWeight() {
        Random random = new Random();
        this.weight = (2 * random.nextDouble()) - 1;
    }

    public boolean equals(Object o) {
        if (!(o instanceof ConnectionGene)) {
            return false;
        }

        ConnectionGene c = (ConnectionGene) o;
        return (fromNode.equals(c.getFromNode()) && toNode.equals(c.getToNode()));
    }

    public ConnectionGene deepCopy() {
        // Create deep copies of the node genes
        NodeGene newFrom = fromNode.deepCopy();
        NodeGene newTo = toNode.deepCopy();

        return new ConnectionGene(newFrom, newTo, this.weight,
                this.enabled, this.innovationNumber);
    }

    public int hashCode() {
        return fromNode.getInnovationNumber() * Neat.MAX_NODES + toNode.getInnovationNumber();
    }

    public NodeGene getFromNode() {
        return fromNode;
    }

    public void setFromNode(NodeGene fromNode) {
        this.fromNode = fromNode;
    }

    public NodeGene getToNode() {
        return toNode;
    }

    public void setToNode(NodeGene toNode) {
        this.toNode = toNode;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}

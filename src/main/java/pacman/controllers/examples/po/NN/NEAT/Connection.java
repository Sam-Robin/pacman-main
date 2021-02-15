package pacman.controllers.examples.po.NN.NEAT;

import pacman.controllers.examples.po.NN.Neuron;

/**
 * Represents a connection between two Neuron objects
 */
public class Connection {

    private Neuron fromNode, toNode;
    private double weight;
    private boolean isEnabled;
    private int innovationNumber;       // details when this connection was made

    public Connection() {

    }

    public Connection(Neuron fromNode, Neuron toNode, double weight,
                      boolean isEnabled, int innovationNumber) {
        this.fromNode = fromNode;
        this.toNode = toNode;
        this.weight = weight;
        this.isEnabled = isEnabled;
        this.innovationNumber = innovationNumber;
    }

    public Neuron getFromNode() {
        return fromNode;
    }

    public void setFromNode(Neuron fromNode) {
        this.fromNode = fromNode;
    }

    public Neuron getToNode() {
        return toNode;
    }

    public void setToNode(Neuron toNode) {
        this.toNode = toNode;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getInnovationNumber() {
        return innovationNumber;
    }

    public void setInnovationNumber(int innovationNumber) {
        this.innovationNumber = innovationNumber;
    }

    public boolean isEnabled() {
        return isEnabled;
    }
}

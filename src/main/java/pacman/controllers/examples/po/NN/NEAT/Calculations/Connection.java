package pacman.controllers.examples.po.NN.NEAT.Calculations;

import pacman.controllers.examples.po.NN.NEAT.NodeGene;

public class Connection {

    private Node fromNode, toNode;
    private double weight;
    private boolean enabled = true;

    public Connection(Node fromNode, Node toNode) {
        this.fromNode = fromNode;
        this.toNode = toNode;
    }

    public double getWeight() {
        return weight;
    }

    public Node getFromNode() {
        return fromNode;
    }

    public void setFromNode(Node fromNode) {
        this.fromNode = fromNode;
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

    public Node getToNode() {
        return toNode;
    }

    public void setToNode(Node toNode) {
        this.toNode = toNode;
    }
}

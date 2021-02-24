package pacman.controllers.examples.po.NN.NEAT.Calculations;

import pacman.controllers.examples.RandomNonRevPacMan;
import pacman.controllers.examples.po.NN.NEAT.NodeGene;

import java.util.ArrayList;
import java.util.Random;

public class Node implements Comparable {

    private double x;
    private double output;
    private double bias;
    private ArrayList<Connection> connections;

    public Node() {
        this.connections = new ArrayList<>();
        this.x = 0;
        this.output = 0;

        Random random = new Random();
        int min = -50;
        int max = 50;
        this.bias = random.nextInt(max - min) + min;
    }

    public Node(double x) {
        this.x = x;
        this.connections = new ArrayList<>();
        this.output = 0;

        Random random = new Random();
        int min = -50;
        int max = 50;
        this.bias = random.nextInt(max - min) + min;
    }

    /**
     * Calculate the output value of this neuron
     * @return
     */
    public void calculate() {
        double linearCombo = linearCombination();
        // f(x) = f(v + b) aka f(linearCombo + bias)
        output = sigmoid(linearCombo + bias);
    }

    /**
     * Calculate the linear combination of inputs and weights
     * @return  the sum of weights minus inputs
     */
    private double linearCombination() {
        double sum = 0;
        // Find sum(weight - input)
        for (Connection c : connections) {
            if (c.isEnabled()) {
                // Add (weight - input) to sum
                sum += (c.getWeight() - c.getFromNode().getOutput());
            }
        }

        return sum;
    }

    /**
     * Calculate the
     * @param x     the x value for sigmoid f(x)
     * @return      f(x)
     */
    private double sigmoid(double x) {
        // sigmoid: f(x) = ( 1 + e^-x )^-1
        return (1 / (1 + Math.pow(Math.E,(-1*x))));
    }

    @Override
    public int compareTo(Object o) {
        return Double.compare(this.x, ((Node) o).getX());
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getOutput() {
        return output;
    }

    public void setOutput(double output) {
        this.output = output;
    }

    public ArrayList<Connection> getConnections() {
        return connections;
    }

    public void setConnections(ArrayList<Connection> connections) {
        this.connections = connections;
    }

    public double getBias() {
        return bias;
    }

    public void setBias(double bias) {
        this.bias = bias;
    }
}

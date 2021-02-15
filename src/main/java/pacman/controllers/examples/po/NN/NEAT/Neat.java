package pacman.controllers.examples.po.NN.NEAT;

import pacman.controllers.examples.po.NN.NeuralNetwork;
import pacman.controllers.examples.po.NN.Neuron;

import java.util.ArrayList;

/**
 * Class used to perform NEAT on neural networks
 *
 * @author Sam Robinson
 */
public class Neat {

    private ArrayList<Connection> connections;

    public Neat() {
        connections = new ArrayList<>();
    }

    /**
     * Mutate a network's topology by adding a connection, adding a node or changing a neuron's weights
     */
    public NeuralNetwork mutate() {
        return null;
    }

    /**
     * Create a new network by combining two together
     * @return
     */
    public NeuralNetwork crossover() {
        return null;
    }

    /**
     * Determine the compatibility (or likeness) of two networks
     * @param n1
     * @param n2
     * @return
     */
    public double calculateCompatibility(NeuralNetwork n1, NeuralNetwork n2) {
        return 0.0;
    }

    /**
     * Finds a connection that has a particular toNeuron
     * @param toNeuron
     * @return
     */
    public Connection findConnectionWithToNeuron(Neuron toNeuron) {
        return null;
    }

    public ArrayList<Connection> getConnections() {
        return connections;
    }

    public void setConnections(ArrayList<Connection> connections) {
        this.connections = connections;
    }
}

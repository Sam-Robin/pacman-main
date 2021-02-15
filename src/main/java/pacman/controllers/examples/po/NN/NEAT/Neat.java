package pacman.controllers.examples.po.NN.NEAT;

import pacman.controllers.examples.po.NN.Layer;
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
    private ArrayList<Neuron> neurons;

    public Neat() {
        connections = new ArrayList<>();
        neurons = new ArrayList<>();
    }

    public void addNetworkData(NeuralNetwork network) {
        // Iterate through every layer in the network
        for (Layer layer : network.getLayers()) {
            // Iterate through every neuron in the network
            for (Neuron neuron : layer.getNeurons()) {
                // Add the neuron to the list of neurons
                neurons.add(neuron);
            }
        }

        // Get the list of connections from the network

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

    /**
     * Adds a set of connections to the already existing connections
     * @param cons
     */
    public void addConnections(ArrayList<Connection> cons) {
        connections.addAll(cons);
    }

    public ArrayList<Connection> getConnections() {
        return connections;
    }

    public void setConnections(ArrayList<Connection> connections) {
        this.connections = connections;
    }
}

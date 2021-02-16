package pacman.controllers.examples.po.NN.NEAT;

import pacman.controllers.examples.po.NN.*;

import java.util.ArrayList;
import java.util.Random;

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

    /**
     * Mutate a network's topology by adding a connection, adding a node or changing a neuron's weights
     * @param network
     */
    public NeuralNetwork mutate(NeuralNetwork network) {
        /*
        *   TODO:
        *   45% chance to add connection to a layer
        *   45% chance to add node on a connection
        *   10% chance to change weights of a neuron
        *
        *   CURRENT:
        *   100% chance to change weights of a neuron
         */
////
////        // Add connection to a layer
////        if (selector < 45) {
////
////        }
////        // Add node on a connection
////        else if (selector < 90) {
////
////        }
////        // Change the weights of a neuron
////        else {
////
////        }

        /* Change weights of a neuron */
        // Select a random layer
        Random random = new Random();
        int min = 0;
        int max = network.getLayers().size();
        int layerSelector = random.nextInt(max - min) + min;
        Layer layer = network.getLayers().get(layerSelector);

        // Select a random neuron
        max = layer.getNeurons().size();
        int neuronSelector = random.nextInt(max - min) + min;
        Neuron neuron = layer.getNeurons().get(neuronSelector);

        // Change its weights
        ArrayList<Double> newWeights = (ArrayList<Double>) NeuralNetwork.generateWeights(neuron.getSynapticWeights().size());
        neuron.setSynapticWeights(newWeights);

        /* Update connections with new connections */
        // Check previous layers if not an InputLayer
        if (!(layer instanceof InputLayer)) {
            // Get the previous layer
            Layer previousLayer = network.getLayers().get(layerSelector - 1);
            // Iterate through the previous layer
            for (int i = 0; i < neuron.getSynapticWeights().size(); i++) {
                Neuron fromNode = previousLayer.getNeurons().get(i);
                double weight = neuron.getSynapticWeights().get(i);
                Connection connection = new Connection(fromNode, neuron, weight, true);
                connections.add(connection);
            }
        }

        return network;
    }

    /**
     * Create a new network by combining two together
     * @param parent1   fittest parent
     * @param parent2   less fit parent
     * @return
     */
    public NeuralNetwork crossover(NeuralNetwork parent1, NeuralNetwork parent2) {
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

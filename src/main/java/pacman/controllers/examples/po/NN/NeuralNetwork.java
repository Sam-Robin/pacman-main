package pacman.controllers.examples.po.NN;

import pacman.controllers.examples.po.NN.NEAT.Connection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a neural network. Is scalable and versatile
 * to changing architecture.
 *
 * @author Sam Robinson
 */
public class NeuralNetwork implements Serializable {

    List<Layer> layers;

    /**
     * Create a default network with one InputLayer, one HiddenLayer
     * and one OutputLayer
     */
    public NeuralNetwork() {
        layers = new ArrayList<>();
        InputLayer inputLayer = new InputLayer();
        HiddenLayer hiddenLayer = new HiddenLayer();
        OutputLayer outputLayer = new OutputLayer();
        layers.add(inputLayer);
        layers.add(hiddenLayer);
        layers.add(outputLayer);
    }

    public NeuralNetwork(List<Layer> layers) {
        this.layers = layers;
    }

    /**
     * Train the neural network
     */
    public void train() {
        for (int i = 0; i < layers.size(); i++) {
            Layer currentLayer = layers.get(i);
            // If the current layer is the input layer...
            if (currentLayer instanceof InputLayer) {
                // Then the inputs are already defined, so train
                currentLayer.train();
            }
            // If the current layer is not the input layer...
            else {
                // Then the inputs are not defined
                Layer previousLayer = layers.get(i - 1);
                List<Double> previousOutputs = previousLayer.getOutputs();
                // Set the outputs of the previous layer as the inputs
                currentLayer.setInputs(previousOutputs);
                currentLayer.train();
            }
        }
    }

    public List<Double> getOutputs() {
        // Return the last layer's output
        return layers.get(layers.size() - 1).getOutputs();
    }

    public void setInputs(List<Double> inputs) {
        layers.get(0).setInputs(inputs);
    }

    /**
     * Return all neuron connections (used by NEAT)
     * @return
     */
    public ArrayList<Connection> getConnections() {
        ArrayList<Connection> output = new ArrayList<>();

        // Iterate through all layers
        for (int l = 0; l < layers.size(); l++) {
            Layer currentLayer = layers.get(l);
            // Do not add connections from the InputLayer - they have no fromNode
            if (!(currentLayer instanceof InputLayer)) {
                Layer previousLayer = layers.get(l - 1);
                // Iterate through the neurons in the layer
                for (Neuron neuron : currentLayer.getNeurons()) {
                    int synapticWeightsLength = neuron.getSynapticWeights().size();
                    // Iterate through the synaptic weights in this neuron
                    for (int i = 0; i < synapticWeightsLength; i++) {
                        Neuron fromNode = previousLayer.getNeurons().get(i);
                        Neuron toNode = neuron;
                        double weight = neuron.getSynapticWeights().get(i);
                        Connection connection = new Connection(fromNode, toNode, weight, true);
                        output.add(connection);
                    }
                }
            }
        }
        return output;
    }

    /**
     * Sum all the outputs of the network
     * @return
     */
    public double getSum() {
        double sum = 0.0;
        // For every output in the final layer...
        for (double d: layers.get(layers.size() - 1).getOutputs()) {
            sum += d;
        }

        return sum;
    }

    /**
     * Create a double[] of length n, with weights as each element
     * @param n     array length
     * @return      array of weights
     */
    public static List<Double> generateWeights(int n) {
        List<Double> output = new ArrayList<>();
        double min = -1;
        double max = 1;
        Random r = new Random();
        for (int i = 0; i < n; i++) {
            output.add(min + (max - min) * r.nextDouble());
        }
        return output;
    }

    /**
     * Create a random bias between -100 and 100
     * @return  bias
     */
    public static double generateBias() {
        Random r = new Random();
        double min = -10;
        double max = 10;
        double bias = (min + (max - min) * r.nextDouble());     // Random bias
        return bias;
    }

    /**
     * A generic cost function that uses the outputs of the network
     * @param goal      goal value
     * @param network   network to determine cost of
     * @return          cost
     */
    public static double costFunction(double goal, NeuralNetwork network) {
        return Math.abs(goal - network.getSum());
    }

    /**
     *
     * @param inputs    Input variables
     * @param design    Dimensions of the network
     * @return          Neural network according to design
     */
    public static NeuralNetwork generateRandomNetwork(List<Double> inputs,
                                                      int[] design) {
        List<Layer> layers = new ArrayList<>();
        // Create neurons for input layer
        List<Neuron> neurons = new ArrayList<>();
        for (int i = 0; i < design[0]; i++) {
            Neuron neuron = new Neuron(inputs, generateWeights(inputs.size()), generateBias());
            neurons.add(neuron);
        }
        // Create input layer
        InputLayer inputLayer = new InputLayer(neurons, inputs);
        layers.add(inputLayer);     // Add input layer to layers

        // Create hidden layer(s)
        // Hidden layers are stored in all but the first and last elements of design
        for (int i = 1; i < design.length - 1; i++) {
            neurons = new ArrayList<>();
            // Create neurons as per the design
            for (int n = 0; n < design[i]; n++) {
                // Number of weights is equal to the number of neurons in the previous layer
                List<Double> weights = generateWeights(design[i - 1]);
                neurons.add(new Neuron(null, weights, generateBias()));
            }
            // Create this hidden layer
            HiddenLayer hiddenLayer = new HiddenLayer(neurons);
            layers.add(hiddenLayer);        // Add hidden layer to network
        }

        // Create neurons for output layer
        neurons = new ArrayList<>();
        for (int i = 0; i < design[design.length - 1]; i++) {
            // Number of weights is equal to the number of neurons in the
            // second-to-last layer
            List<Double> weights = generateWeights(design[design.length - 2]);
            neurons.add(new Neuron(null, weights, generateBias()));
        }
        // Create output layer
        OutputLayer outputLayer = new OutputLayer(neurons);
        layers.add(outputLayer);        // Add output layer to neural network
        return new NeuralNetwork(layers);
    }

    public List<Layer> getLayers() {
        return layers;
    }
}

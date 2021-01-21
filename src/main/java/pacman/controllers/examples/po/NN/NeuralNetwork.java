package pacman.controllers.examples.po.NN;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
}

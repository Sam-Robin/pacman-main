package pacman.controllers.examples.po.NN;

import java.util.List;

/**
 * Represents a hidden layer in a neural network
 *
 * @author Sam Robinson
 */
public class HiddenLayer extends Layer {

    public HiddenLayer() {

    }

    public HiddenLayer(List<Neuron> neurons) {
        this.neurons = neurons;
    }

    public HiddenLayer(List<Neuron> neurons, List<Double> inputs) {
        this.neurons = neurons;
        this.inputs = inputs;
    }
}

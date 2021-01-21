package pacman.controllers.examples.po.NN;

import java.util.List;

/**
 * Represents the first layer in a neural network
 *
 * @author Sam Robinson
 */
public class InputLayer extends Layer {

    public InputLayer() {

    }

    public InputLayer(List<Neuron> neurons, List<Double> inputs) {
        this.neurons = neurons;
        this.inputs = inputs;
    }
}
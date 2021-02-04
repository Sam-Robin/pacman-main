package pacman.controllers.examples.po.NN;

import java.util.List;

/**
 * Represents the final layer of a neural network
 *
 * @author Sam Robinson
 */
public class OutputLayer extends Layer {

    public OutputLayer() { }

    public OutputLayer(List<Neuron> neurons) {
        this.neurons = neurons;
    }

    public OutputLayer(List<Neuron> neurons, List<Double> inputs) {
        this.neurons = neurons;
        this.inputs = inputs;
    }
}

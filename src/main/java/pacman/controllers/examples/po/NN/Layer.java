package pacman.controllers.examples.po.NN;

import java.util.ArrayList;
import java.util.List;

/**
 * An generic representation of a neural network layer
 *
 * @author Sam Robinson
 */
public class Layer {

    protected List<Neuron> neurons;
    protected List<Double> inputs;
    protected List<Double> outputs;

    public Layer() {
        this.neurons = new ArrayList<>();
        this.inputs = new ArrayList<>();
        this.outputs = new ArrayList<>();
    }

    public Layer(List<Neuron> neurons, List<Double> inputs, List<Double> outputs) {
        this.neurons = neurons;
        this.inputs = inputs;
        this.outputs = outputs;
    }

    /**
     * Read the inputs into each neuron in this layer
     */
    public void setInputs(List<Double> inputs) {
        this.inputs = inputs;
        for (Neuron n : neurons) {
            n.setInputs(inputs);
        }
    }

    /**
     * Return the outputs of each neuron in this layer
     * @return array of outputs
     */
    public List<Double> getOutputs() {
        outputs.clear();
        for (Neuron n : neurons) {
            outputs.add(n.getOutput());
        }
        return outputs;
    }

    /**
     * Train each neuron in this layer
     */
    public void train() {
        // Clear out the previous training session data
        outputs.clear();
        // Iterate through every neuron in this layer
        for (Neuron n : neurons) {
            // Add the neuron's output to the output list
            n.calculate();
        }
    }
}

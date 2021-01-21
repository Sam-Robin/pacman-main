package pacman.controllers.examples.po.NN;

import java.util.List;

/**
 * Represents one neuron
 *
 * @author Sam Robinson
 */
public class Neuron {

    private List<Double> inputs;
    private List<Double> synapticWeights;
    private double bias;

    public Neuron(List<Double> inputs, List<Double> synapticWeights, double bias) {
        this.inputs = inputs;
        this.synapticWeights = synapticWeights;
        this.bias = bias;
    }

    /**
     * Calculate the output value of this neuron
     * @return
     */
    public double getOutput() {
        double linearCombo = linearCombination();
        // f(x) = f(v + b) aka f(linearCombo + bias)
        double output = sigmoid(linearCombo + bias);

        return output;
    }

    /**
     * Calculate the linear combination of inputs and weights
     * @return  the sum of weights minus inputs
     */
    private double linearCombination() {
        double sum = 0;
        // Find sum(weight - input)
        for (int i = 0; i < inputs.size(); i++) {
            // Add (weight - input) to sum
            sum += (synapticWeights.get(i) - inputs.get(i));
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

    public List<Double> getInputs() {
        return inputs;
    }

    public void setInputs(List<Double> inputs) {
        this.inputs = inputs;
    }

    public List<Double> getSynapticWeights() {
        return synapticWeights;
    }

    public void setSynapticWeights(List<Double> synapticWeights) {
        this.synapticWeights = synapticWeights;
    }

    @Override
    public String toString() {
        String output = "";
        // Add inputs to string
        output += "Inputs: ";
        for (int i = 0; i < inputs.size(); i++) {
            output += inputs.get(i) + " ";
        }
        output += "\n";

        // Add weights to string
        output += "Weights: ";
        for (int i = 0; i < synapticWeights.size(); i++) {
            output += synapticWeights.get(i) + " ";
        }
        output += "\nBias: " + bias + "\n";

        return output;
    }
}

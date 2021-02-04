package pacman.controllers.examples.po.NN;

import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class NeuralNetworkTest {

    @Test
    void differentOutputs_OneNeuron() {
        ArrayList<Double> input = new ArrayList<>();
        input.add(200.0);

        Neuron n = new Neuron(input, NeuralNetwork.generateWeights(1), NeuralNetwork.generateBias());
        n.calculate();
        double o1 = n.getOutput();

        input.clear();
        input.add(-100.0);

        n.setInputs(input);
        n.calculate();
        double o2 = n.getOutput();

        assertNotEquals(o1, o2);
    }

    @Test
    void differentOutputs_WithHiddenLayer() {
        NeuralNetwork n = new NeuralNetwork();

        // Create inputs
        ArrayList<Double> inputs = new ArrayList<>();
        inputs.add(2000.0);

        // Create design
        int[] design = { inputs.size(), 2, 4 };

        // Create a new network
        n = NeuralNetwork.generateRandomNetwork(inputs, design);
        n.train();

        // Get the outputs from the first run
        ArrayList<Double> outputsFirst = (ArrayList) n.getOutputs();
        Object[] array1 = outputsFirst.toArray();

        // Change the inputs
        inputs.clear();
        inputs.add(-1000.0);

        n.setInputs(inputs);
        n.train();

        // Get the outputs from the second run
        ArrayList<Double> outputsSecond = (ArrayList) n.getOutputs();
        Object[] array2 = outputsSecond.toArray();

        // Check that the first value is NOT the same
        assertNotEquals(array1[0], array2[0]);
        // Check that the second value is NOT the same
        assertNotEquals(array1[1], array2[1]);
        // Check that the third value is NOT the same
        assertNotEquals(array1[2], array2[2]);
        // Check that the fourth value is NOT the same
        assertNotEquals(array1[3], array2[3]);
    }

}
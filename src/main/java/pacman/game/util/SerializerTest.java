package pacman.game.util;

import org.junit.jupiter.api.Test;
import pacman.controllers.examples.po.NN.Neuron;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SerializerTest {

    @Test
    void deconstructThenConstructNeuron() {
        // Create a neuron
        ArrayList<Double> inputs = new ArrayList<>();
        inputs.add(100.0);
        ArrayList<Double> weights = new ArrayList<>();
        weights.add(5.5);
        double bias = 8.897;
        Neuron n =  new Neuron(inputs, weights, bias);

        // Serialize the neuron to JSON
        Serializer serializer = new Serializer();
        String json = serializer.serialize(n);

        // Deserialize the neuron to Neuron class
        Neuron newNeuron = (Neuron) serializer.deserialize(json, Neuron.class);

        assertEquals(n.getInputs(), newNeuron.getInputs());
        assertEquals(n.getSynapticWeights(), newNeuron.getSynapticWeights());
        assertEquals(n.getOutput(), newNeuron.getOutput());
    }

    @Test
    void deconstuctThenConstructNetwork() {

    }
}
package pacman.game.util;

import org.junit.jupiter.api.Test;
import pacman.controllers.examples.po.NN.Layer;
import pacman.controllers.examples.po.NN.NeuralNetwork;
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
    void deconstructThenConstructNetwork() {
        // Create neural network
        NeuralNetwork n = new NeuralNetwork();

        // Create inputs
        ArrayList<Double> inputs = new ArrayList<>();
        inputs.add(2000.0);
        inputs.add(160.569);
        inputs.add(-56.85);

        // Create design
        int[] design = { inputs.size(), 2, 4 };

        n = NeuralNetwork.generateRandomNetwork(inputs, design);

        // Serialize the network
        Serializer serializer = new Serializer();
        String json = serializer.serialize(n);

        // Deserialize the network
        NeuralNetwork newNetwork = (NeuralNetwork) serializer.deserialize(json, NeuralNetwork.class);

        // Check the outputs are the same
        assertEquals(n.getOutputs(), newNetwork.getOutputs());
        // Check the layers are the same
        ArrayList<Layer> originalLayers = (ArrayList) n.getLayers();
        ArrayList<Layer> newLayers = (ArrayList) newNetwork.getLayers();

        for (int i = 0; i < n.getLayers().size(); i++) {
            // Check that the inputs to the layers are the same
            assertEquals(originalLayers.get(i).getInputs(), newLayers.get(i).getInputs());

            // Check that the Layer subclass type is the same
            assertEquals(originalLayers.get(i).getClass().toString(),
                    newLayers.get(i).getClass().toString());

            // Check that the neurons are the same
            ArrayList<Neuron> originalNeurons = (ArrayList<Neuron>) originalLayers.get(i).getNeurons();
            ArrayList<Neuron> newNeurons = (ArrayList<Neuron>) originalLayers.get(i).getNeurons();
            for (int c = 0; c < originalNeurons.size(); c++) {
                // Check the inputs are the same
                assertEquals(originalNeurons.get(c).getInputs(), newNeurons.get(c).getInputs());
                // Check the synaptic weights are the same
                assertEquals(originalNeurons.get(c).getSynapticWeights(),
                        newNeurons.get(c).getSynapticWeights());
                // Check the output is the same
                assertEquals(originalNeurons.get(c).getOutput(), newNeurons.get(c).getOutput());
            }
        }

    }
}
//package pacman.controllers.examples.po.NN.NEAT;
//
//import org.junit.jupiter.api.Test;
//import pacman.controllers.examples.po.NN.Layer;
//import pacman.controllers.examples.po.NN.NeuralNetwork;
//import pacman.controllers.examples.po.NN.Neuron;
//
//import java.util.ArrayList;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class NeatTest {
//
//    @Test
//    void compatibilityTest() {
//
//    }
//
//    @Test
//    void mutation_1to1_Test() {
//        // Create a NeuralNetwork object
//        NeuralNetwork n = new NeuralNetwork();
//
//        // Create input
//        ArrayList<Double> input = new ArrayList<>();
//        input.add(100.0);
//
//        // Create design
//        int[] design = {1, 1};
//
//        // Create a new network
//        n = NeuralNetwork.generateRandomNetwork(input, design);
//        n.train();
//
//        // Create a NEAT object
//        Neat NEAT = new Neat();
//
//        // Create a mutated NeuralNetwork from the NEAT object
//        NeuralNetwork newNetwork = NEAT.mutate(n);
//
//        /* Check which neuron has changed (if any) */
//        // Get first neuron in each network
//        Neuron firstNeuron_n1 = n.getLayers().get(0).getNeurons().get(0);
//        Neuron firstNeuron_n2 = newNetwork.getLayers().get(0).getNeurons().get(0);
//        // Get second neuron in each network
//        Neuron secondNeuron_n1 = n.getLayers().get(1).getNeurons().get(0);
//        Neuron secondNeuron_n2 = n.getLayers().get(1).getNeurons().get(0);
//
//
//        boolean isFirstSame = false;
//        boolean isSecondSame = false;
//        // Compare the weights in the first neurons
//        for (int i = 0; i < firstNeuron_n1.getSynapticWeights().size(); i++) {
//            Double n1_weight = firstNeuron_n1.getSynapticWeights().get(i);
//            Double n2_weight = firstNeuron_n2.getSynapticWeights().get(i);
//            if (!n1_weight.equals(n2_weight)) {
//                isFirstSame = true;
//            }
//        }
//        // Compare the weights of the second neurons
//        for (int i = 0; i < secondNeuron_n1.getSynapticWeights().size(); i++) {
//            Double n1_weight = secondNeuron_n1.getSynapticWeights().get(i);
//            Double n2_weight = secondNeuron_n2.getSynapticWeights().get(i);
//            if (!n1_weight.equals(n2_weight)) {
//                isSecondSame = true;
//            }
//        }
//
//        assertEquals(false, (isFirstSame || isSecondSame));
//    }
//
//    @Test
//    void findConnectionTest() {
//
//    }
//
//    @Test
//    void connectionsUpdateTest() {
//
//    }
//
//    @Test
//    void similarConnectionsTest() {
//
//    }
//}
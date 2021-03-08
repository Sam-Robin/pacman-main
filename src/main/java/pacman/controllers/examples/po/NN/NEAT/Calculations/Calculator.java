package pacman.controllers.examples.po.NN.NEAT.Calculations;

import pacman.controllers.examples.po.NN.NEAT.ConnectionGene;
import pacman.controllers.examples.po.NN.NEAT.Genome;
import pacman.controllers.examples.po.NN.NEAT.Node;
import pacman.controllers.examples.po.NN.NEAT.NodeGene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Calculator {

    private ArrayList<Node> inputNodes, hiddenNodes, outputNodes;
    private Genome genome;

    public Calculator(Genome genome) {
        this.genome = genome;
        this.inputNodes = new ArrayList<>();
        this.hiddenNodes = new ArrayList<>();
        this.outputNodes = new ArrayList<>();

        ArrayList<NodeGene> nodes = genome.getNodes();
        ArrayList<ConnectionGene> connections = genome.getConnections();

        HashMap<Integer, Node> nodeHashMap = new HashMap<>();

        for (NodeGene n : nodes) {
            Node node = new Node(n.getX());
            nodeHashMap.put(n.getInnovationNumber(), node);

            if (n.getX() <= 0.1) {
                inputNodes.add(node);
            }
            else if (n.getX() >= 0.9) {
                outputNodes.add(node);
            }
            else {
                hiddenNodes.add(node);
            }
        }

        Collections.sort(hiddenNodes);

        for (ConnectionGene c : connections) {
            NodeGene fromNode = c.getFromNode();
            NodeGene toNode = c.getToNode();

            Node nodeFrom = nodeHashMap.get(fromNode.getInnovationNumber());
            Node nodeTo = nodeHashMap.get(toNode.getInnovationNumber());

            Connection con = new Connection(nodeFrom, nodeTo);
            con.setWeight(c.getWeight());
            con.setEnabled(c.isEnabled());

            nodeTo.getConnections().add(con);
        }
    }

    public double[] calculate(double... data) throws Exception {
        // Check that the length of data is equal to the number of input nodes
        if (data.length > inputNodes.size()) {
            throw new Exception("Too many inputs. Expected: " + inputNodes.size()
                    + "\t Actual: " + data.length);
        }
        else if (data.length < inputNodes.size()) {
            throw new Exception("Too few inputs. Expected: " + inputNodes.size()
                    + "\t Actual: " + data.length);
        }
        // Iterate through input nodes
        for (int i = 0; i < inputNodes.size(); i++) {
            inputNodes.get(i).setOutput(data[i]);
        }

        // Iterate through hidden nodes
        for (Node node : hiddenNodes) {
            node.calculate();
        }

        double[] output = new double[outputNodes.size()];
        for (int i = 0; i < outputNodes.size(); i++) {
            outputNodes.get(i).calculate();
            output[i] = outputNodes.get(i).getOutput();
        }

        //System.out.println(Arrays.toString(output));

        return output;
    }
}

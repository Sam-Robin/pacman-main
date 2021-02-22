package pacman.controllers.examples.po.NN.NEAT.Frame;

import pacman.controllers.examples.po.NN.NEAT.ConnectionGene;
import pacman.controllers.examples.po.NN.NEAT.NodeGene;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class NetworkPanel extends JPanel {
    private ArrayList<NodeGene> nodes;
    private ArrayList<ConnectionGene> connections;

    public NetworkPanel() {
        this.nodes = new ArrayList<>();
        this.connections = new ArrayList<>();
    }

    public NetworkPanel(ArrayList nodes, ArrayList connections) {
        this.nodes = nodes;
        this.connections = connections;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        // Draw nodes
        for (NodeGene node : nodes) {
            int x = (int) (node.getX() * getWidth());
            int y = (int) (node.getY() * getHeight());
            g.drawOval(x, y, 10, 10);
        }

        // Draw connections
        for (ConnectionGene connection : connections) {
            int xFrom = (int) (connection.getFromNode().getX() * getWidth());
            int yFrom = (int) (connection.getFromNode().getY() * getHeight());
            int xTo = (int) (connection.getToNode().getX() * getWidth());
            int yTo = (int) (connection.getToNode().getY() * getHeight());
            g.drawLine(xFrom, yFrom, xTo, yTo);
        }
    }

    public void addNode(NodeGene node) {
        nodes.add(node);
    }

    public void addConnection(ConnectionGene connection) {
        connections.add(connection);
    }
}

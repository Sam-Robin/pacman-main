package pacman.controllers.examples.po.NN.NEAT.Frame;

import pacman.controllers.examples.po.NN.NEAT.ConnectionGene;
import pacman.controllers.examples.po.NN.NEAT.Genome;
import pacman.controllers.examples.po.NN.NEAT.NodeGene;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class NetworkPanel extends JPanel {
    private Genome genome;
    private int networkNumber;
    private int nodeOffset = 50;

    public NetworkPanel() {
        this.genome = new Genome();
        this.networkNumber = 0;
        setVisible(true);
    }

    public NetworkPanel(Genome genome) {
        this.genome = genome;
        this.networkNumber = 0;
        setVisible(true);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.BLACK);
        g.setColor(Color.WHITE);

        // Draw nodes
        for (NodeGene node : genome.getNodes()) {
            int x = (int) (node.getX() * getWidth());
            int y = (int) (node.getY() * getHeight());
            g.drawOval(x, y - nodeOffset, 10, 10);
        }

        // Draw connections
        for (ConnectionGene connection : genome.getConnections()) {
            g.setColor(Color.GREEN);
            if (!connection.isEnabled()) {
                g.setColor(Color.RED);
            }
            int xFrom = (int) (connection.getFromNode().getX() * getWidth());
            int yFrom = (int) (connection.getFromNode().getY() * getHeight());
            int xTo = (int) (connection.getToNode().getX() * getWidth());
            int yTo = (int) (connection.getToNode().getY() * getHeight());
            g.drawLine(xFrom, yFrom - nodeOffset, xTo, yTo - nodeOffset);
        }
    }
    public void setGenome(Genome genome) {
        this.genome = genome;
        networkNumber++;
    }

    public int getNetworkNumber() {
        return networkNumber;
    }

    public void setNetworkNumber(int networkNumber) {
        this.networkNumber = networkNumber;
    }
}

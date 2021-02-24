package pacman.controllers.examples.po.NN.NEAT.Frame;

import pacman.controllers.examples.po.NN.NEAT.ConnectionGene;
import pacman.controllers.examples.po.NN.NEAT.NodeGene;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class NetworkFrame extends JFrame {

    private NetworkPanel networkPanel;

    public NetworkFrame() {
        super("Network Viewer");

        networkPanel = new NetworkPanel();
        add(networkPanel);

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        setVisible(true);
    }

    public NetworkFrame(NetworkPanel networkPanel) {
        super("Network Viewer");

        this.networkPanel = networkPanel;
        add(networkPanel);

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        setVisible(true);
    }

    public void update() {
        networkPanel.repaint();
    }
}

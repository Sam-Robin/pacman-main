package pacman.test;

import pacman.controllers.PacmanController;
import pacman.controllers.examples.po.*;
import pacman.controllers.examples.po.NN.NEAT.ConnectionGene;
import pacman.controllers.examples.po.NN.NEAT.Frame.NetworkFrame;
import pacman.controllers.examples.po.NN.NEAT.Genome;
import pacman.controllers.examples.po.NN.NEAT.Neat;
import pacman.controllers.examples.po.NN.NEAT.NodeGene;
import pacman.controllers.examples.po.NN.NeuralNetwork;
import pacman.controllers.examples.po.NN.Neuron;
import pacman.game.Constants;
import pacman.game.Game;
import pacman.game.GameView;
import pacman.game.comms.BasicMessenger;
import pacman.game.internal.POType;
import pacman.game.util.Serializer;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class MyNeatTest {

    /**
     * Show 1 view for each ghost, in PO mode
     *
     * @param args Not used
     */
    public static void main(String[] args) throws IOException {
        Game g = new Game(100, 0, new BasicMessenger(), POType.LOS, 175);
        System.out.println(g.isGamePo());
//        GameView[] ghostViews = new GameView[]{
//                new GameView(g, false).showGame(),
//                new GameView(g, false).showGame(),
//                new GameView(g, false).showGame(),
//                new GameView(g, false).showGame()
//        };
//        for (Constants.GHOST ghost : Constants.GHOST.values()) {
//            ghostViews[ghost.ordinal()].setPO(true, ghost);
//        }

        //GameView gView = new GameView(g).showGame();
        PacmanController pacman = new POPacMan();
        NNGhosts ghosts = new NNGhosts();
        ghosts.setupNNs(g);

        // Add the network connections to the NEAT object
        Neat neat = new Neat(5, 4, 100);
        Genome genome = neat.emptyGenome();
        System.out.println(genome.getNodes().size());

        NetworkFrame networkFrame = new NetworkFrame();

        NodeGene n1 = new NodeGene(0);
        n1.setX(0.2);
        n1.setY(0.5);
        networkFrame.addNode(n1);

        NodeGene n2 = new NodeGene(1);
        n2.setX(0.8);
        n2.setY(0.5);
        networkFrame.addNode(n2);
        networkFrame.update();

        NodeGene n3 = new NodeGene(1);
        n3.setX(0.4);
        n3.setY(0.7);
        networkFrame.addNode(n3);
        networkFrame.update();

        ConnectionGene con1 = new ConnectionGene(n1, n2);
        networkFrame.addConnection(con1);
        networkFrame.update();

        ConnectionGene con2 = new ConnectionGene(n3, n2);
        networkFrame.addConnection(con2);
        networkFrame.update();
    }
}

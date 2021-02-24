package pacman.test;

import pacman.controllers.PacmanController;
import pacman.controllers.examples.po.*;
import pacman.controllers.examples.po.NN.NEAT.Frame.NetworkFrame;
import pacman.controllers.examples.po.NN.NEAT.Frame.NetworkPanel;
import pacman.controllers.examples.po.NN.NEAT.Genome;
import pacman.controllers.examples.po.NN.NEAT.Neat;
import pacman.game.Constants;
import pacman.game.Game;
import pacman.game.GameView;
import pacman.game.comms.BasicMessenger;
import pacman.game.internal.POType;

import java.io.IOException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class MyNeatTest {

    private static HashMap<Genome, Integer> genomeScores;

    /**
     * Show 1 view for each ghost, in PO mode
     *
     * @param args Not used
     * @return
     */
    public static void main(String[] args) throws IOException {
        genomeScores = new HashMap<>();
        Game g = new Game(100, 0, new BasicMessenger(), POType.LOS, 175);
        System.out.println(g.isGamePo());
        GameView[] ghostViews = createGhostViews(g);

        GameView gView = new GameView(g).showGame();

        // Add the network connections to the NEAT object
        Neat neat = new Neat(5, 4, 100);
        Genome genome = neat.emptyGenome();
        System.out.println(genome.getNodes().size());

        NetworkPanel panel = new NetworkPanel(genome);
        NetworkFrame networkFrame = new NetworkFrame(panel);
        networkFrame.update();

        PacmanController pacman = new POPacMan();
        NNGhosts ghosts = new NNGhosts(genome);
        genome.update();
        ghosts.runGhosts(g);

        int exp = 10;
        for (int i = 0; i < exp; i++) {
            while (!g.gameOver()) {
                try {
                    Thread.sleep(40);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Constants.MOVE pacmanMove =
                        null;
                try {
                    pacmanMove = pacman.getMove(g.copy(5), 40);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                EnumMap<Constants.GHOST, Constants.MOVE> ghostMoves =
                        ghosts.getMove(g.copy(), -1);

                g.advanceGame(pacmanMove, ghostMoves);

                for (Map.Entry<Constants.GHOST, Constants.MOVE> entry : ghostMoves.entrySet()) {
                    System.out.println(entry.getKey().toString() + " moving " + entry.getValue().toString());
                }

                // Display every ghost's view
                for (GameView view : ghostViews) {
                    view.paintImmediately(view.getBounds());
                }

                // Update pacman view
                gView.paintImmediately(gView.getBounds());
            }

            // Close the previous ghost views
            for (GameView view : ghostViews) {
                view.closeGame();
            }

            // Close the previous pacman view
            gView.closeGame();

            // Store the genome and its score
            genomeScores.put(genome, g.getScore());

            // Create a new genome
            genome = neat.emptyGenome();
            ghosts = new NNGhosts(genome);
            genome.update();

            // Create a new game
            g = new Game(1000, 0, new BasicMessenger(), POType.LOS, 175);
            ghosts.runGhosts(g);

            // Update the network frame with the new genome
            panel.setGenome(genome);
            networkFrame.update();

            // Create new ghost views for the current game
            ghostViews = createGhostViews(g);

            // Create a new pacman view
            gView = new GameView(g).showGame();
        }


//        NodeGene n1 = new NodeGene(0);
//        n1.setX(0.2);
//        n1.setY(0.5);
//        networkFrame.addNode(n1);
//
//        NodeGene n2 = new NodeGene(1);
//        n2.setX(0.8);
//        n2.setY(0.5);
//        networkFrame.addNode(n2);
//        networkFrame.update();
//
//        NodeGene n3 = new NodeGene(1);
//        n3.setX(0.4);
//        n3.setY(0.7);
//        networkFrame.addNode(n3);
//        networkFrame.update();
//
//        ConnectionGene con1 = new ConnectionGene(n1, n2);
//        networkFrame.addConnection(con1);
//        networkFrame.update();
//
//        ConnectionGene con2 = new ConnectionGene(n3, n2);
//        networkFrame.addConnection(con2);
//        networkFrame.update();
    }

    private static GameView[] createGhostViews(Game g) {
        GameView[] views = new GameView[]{
                new GameView(g, false).showGame(),
                new GameView(g, false).showGame(),
                new GameView(g, false).showGame(),
                new GameView(g, false).showGame()
        };

        for (Constants.GHOST ghost : Constants.GHOST.values()) {
            // Make the views PO
            views[ghost.ordinal()].setPO(true, ghost);
        }


        return views;
    }
}

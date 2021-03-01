package pacman.test;

import pacman.controllers.PacmanController;
import pacman.controllers.examples.po.CentralisedGhosts;
import pacman.controllers.examples.po.NN.NEAT.Frame.NetworkFrame;
import pacman.controllers.examples.po.NN.NEAT.Frame.NetworkPanel;
import pacman.controllers.examples.po.NN.NEAT.Genome;
import pacman.controllers.examples.po.NN.NEAT.Neat;
import pacman.controllers.examples.po.NNGhosts;
import pacman.controllers.examples.po.POPacMan;
import pacman.game.Constants;
import pacman.game.Game;
import pacman.game.GameView;
import pacman.game.comms.BasicMessenger;
import pacman.game.internal.POType;

import java.io.IOException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CentralisedNeatTest {

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
        Neat neat = new Neat(11, 16, 100);
        Genome genome = neat.emptyGenome();
        System.out.println(genome.getNodes().size());

        NetworkPanel panel = new NetworkPanel(genome);
        NetworkFrame networkFrame = new NetworkFrame(panel);
        networkFrame.update();

        PacmanController pacman = new POPacMan();
        CentralisedGhosts ghosts = new CentralisedGhosts(genome);
        genome.update();
        //ghosts.runGhosts(g);

        int exp = 3;
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
                //EnumMap<Constants.GHOST, Constants.MOVE> ghostMoves =
                        //ghosts.getMove(g.copy(), -1);

                //g.advanceGame(pacmanMove, ghostMoves);

//                for (Map.Entry<Constants.GHOST, Constants.MOVE> entry : ghostMoves.entrySet()) {
//                    System.out.println(entry.getKey().toString() + " moving " + entry.getValue().toString());
//                }

                try {
                    EnumMap<Constants.GHOST, Constants.MOVE> ghostMoves =
                            ghosts.getMove(g.copy(), -1);
                    g.advanceGame(pacmanMove, ghostMoves);

                    for (Map.Entry<Constants.GHOST, Constants.MOVE> entry : ghostMoves.entrySet()) {
                    System.out.println(entry.getKey().toString() + " moving " + entry.getValue().toString());
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
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

            // If this was the second experiment...
            if (i == 1) {
                // Combine the previous two genomes
                Iterator iterator = genomeScores.keySet().iterator();
                Genome g1 = (Genome) iterator.next();
                Genome g2 = (Genome) iterator.next();
                genome = Genome.crossover(g1, g2);
            }
            // Otherwise...
            else {
                // Create a new genome
                genome = neat.emptyGenome();
            }
//            ghosts = new NNGhosts(genome);
            genome.update();


            // Create a new game
            g = new Game(1000, 0, new BasicMessenger(), POType.LOS, 175);
//           ghosts.runGhosts(g);

            // Update the network frame with the new genome
            panel.setGenome(genome);
            networkFrame.update();

            // Create new ghost views for the current game
            ghostViews = createGhostViews(g);

            // Create a new pacman view
            gView = new GameView(g).showGame();
        }
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

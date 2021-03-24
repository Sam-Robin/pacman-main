package pacman.test;

import com.opencsv.CSVWriter;
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

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

/**
 * A 1+1 genetic algorithm for playing Ms. Pacman centralised.
 */
public class Simple_ExploreCentralisedNeatTest {

    private enum EXPERIMENT_TYPE {
        LEFT_WALL,
        RIGHT_WALL,
        BOTTOM_WALL,
        TOP_WALL,
        MINIMISE_DISTANCE,
        EXPLORE
    }

    public static void main(String[] args) throws Exception {
        Neat neat = new Neat(9, 16, 1);
        Genome genome = neat.emptyGenome(500);
        genome.update();
        Genome bestGenome = genome;
        ArrayList<Integer> nodesVisited = new ArrayList<>();

        Game g = new Game(100, 0, new BasicMessenger(), POType.LOS, 175);

        NetworkPanel panel = new NetworkPanel(genome);
        NetworkFrame frame = new NetworkFrame(panel);
        frame.update();

        POPacMan pacman = new POPacMan();


        for (int i = 0; i < 1000; i++) {
            Genome g1 = neat.emptyGenome(500);
            CentralisedGhosts ghosts = new CentralisedGhosts(g1);
            g1.update();
            panel.setGenome(g1);
            frame.update();

            // Evaluate a genome over 30 games
            ArrayList<Integer> scores = new ArrayList<>(30);
            for (int n = 0; n < 30; n++) {
                g = new Game(100, 0, new BasicMessenger(), POType.LOS, 175);
                scores.add(playNonViewableGame(g1, g, frame));
            }

            // Take the average of all scores
            int sum = 0;
            for (Integer score : scores) {
                sum += score;
            }


            g1.setScore(sum / 30);

            if (g1.getScore() > bestGenome.getScore()) {
                bestGenome = g1;
            }

            g = new Game(100, 0, new BasicMessenger(), POType.LOS, 175);
            nodesVisited.clear();

            // Save the experiment to a file
            try {
                writeExperimentToCSV(scores, i, "centralised_simple.csv");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Now all the experiments are done...

        // Show off the best genome
        playViewableGame(bestGenome, g, frame);
    }

    private static int playNonViewableGame(Genome genome, Game g, NetworkFrame frame) {
        POPacMan pacman = new POPacMan();
        CentralisedGhosts ghosts = new CentralisedGhosts(genome);
        ArrayList<Integer> nodesVisited = new ArrayList<>();

        frame.update();

        while(!g.gameOver()) {
            Constants.MOVE pacmanMove = pacman.getMove(g.copy(5), 40);

            EnumMap<Constants.GHOST, Constants.MOVE> ghostMoves = ghosts.getMove(g.copy(), -1);
            g.advanceGame(pacmanMove, ghostMoves);

            // Add the unique nodes visited to the nodesVisited ArrayList
            for (Map.Entry<Constants.GHOST, Constants.MOVE> entry : ghostMoves.entrySet()) {
                int index = g.getGhostCurrentNodeIndex(entry.getKey());
                if (!nodesVisited.contains(index)) {
                    nodesVisited.add(index);
                }
            }
        }

        return nodesVisited.size();
    }

    private static void playViewableGame(Genome genome, Game g, NetworkFrame frame) {
        GameView[] ghostViews = createGhostViews(g);
        GameView pacmanView = new GameView(g).showGame();
        POPacMan pacman = new POPacMan();
        CentralisedGhosts ghosts = new CentralisedGhosts(genome);

        frame.getPanel().setGenome(genome);
        frame.update();


        while (!g.gameOver()) {
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

            //Display every ghost's view
            for (GameView view : ghostViews) {
                view.paintImmediately(view.getBounds());
            }

            //Update pacman view
            pacmanView.paintImmediately(pacmanView.getBounds());
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

    private static void writeExperimentToCSV(ArrayList<Integer> scores,
                                             int generation, String name) throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter(name, true));

        for (Integer score : scores) {
            writer.writeNext(new String[] { String.valueOf(generation),
                    String.valueOf(score) });
        }

        writer.close();
    }


    private static double costFunction(EXPERIMENT_TYPE type, ArrayList<Integer> nodesVisited) {
        if (type == EXPERIMENT_TYPE.EXPLORE) {
            return nodesVisited.size();
        }
        else return 0.0;
    }
}

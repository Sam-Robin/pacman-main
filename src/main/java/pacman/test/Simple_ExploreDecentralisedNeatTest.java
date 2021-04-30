package pacman.test;

import com.opencsv.CSVWriter;
import pacman.controllers.examples.po.CentralisedGhosts;
import pacman.controllers.examples.po.NN.NEAT.Frame.NetworkFrame;
import pacman.controllers.examples.po.NN.NEAT.Frame.NetworkPanel;
import pacman.controllers.examples.po.NN.NEAT.Genome;
import pacman.controllers.examples.po.NN.NEAT.Neat;
import pacman.controllers.examples.po.NNGhost;
import pacman.controllers.examples.po.NNGhosts;
import pacman.controllers.examples.po.POPacMan;
import pacman.game.Constants;
import pacman.game.Game;
import pacman.game.GameView;
import pacman.game.comms.BasicMessenger;
import pacman.game.internal.POType;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public class Simple_ExploreDecentralisedNeatTest {

    private enum EXPERIMENT_TYPE {
        LEFT_WALL,
        RIGHT_WALL,
        BOTTOM_WALL,
        TOP_WALL,
        MINIMISE_DISTANCE,
        EXPLORE
    }

    public static void main(String[] args) throws Exception {
        Neat neat = new Neat(4, 4, 100);
        Genome genome = neat.emptyGenome(50);
        genome.update();
        Genome bestGenome = genome;
        ArrayList<Integer> nodesVisited = new ArrayList<>();

        Game g = new Game(100, 0, new BasicMessenger(), POType.LOS, 175);

        NetworkPanel panel = new NetworkPanel(genome);
        NetworkFrame frame = new NetworkFrame(panel);
        frame.update();

        POPacMan pacman = new POPacMan();

        ArrayList<Genome> population = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            population.add(neat.emptyGenome(50));
        }

        for (int i = 0; i < 1000; i++) {
            for (Genome g1 : population) {
                NNGhosts ghosts = new NNGhosts(genome);
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
                    writeExperimentToCSV((int) g1.getScore(), i,
                            g1.findInputNodes().size(), g1.findHiddenNodes().size(),
                            g1.calculateInputVectorSum(), g1.calculateHiddenVectorSum(),
                            g1.calculateInputLayerSprawl(), g1.calculateHiddenLayerSprawl(),
                            g1.calculateInputLayerReach(), g1.calculateHiddenLayerReach(),
                            "decentralised_explore_vs_FAKE.csv");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            Collections.sort(population);

            for (Genome g1 : population) {
                g1.mutate(1);
            }

             //population = crossoverPopulation(population, 10);
        }

        // Now all the experiments are done...

        System.in.read();

        // Show off the best genome
        playViewableGame(bestGenome, g, frame);
    }

    private static ArrayList<Genome> crossoverPopulation(ArrayList<Genome> population, int size) {
        ArrayList<Genome> newPopulation = new ArrayList<>();
        Genome parent1, parent2;

        // Iterate through the population
        for (int i = 0; i < size; i++) {
            // Get the parent
            parent1 = population.get(i);

            // Choose the next-best genome
            try {
                // Try and get the (i + 1)'th genome
                parent2 = population.get(i + 1);
            }
            catch (Exception e) {
                // This must be the last genome in the list...
                // So get the best genome
                parent2 = population.get(0);
            }

            // Add the child to the new population
            newPopulation.add(Genome.crossover(parent1, parent2));
        }

        return newPopulation;
    }

    private static void writeExperimentToCSV(int score,
                                             int generation,
                                             int inputNodes,
                                             int hiddenNodes,
                                             double inputWeightSum,
                                             double hiddenWeightSum,
                                             BigDecimal inputSprawl,
                                             BigDecimal hiddenSprawl,
                                             BigDecimal inputReach,
                                             BigDecimal hiddenReach, String name) throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter(name, true));

        writer.writeNext(new String[] {
                String.valueOf(generation), String.valueOf(score),
                String.valueOf(inputNodes), String.valueOf(hiddenNodes),
                String.valueOf(inputWeightSum), String.valueOf(hiddenWeightSum),
                inputSprawl.toString(), hiddenSprawl.toString(),
                inputReach.toString(), hiddenReach.toString()});

        writer.close();
    }

    private static int playNonViewableGame(Genome genome, Game g, NetworkFrame frame) {
        POPacMan pacman = new POPacMan();
        NNGhosts ghosts = new NNGhosts(genome);
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
        NNGhosts ghosts = new NNGhosts(genome);

        frame.getPanel().setGenome(genome);
        frame.update();


        while (!g.gameOver()) {
            Constants.MOVE pacmanMove =
                    null;
            try {
                pacmanMove = pacman.getMove(g.copy(5), 40);
                Thread.sleep(40);
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

    private static double costFunction(EXPERIMENT_TYPE type, ArrayList<Integer> nodesVisited) {
        if (type == EXPERIMENT_TYPE.EXPLORE) {
            return nodesVisited.size();
        }
        else return 0.0;
    }
}

package pacman.test;

import com.opencsv.CSVWriter;
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

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class DecentralisedNeatTest {

    private static HashMap<Genome, Integer> genomeScores;
    private static ArrayList<ArrayList<Genome>> experiments;

    /**
     * Show 1 view for each ghost, in PO mode
     *
     * @param args Not used
     * @return
     */
    public static void main(String[] args) throws IOException {
        genomeScores = new HashMap<>();
        experiments = new ArrayList<>();
        Game g = new Game(100, 0, new BasicMessenger(), POType.LOS, 175);
        System.out.println(g.isGamePo());
        GameView[] ghostViews = createGhostViews(g);

        GameView gView = new GameView(g).showGame();

        // Create an initial population
        int populationSize = 20;
        Neat neat = new Neat(4, 4, 100);
        ArrayList<Genome> population = createInitialPopulation(neat, populationSize);

        NetworkPanel panel = new NetworkPanel(null);
        NetworkFrame networkFrame = new NetworkFrame(panel);
        networkFrame.update();

        // Create a PO pacman controller
        PacmanController pacman = new POPacMan();

        // Run 10 candidate solutions
        // Create 100 generations
        for (int gen = 0; gen < 100; gen++) {
            // Iterate though every candidate in the population
            for (Genome genome : population) {

                // Add the candidate solution's genome to the panel
                panel.setGenome(genome);
                networkFrame.update();

                // Setup the ghosts with the genome
                NNGhosts ghosts = new NNGhosts(genome);
                genome.update();
                ghosts.runGhosts(g);

                // Set the start time of this genome's evaluation
                long startTime = System.nanoTime();
                long endTime = 0;
                final long maxDuration = 600000000;
                long duration = 0;

                // While the game is running...
                while (!g.gameOver() &&
                (duration / 100) < maxDuration) {
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

                    endTime = System.nanoTime();
                    duration = endTime - startTime;
                }

                // Now the game is over...

                // Close the previous ghost views
                for (GameView view : ghostViews) {
                    view.closeGame();
                }

                // Close the previous pacman view
                gView.closeGame();

                // Store the genome and its score
                genomeScores.put(genome, g.getScore());
                genome.setScore(g.getScore());

                /*
                    OLD CROSSOVER
                 */
                // If this was the second experiment...
//                if (i == 1) {
//                    // Combine the previous two genomes
//                    Iterator iterator = genomeScores.keySet().iterator();
//                    Genome g1 = (Genome) iterator.next();
//                    Genome g2 = (Genome) iterator.next();
//                    genome = Genome.crossover(g1, g2);
//                }
//                // Otherwise...
//                else {
//                    // Create a new genome
//                    genome = neat.emptyGenome();
//                }
//                ghosts = new NNGhosts(genome);
//                genome.update();


                // Create a new game
                g = new Game(1000, 0, new BasicMessenger(), POType.LOS, 175);
                ghosts.runGhosts(g);

                // Create new ghost views for the current game
                ghostViews = createGhostViews(g);

                // Create a new pacman view
                gView = new GameView(g).showGame();
            }

            // Now that the population has been evaluated...

            // Save the population to a csv file
            try {
                writeExperimentToCSV(population, gen, "experiment1");
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            // Now that a batch of candidates has been evaluated...

            // Add the latest generation to the experiments ArrayList
            experiments.add(population);

            // Order the candidates by their score
            LinkedHashMap<Genome, Integer> orderedPopulation = sortHashMapByValues(genomeScores);

            // Crossover the candidates
            population = crossoverPopulation(new ArrayList<>(orderedPopulation.keySet()), populationSize);
        }

        // Now all the experiments are done...
    }

    private static ArrayList<Genome> createInitialPopulation(Neat neat, int size) {
        ArrayList<Genome> population = new ArrayList<>(size);

        Genome first = neat.emptyGenome();
        population.add(first);

        for (int i = 0; i < (size / 2) - 1; i++) {
            Genome next = first.deepCopy();
            next.mutate(10);
            population.add(next);
        }

        Genome second = neat.emptyGenome();
        population.add(second);

        for (int i = 0; i < (size / 2) - 1; i++) {
            Genome next = second.deepCopy();
            next.mutate(10);
            population.add(next);
        }

        return population;
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

    private static void writeExperimentToCSV(ArrayList<Genome> experiments,
                                             int generation, String name) throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter(name, true));

        for (Genome genome : experiments) {
            writer.writeNext(new String[] { String.valueOf(generation),
                    String.valueOf(genome.getScore())} );
        }

        writer.close();
    }

    private static LinkedHashMap<Genome, Integer> sortHashMapByValues(
            HashMap<Genome, Integer> passedMap) {
        List<Genome> mapKeys = new ArrayList<>(passedMap.keySet());
        List<Integer> mapValues = new ArrayList<>(passedMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);

        LinkedHashMap<Genome, Integer> sortedMap =
                new LinkedHashMap<>();

        Iterator<Integer> valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            Integer val = valueIt.next();
            Iterator<Genome> keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                Genome key = keyIt.next();
                Integer comp1 = passedMap.get(key);
                Integer comp2 = val;

                if (comp1.equals(comp2)) {
                    keyIt.remove();
                    sortedMap.put(key, val);
                    break;
                }
            }
        }
        return sortedMap;
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

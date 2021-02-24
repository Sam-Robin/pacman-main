package pacman.test;

import com.opencsv.CSVWriter;
import pacman.controllers.PacmanController;
import pacman.controllers.examples.po.*;
import pacman.controllers.examples.po.NN.NEAT.Genome;
import pacman.controllers.examples.po.NN.NEAT.Neat;
import pacman.controllers.examples.po.NN.NeuralNetwork;
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
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExperimentExecutor {

    /**
     * Show 1 view for each ghost, in PO mode
     *
     * @param args Not used
     */
    public static void main(String[] args) throws IOException {
        Game g = new Game(1000, 0, new BasicMessenger(), POType.LOS, 175);
        System.out.println(g.isGamePo());

        //GameView gView = new GameView(g).showGame();
        PacmanController pacman = new POPacMan();
        Neat neat = new Neat(5, 4, 100);
        Genome genome = neat.emptyGenome();
        NNGhosts ghosts = new NNGhosts(genome);
        genome.update();
        ghosts.runGhosts(g);

        // Setup list of experiments
        ArrayList<ExperimentData> experiments = new ArrayList<>();

        // Run exp experiments
        int exp = 2;
        for (int i = 0; i < exp; i++) {
            System.out.println("Running experiment " + (i + 1));
            // Run the game
            while(!g.gameOver()) {
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

//                System.out.println("Experiment: " + i + "\tLevel: " + g.getCurrentLevel() +
//                        "\tTime: " + g.getTotalTime());
            }

            // Add data about game to experiments
            experiments.add(new ExperimentData(genome, g.getScore(), i, g.getTotalTime()));


            // Create a new game once the previous one has finished
            g = new Game(1000, 0, new BasicMessenger(), POType.LOS, 175);
            // Create a new genome
            genome = neat.emptyGenome();
            // Create a new set of NNGhosts
            ghosts = new NNGhosts(genome);
            genome.update();
            ghosts.runGhosts(g);

            System.out.println("Experiment " + (i + 1) + " complete");
        }

        // Set the calculators in the genomes to null to avoid circular references
        for (ExperimentData experimentData : experiments) {
            Genome gen = experimentData.getGenome();
            gen.setCalculator(null);
        }

        // Save the genomes to a file
        Serializer serializer = new Serializer();
        FileWriter experimentWriter = new FileWriter("genomes.txt");
        experimentWriter.write(serializer.serialize(experiments));
        experimentWriter.close();

        // Save the experimental data to a file
        String[][] data = new String[experiments.size()][3];
        for (int i = 0; i < experiments.size(); i++) {
            ExperimentData experimentData = experiments.get(i);
            data[i] = new String[] { String.valueOf(experimentData.getGeneration()),
                    String.valueOf(experimentData.getScore()),
                    String.valueOf(experimentData.getTimeTaken()) };
        }

        CSVWriter writer = new CSVWriter(new FileWriter("experimental_data.csv"));
        for (String[] array : data) {
            writer.writeNext(array);
        }
        writer.close();
    }
}

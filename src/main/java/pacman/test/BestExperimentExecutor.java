package pacman.test;

import pacman.controllers.PacmanController;
import pacman.controllers.examples.po.NN.NeuralNetwork;
import pacman.controllers.examples.po.NNGhosts;
import pacman.controllers.examples.po.POPacMan;
import pacman.game.Constants;
import pacman.game.Game;
import pacman.game.GameView;
import pacman.game.comms.BasicMessenger;
import pacman.game.internal.POType;
import pacman.game.util.Serializer;

import javax.crypto.SealedObject;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.ServerError;
import java.util.*;

public class BestExperimentExecutor {
    /**
     * Show 1 view for each ghost, in PO mode
     *
     * @param args Not used
     */
    public static void main(String[] args) throws IOException {
        Game g = new Game(100, 0, new BasicMessenger(), POType.LOS, 175);
        System.out.println(g.isGamePo());
        GameView[] ghostViews = new GameView[]{
                new GameView(g, false).showGame(),
                new GameView(g, false).showGame(),
                new GameView(g, false).showGame(),
                new GameView(g, false).showGame()
        };
        for (Constants.GHOST ghost : Constants.GHOST.values()) {
            ghostViews[ghost.ordinal()].setPO(true, ghost);
        }

        // Read in experiments from file
        Serializer serializer = new Serializer();
        String content = new String(Files.readAllBytes(Paths.get("sample_experiments.txt")));
        ExperimentData[] experiments = (ExperimentData[]) serializer.deserialize(content, ExperimentData[].class);
        List<ExperimentData> experimentsList = Arrays.asList(experiments);
        // Sort the experiments so the one with the lowest score is at the top
        Collections.sort(experimentsList);
        // Take the top experiment as the best one
        ExperimentData bestExperiment = experimentsList.get(0);
        // Get the networks from the experiment
        HashMap<Constants.GHOST, NeuralNetwork> networks = bestExperiment.getNetworks();

        //GameView gView = new GameView(g).showGame();
        PacmanController pacman = new POPacMan();
        NNGhosts ghosts = new NNGhosts();
        try {
            ghosts.setupNNs_Predefined(networks);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // Run the game
        while(!g.gameOver()) {
            try {
                Thread.sleep(40);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Constants.MOVE pacmanMove =
                    pacman.getMove(g.copy(5), 40);
            EnumMap<Constants.GHOST, Constants.MOVE> ghostMoves =
                    ghosts.getMove(g.copy(), -1);

            g.advanceGame(pacmanMove, ghostMoves);

            // Display every ghost's view
            for (GameView view : ghostViews) {
                view.paintImmediately(view.getBounds());
            }
            System.out.println("Level: " + g.getCurrentLevel() + "\tTime: " + g.getTotalTime());
        }
    }
}

package pacman.test;

import pacman.controllers.PacmanController;
import pacman.controllers.examples.po.*;
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

public class ExperimentExecutor {

    /**
     * Show 1 view for each ghost, in PO mode
     *
     * @param args Not used
     */
    public static void main(String[] args) throws IOException {
        Game g = new Game(1000, 0, new BasicMessenger(), POType.LOS, 175);
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

        // Setup list of experiments
        ArrayList<ExperimentData> experiments = new ArrayList<>();

        // Run exp experiments
        int exp = 10;
        for (int i = 0; i < exp; i++) {
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

                System.out.println("Experiment: " + i + "\tLevel: " + g.getCurrentLevel() +
                        "\tTime: " + g.getTotalTime());
            }

            // Add data about game to experiments
            experiments.add(new ExperimentData(ghosts.getGhostsNetworks(), i, g.getScore(), g.getTotalTime()));


            // Create a new game once the previous one has finished
            g = new Game(1000, 0, new BasicMessenger(), POType.LOS, 175);
            // Create a new set of NNGhosts
            ghosts = new NNGhosts();
            ghosts.setupNNs(g);
        }


        HashMap<Constants.GHOST, NeuralNetwork> map = ghosts.getGhostsNetworks();

        Serializer serializer = new Serializer();
        NeuralNetwork map1 = map.entrySet().iterator().next().getValue();
        String json = serializer.serialize(map1);
        System.out.println(json.length());

        FileWriter fileWriter = new FileWriter("map1.txt");
        fileWriter.write(json);
        fileWriter.close();

        // Save the experiments to a file
        FileWriter experimentWriter = new FileWriter("experiments.txt");
        experimentWriter.write(serializer.serialize(experiments));
        experimentWriter.close();
    }
}

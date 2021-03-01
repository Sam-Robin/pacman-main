package pacman.controllers.examples.po;

import pacman.controllers.CentralisedGhostController;
import pacman.controllers.examples.po.NN.NEAT.Genome;
import pacman.controllers.examples.po.NN.NeuralNetwork;
import pacman.game.Constants;
import pacman.game.Game;

import java.util.EnumMap;
import java.util.Iterator;

public class CentralisedGhosts extends CentralisedGhostController {

    private Genome genome;
    private double[] inputs;
    private int pacmanLastSeen;

    public CentralisedGhosts(Genome genome) {
        super(true, Constants.GHOST.BLINKY, new EnumMap<>(Constants.GHOST.class));
        controllers.put(Constants.GHOST.BLINKY, new CentralisedGhost(Constants.GHOST.BLINKY, genome));
        controllers.put(Constants.GHOST.INKY, new CentralisedGhost(Constants.GHOST.INKY, genome));
        controllers.put(Constants.GHOST.PINKY, new CentralisedGhost(Constants.GHOST.PINKY, genome));
        controllers.put(Constants.GHOST.SUE, new CentralisedGhost(Constants.GHOST.SUE, genome));
        this.genome = genome;
        this.inputs = new double[11];
        this.pacmanLastSeen = -1;
    }

    /**
     * Updates the neural network with the latest game information
     * @param game
     */
    public void updateNN(Game game) {
        int[] ghostLocations = new int[4];
        int[] edibleTimes = new int[4];
        Iterator iterator = controllers.keySet().iterator();
        int i = 0;
        while (iterator.hasNext()) {
            Constants.GHOST g = (Constants.GHOST) iterator.next();
            // Get each ghost's location
            ghostLocations[i] = game.getGhostCurrentNodeIndex(g);
            // Get each ghost's edible time
            edibleTimes[i] = game.getGhostEdibleTime(g);
            // Update where pacman was last seen
            if (game.getGhostCurrentNodeIndex(g) > -1) {
                pacmanLastSeen = game.getGhostCurrentNodeIndex(g);
            }

            i++;
        }

        // Get pacman's score
        int score = game.getScore();
        int time = game.getTotalTime();

        // Iterate through the arrays of data
        for (i = 0; i < inputs.length; i++) {
            // Add the ghost locations
            if (i <= 0 && i < 4) {
                for (int c = 0; c < 4; c++) {
                    inputs[i] = ghostLocations[c];
                }
            }
            else if (i >= 4 && i < 8) {
                // Add the edible times
                for (int c = 0; c < 4; c++) {
                    inputs[i] = edibleTimes[c];
                }
            }
        }
        // Add pacman's last known location
        inputs[8] = pacmanLastSeen;
        inputs[9] = score;          // Add the score
        inputs[10] = time;          // Add the time
    }

    @Override
    public EnumMap<Constants.GHOST, Constants.MOVE> getMove(Game game, long timeDue) {
        // Clear the previous moves
        myMoves.clear();
        // Update the neural network with the latest game state
        updateNN(game);

        // Get the next node to move to for each ghost
        try {
            double[] outputs = genome.calculate(inputs);
            // Split the outputs into smaller arrays for each ghost
            double[] ghost1_moves = new double[4];
            double[] ghost2_moves = new double[4];
            double[] ghost3_moves = new double[4];
            double[] ghost4_moves = new double[4];
            for (int i = 0; i < 4; i++) {
                ghost1_moves[i] = outputs[i];
            }
            for (int i = 4; i < 8; i++) {
                ghost2_moves[i - 4] = outputs[i];
            }
            for (int i = 8; i < 12; i++) {
                ghost3_moves[i - 8] = outputs[i];
            }
            for (int i = 12; i < 16; i++) {
                ghost4_moves[i - 12] = outputs[i];
            }

            // Get the index of the largest value of each smaller array
            int ghost1_largest = returnIndexOfLargestValue(ghost1_moves);
            int ghost2_largest = returnIndexOfLargestValue(ghost2_moves);
            int ghost3_largest = returnIndexOfLargestValue(ghost3_moves);
            int ghost4_largest = returnIndexOfLargestValue(ghost4_moves);
            int[] moveIndexes = new int[] { ghost1_largest, ghost2_largest,
            ghost3_largest, ghost4_largest };

            // Add the moves for each ghost
            Iterator iterator = controllers.keySet().iterator();
            int i = 0;
            while (iterator.hasNext()) {
                Constants.GHOST g = (Constants.GHOST) iterator.next();
                myMoves.put(g, returnMoveFromIndex(moveIndexes[i]));
                i++;
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return myMoves;
    }

    private int returnIndexOfLargestValue(double[] arr) {
        int largest = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > arr[largest]) {
                largest = i;
            }
        }

        return largest;
    }

    private Constants.MOVE returnMoveFromIndex(int index) {
        if (index == 0) {
            return Constants.MOVE.UP;
        }
        else if (index == 1) {
            return Constants.MOVE.DOWN;
        }
        else if (index == 2) {
            return Constants.MOVE.LEFT;
        }
        else if (index == 3) {
            return Constants.MOVE.RIGHT;
        }
        else return Constants.MOVE.NEUTRAL;
    }

    @Override
    public Object getMove(NeuralNetwork network, Game game, long timeDue) {
        return null;
    }
}

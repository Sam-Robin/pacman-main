package pacman.controllers.examples.po;

import pacman.controllers.DecentralisedGhostController;
import pacman.controllers.examples.po.NN.NeuralNetwork;
import pacman.game.Constants;
import pacman.game.Game;
import java.util.*;

public class NNGhost extends DecentralisedGhostController {

    private int TICK_THRESHOLD;
    private NeuralNetwork network;
    private int pacmanLastSeen;
    private int[] ghostsLastKnown;
    private int distanceToPacman;
    private int toNode;

    public NNGhost(Constants.GHOST ghost) {
        super(ghost);
        network = new NeuralNetwork();
        this.pacmanLastSeen = -1;
        ghostsLastKnown = new int[3];
        distanceToPacman = -1;
    }

    public NNGhost(Constants.GHOST ghost, int TICK_THRESHOLD) {
        super(ghost);
        this.TICK_THRESHOLD = TICK_THRESHOLD;
        this.pacmanLastSeen = -1;
        ghostsLastKnown = new int[3];
        distanceToPacman = -1;
    }

    /**
     * Sets the initial parameters of the neural network.
     * @param game
     */
    public void setupNN(Game game) {
        int myLocation = game.getGhostCurrentNodeIndex(ghost);
        int pacmanLocation = whereIsPacman(game);
        int edibleTime = game.getGhostEdibleTime(ghost);
        int score = game.getScore();
        int time = game.getTotalTime();

        ArrayList<Double> inputs = new ArrayList<>();
        inputs.add((double) myLocation / 2);
        inputs.add((double) pacmanLocation / 2);
        inputs.add((double) edibleTime);
        inputs.add((double) score);
        inputs.add((double) time);

        int[] design = { inputs.size(), 5, 8, 5, 4 };

        network = NeuralNetwork.generateRandomNetwork(inputs, design);
    }

    /**
     * Updates the neural network with the latest game information
     * @param game
     */
    public void updateNN(Game game) {
        // Get this ghost's location
        int currentLocation = game.getGhostCurrentNodeIndex(ghost);

        // See if the ghost is edible
        int edibleTime = game.getGhostEdibleTime(ghost);

        // Get Pacman's last known location
        int pacmanLocation = game.getPacmanCurrentNodeIndex();
        // Update pacman's last known location with the most up to date location
        if (pacmanLocation > -1) {
            pacmanLastSeen = pacmanLocation;
        }

        // Get distance to Pacman
        if (pacmanLocation > -1) {
            distanceToPacman = game.getShortestPathDistance(currentLocation, pacmanLastSeen);
        }

        // Get last known locations of other ghosts
        int i = 0;
        for (Constants.GHOST g : Constants.GHOST.values()) {
            // Make sure the ghost isn't checking its own location
            if (g.className != ghost.className) {
                // Only update when this ghost sees another ghost
                if (game.getGhostCurrentNodeIndex(g) > -1) {
                    ghostsLastKnown[i] = game.getGhostCurrentNodeIndex(g);
                }
                i++;
            }
        }

        int score = game.getScore();
        int time = game.getTotalTime();

        List<Double> inputs = new ArrayList<>();
        inputs.add((double) currentLocation / 2);
        inputs.add((double) pacmanLocation / 2);
        inputs.add((double) edibleTime);
        inputs.add((double) score);
        inputs.add((double) time);
        network.setInputs(inputs);
    }

    @Override
    public Constants.MOVE getMove(Game game, long timeDue) {
        // Update the neural network
        updateNN(game);
        network.train();

        // Get the next node to move to
        ArrayList<Double> outputs = (ArrayList) network.getOutputs();

        int largest = 0;
        for (int c = 0; c < outputs.size(); c++) {
            if (outputs.get(c) > outputs.get(largest)) {
                largest = c;
            }
        }
        if (largest == 0) {
            return Constants.MOVE.UP;
        }
        else if (largest == 1) {
            return Constants.MOVE.DOWN;
        }
        else if (largest == 2) {
            return Constants.MOVE.LEFT;
        }
        else if (largest == 3) {
            return Constants.MOVE.RIGHT;
        }

        return Constants.MOVE.UP;
    }

    @Override
    public Object getMove(NeuralNetwork network, Game game, long timeDue) {
        return null;
    }

    /**
     * Returns the location of pacman
     * @param game
     * @return
     */
    private int whereIsPacman(Game game) {
        int pacmanLocation = game.getPacmanCurrentNodeIndex();
        return pacmanLocation;
    }

    public int getToNode() {
        return toNode;
    }

    public NeuralNetwork getNetwork() {
        return network;
    }

    public void setNetwork(NeuralNetwork network) {
        this.network = network;
    }
}

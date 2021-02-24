package pacman.controllers.examples.po;

import pacman.controllers.DecentralisedGhostController;
import pacman.controllers.examples.po.NN.NEAT.Genome;
import pacman.controllers.examples.po.NN.NeuralNetwork;
import pacman.game.Constants;
import pacman.game.Game;
import java.util.*;

public class NNGhost extends DecentralisedGhostController {

    private int TICK_THRESHOLD;
    private NeuralNetwork network;
    private int pacmanLastSeen;
    private int[] ghostsLastKnown;
    private Genome genome;
    private int distanceToPacman;
    private double[] inputs;

    public NNGhost(Constants.GHOST ghost) {
        super(ghost);
        network = new NeuralNetwork();
        this.pacmanLastSeen = -1;
        ghostsLastKnown = new int[3];
        this.genome = new Genome();
    }

    public NNGhost(Constants.GHOST ghost, int TICK_THRESHOLD) {
        super(ghost);
        this.TICK_THRESHOLD = TICK_THRESHOLD;
        this.pacmanLastSeen = -1;
        ghostsLastKnown = new int[3];
        this.genome = new Genome();
    }

    public NNGhost(Constants.GHOST ghost, Genome genome) {
        super(ghost);
        this.TICK_THRESHOLD = TICK_THRESHOLD;
        this.pacmanLastSeen = -1;
        ghostsLastKnown = new int[3];
        this.genome = genome;
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

        inputs = new double[5];
        inputs[0] = currentLocation / 2;
        inputs[1] = pacmanLocation / 2;
        inputs[2] = edibleTime;
        inputs[3] = score;
        inputs[4] = time;
        try {
            genome.calculate(inputs);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Constants.MOVE getMove(Game game, long timeDue) throws Exception {
        // Update the neural network
        updateNN(game);

        // Get the next node to move to
        double[] outputs = genome.calculate(inputs);

        int largest = 0;
        for (int c = 0; c < outputs.length; c++) {
            if (outputs[c] > outputs[largest]) {
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

    public NeuralNetwork getNetwork() {
        return network;
    }

    public void setNetwork(NeuralNetwork network) {
        this.network = network;
    }

    public Genome getGenome() {
        return genome;
    }

    public void setGenome(Genome genome) {
        this.genome = genome;
    }
}

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

    public void setupNN(Game game) {
        int pacmanLocation = whereIsPacman(game);
        int edibleTime = game.getGhostEdibleTime(ghost);

        ArrayList<Double> inputs = new ArrayList<>();
        inputs.add((double) pacmanLocation);
        inputs.add((double) edibleTime);

        int[] design = { inputs.size(), 5, 10, game.getNumberOfNodes() };

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
    }

    @Override
    public Constants.MOVE getMove(Game game, long timeDue) {
        // Update the neural network
        updateNN(game);
        network.train();

        // Get the next node to move to
        ArrayList<Double> outputs = (ArrayList) network.getOutputs();

        // Convert ArrayList to HashMap
        HashMap<Integer, Double> nodeChoices = new HashMap<>();
        int i = 0;
        for (Double d : outputs) {
            nodeChoices.put(i, d);
            i++;
        }

        // Sort the HashMap by values
        List<Integer> mapKeys = new ArrayList<>(nodeChoices.keySet());
        List<Double> mapValues = new ArrayList<>(nodeChoices.values());
        Collections.sort(mapValues);
        Collections.reverse(mapValues);
        Collections.sort(mapKeys);
        Collections.reverse(mapKeys);
        LinkedHashMap<Integer, Double> sortedMap = new LinkedHashMap<>();

        Iterator<Double> valueIterator = mapValues.iterator();
        while(valueIterator.hasNext()) {
            Double value = valueIterator.next();
            Iterator<Integer> keyIterator = mapKeys.iterator();

            while (keyIterator.hasNext()) {
                Integer key = keyIterator.next();
                Double comp1 = nodeChoices.get(key);
                Double comp2 = value;

                if (comp1.equals(comp2)) {
                    keyIterator.remove();
                    sortedMap.put(key, value);
                    break;
                }
            }
        }

        Map.Entry<Integer, Double> entry = sortedMap.entrySet().iterator().next();
        Integer node = entry.getKey();

        return game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost), node,
                game.getGhostLastMoveMade(ghost), Constants.DM.PATH);
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
}

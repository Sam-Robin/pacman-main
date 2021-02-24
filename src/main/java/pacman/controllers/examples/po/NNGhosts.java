package pacman.controllers.examples.po;

import com.fossgalaxy.object.annotations.ObjectDef;
import pacman.controllers.DecentralisedGhostController;
import pacman.controllers.examples.po.NN.NEAT.Genome;
import pacman.controllers.examples.po.NN.NeuralNetwork;
import pacman.game.Constants;
import pacman.game.Game;

import java.util.*;

public class NNGhosts extends DecentralisedGhostController {

    @ObjectDef("POG")
    public NNGhosts(Genome genome) {
        super(true, Constants.GHOST.BLINKY, new EnumMap<>(Constants.GHOST.class));
        controllers.put(Constants.GHOST.BLINKY, new NNGhost(Constants.GHOST.BLINKY, genome));
        controllers.put(Constants.GHOST.INKY, new NNGhost(Constants.GHOST.INKY, genome));
        controllers.put(Constants.GHOST.PINKY, new NNGhost(Constants.GHOST.PINKY, genome));
        controllers.put(Constants.GHOST.SUE, new NNGhost(Constants.GHOST.SUE, genome));
    }

    /**
     * Updates the neural networks of all the ghosts a controller has.
     * @param game
     */
    public void runGhosts(Game game) {
        for (DecentralisedGhostController g : controllers.values()) {
            ((NNGhost) g).updateNN(game);
        }
    }

    public HashMap<Constants.GHOST, NeuralNetwork> getGhostsNetworks() {

        ArrayList<NeuralNetwork> neuralNetworks = new ArrayList<>();
        ArrayList<Constants.GHOST> ghosts = new ArrayList<>(controllers.keySet());
        HashMap<Constants.GHOST, NeuralNetwork> output = new HashMap<>();

        for (DecentralisedGhostController entry : controllers.values()) {
            NeuralNetwork network = ((NNGhost) entry).getNetwork();
            neuralNetworks.add(network);
        }

        for (int i = 0; i < 4; i++){
            output.put(ghosts.get(i), neuralNetworks.get(i));
        }

        return output;
    }

    public void setupNNs_Predefined(HashMap<Constants.GHOST, NeuralNetwork> map) throws Exception {
        // Check that all ghosts have networks associated with them
        if (map.entrySet().size() < 4) {
            throw new Exception("There are too few ghosts with pre-defined networks." +
                    "\nExpected: " + 4 + " Given: " + map.entrySet().size()) {
            };
        }
        else if (map.entrySet().size() > 4) {
            throw new Exception("There are too many ghosts with pre-defined networks." +
                    "\nExpected: " + 4 + " Given: " + map.entrySet().size()) {
            };
        }
        else {
            // Iterate through the HashMap
            for (Map.Entry<Constants.GHOST, NeuralNetwork> entry : map.entrySet()) {
                // Iterate through the ghosts
                for (DecentralisedGhostController ghost : controllers.values()) {
                    // Check that the entry's ghost is the same as the ghost's
                    if (entry.getKey().className == ghost.getName()) {
                        // If they're the same, then set the predefined network
                        ((NNGhost) ghost).setNetwork(entry.getValue());
                    }
                }
            }
        }
    }

    @Override
    public String getName() {
        return "POG";
    }

    @Override
    public EnumMap<Constants.GHOST, Constants.MOVE> getMove(Game game, long timeDue) {
        myMoves.clear();

        for (Constants.GHOST ghost : Constants.GHOST.values()) {
            try {
                myMoves.put(
                        ghost,
                        (Constants.MOVE)controllers.get(ghost).getMove(
                                (po) ? game.copy(ghost) : game.copy(),
                                timeDue));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return myMoves;
    }

    @Override
    public Object getMove(NeuralNetwork network, Game game, long timeDue) {
        return null;
    }

    public EnumMap<Constants.GHOST, DecentralisedGhostController> getControllers() {
        return controllers;
    }
}

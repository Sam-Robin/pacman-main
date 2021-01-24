package pacman.controllers.examples.po;

import com.fossgalaxy.object.annotations.ObjectDef;
import pacman.controllers.DecentralisedGhostController;
import pacman.controllers.examples.po.NN.NeuralNetwork;
import pacman.game.Constants;
import pacman.game.Game;

import java.util.EnumMap;

public class NNGhosts extends DecentralisedGhostController {

    @ObjectDef("POG")
    public NNGhosts() {
        super(true, Constants.GHOST.BLINKY, new EnumMap<>(Constants.GHOST.class));
        controllers.put(Constants.GHOST.BLINKY, new NNGhost(Constants.GHOST.BLINKY));
        controllers.put(Constants.GHOST.INKY, new NNGhost(Constants.GHOST.INKY));
        controllers.put(Constants.GHOST.PINKY, new NNGhost(Constants.GHOST.PINKY));
        controllers.put(Constants.GHOST.SUE, new NNGhost(Constants.GHOST.SUE));
    }

    /**
     * Updates the neural networks of all the ghosts a controller has.
     * @param game
     */
    public void updateNNs(Game game) {
        for (DecentralisedGhostController g : controllers.values()) {
            ((NNGhost) g).updateNN(game);
        }
    }

    public void setupNNs(Game game) {
        for (DecentralisedGhostController g : controllers.values()) {
            ((NNGhost) g).setupNN(game);
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
            myMoves.put(
                    ghost,
                    (Constants.MOVE)controllers.get(ghost).getMove(
                            (po) ? game.copy(ghost) : game.copy(),
                            timeDue));
        }
        return myMoves;
    }

    @Override
    public Object getMove(NeuralNetwork network, Game game, long timeDue) {
        return null;
    }
}

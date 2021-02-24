package pacman.controllers.examples.po;

import pacman.controllers.DecentralisedGhostController;
import pacman.controllers.examples.po.NN.NeuralNetwork;
import pacman.game.Constants;
import pacman.game.Game;

import java.util.EnumMap;

public class NeatGhosts extends DecentralisedGhostController {

    public NeatGhosts() {
        super(true, Constants.GHOST.BLINKY, new EnumMap<>(Constants.GHOST.class));
        controllers.put(Constants.GHOST.BLINKY, new NNGhost(Constants.GHOST.BLINKY));
        controllers.put(Constants.GHOST.INKY, new NNGhost(Constants.GHOST.INKY));
        controllers.put(Constants.GHOST.PINKY, new NNGhost(Constants.GHOST.PINKY));
        controllers.put(Constants.GHOST.SUE, new NNGhost(Constants.GHOST.SUE));
    }

    public void setup(Game game) {
        for (DecentralisedGhostController g : controllers.values()) {
            ((NeatGhost) g).setup(game);
        }
    }

    @Override
    public Object getMove(Game game, long timeDue) {
        return null;
    }

    @Override
    public Object getMove(NeuralNetwork network, Game game, long timeDue) {
        return null;
    }
}

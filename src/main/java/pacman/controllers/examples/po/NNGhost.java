package pacman.controllers.examples.po;

import pacman.controllers.DecentralisedGhostController;
import pacman.controllers.examples.po.NN.NeuralNetwork;
import pacman.game.Constants;
import pacman.game.Game;

public class NNGhost extends DecentralisedGhostController {

    private int TICK_THRESHOLD;

    public NNGhost(Constants.GHOST ghost) {
        super(ghost);
    }

    public NNGhost(Constants.GHOST ghost, int TICK_THRESHOLD) {
        super(ghost);
        this.TICK_THRESHOLD = TICK_THRESHOLD;
    }

    @Override
    public Constants.MOVE getMove(NeuralNetwork network, Game game, long timeDue) {
        return null;
    }
}

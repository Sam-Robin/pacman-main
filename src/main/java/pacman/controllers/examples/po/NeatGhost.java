package pacman.controllers.examples.po;

import pacman.controllers.DecentralisedGhostController;
import pacman.controllers.examples.po.NN.NeuralNetwork;
import pacman.game.Constants;
import pacman.game.Game;

public class NeatGhost extends DecentralisedGhostController {

    public NeatGhost(Constants.GHOST ghost) {
        super(ghost);
    }

    public void setup(Game game) {

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

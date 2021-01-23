package pacman.controllers;

import pacman.controllers.examples.po.NN.NeuralNetwork;
import pacman.game.Constants;
import pacman.game.Game;

public abstract class DecentralisedGhostController {
    protected final Constants.GHOST ghost;
    public DecentralisedGhostController(Constants.GHOST ghost) {
        this.ghost = ghost;
    }

    public abstract Constants.MOVE getMove(NeuralNetwork network, Game game, long timeDue);
}

package pacman.controllers;

import pacman.controllers.examples.po.NN.NeuralNetwork;
import pacman.game.Constants;
import pacman.game.Game;

import java.util.EnumMap;

public abstract class CentralisedGhostController extends Controller {

    protected final boolean po;
    protected final Constants.GHOST ghost;
    protected EnumMap<Constants.GHOST, CentralisedGhostController> controllers = new EnumMap<>(Constants.GHOST.class);
    protected EnumMap<Constants.GHOST, Constants.MOVE> myMoves = new EnumMap<Constants.GHOST, Constants.MOVE>(Constants.GHOST.class);

    public CentralisedGhostController(boolean po, Constants.GHOST ghost, EnumMap<Constants.GHOST, CentralisedGhostController> controllers) {
        this.po = true;
        this.ghost = ghost;
        this.controllers = controllers;
    }

    public CentralisedGhostController(Constants.GHOST ghost) {
        this.ghost = ghost;
        this.po = true;
    }

    public abstract Object getMove(NeuralNetwork network, Game game, long timeDue);
}

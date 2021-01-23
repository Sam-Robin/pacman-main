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
        super(true, Constants.GHOST.BLINKY, new EnumMap<Constants.GHOST, DecentralisedGhostController>(Constants.GHOST.class));
        controllers.put(Constants.GHOST.BLINKY, new NNGhost(Constants.GHOST.BLINKY));
        controllers.put(Constants.GHOST.INKY, new NNGhost(Constants.GHOST.INKY));
        controllers.put(Constants.GHOST.PINKY, new NNGhost(Constants.GHOST.PINKY));
        controllers.put(Constants.GHOST.SUE, new NNGhost(Constants.GHOST.SUE));
    }

    @Override
    public String getName() {
        return "POG";
    }

    @Override
    public EnumMap<Constants.GHOST, Constants.MOVE> getMove(Game game, long timeDue) {
        return null;
    }

    @Override
    public Object getMove(NeuralNetwork network, Game game, long timeDue) {
        return null;
    }
}

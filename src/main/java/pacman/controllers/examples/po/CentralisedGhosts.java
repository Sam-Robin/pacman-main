package pacman.controllers.examples.po;

import pacman.controllers.CentralisedGhostController;
import pacman.controllers.examples.po.NN.NEAT.Genome;
import pacman.controllers.examples.po.NN.NeuralNetwork;
import pacman.game.Constants;
import pacman.game.Game;

import java.util.EnumMap;

public class CentralisedGhosts extends CentralisedGhostController {

    public CentralisedGhosts(Genome genome) {
        super(true, Constants.GHOST.BLINKY, new EnumMap<>(Constants.GHOST.class));
        controllers.put(Constants.GHOST.BLINKY, new CentralisedGhost(Constants.GHOST.BLINKY, genome));
        controllers.put(Constants.GHOST.INKY, new CentralisedGhost(Constants.GHOST.INKY, genome));
        controllers.put(Constants.GHOST.PINKY, new CentralisedGhost(Constants.GHOST.PINKY, genome));
        controllers.put(Constants.GHOST.SUE, new CentralisedGhost(Constants.GHOST.SUE, genome));
    }

    @Override
    public Object getMove(Game game, long timeDue) throws Exception {
        return null;
    }

    @Override
    public Object getMove(NeuralNetwork network, Game game, long timeDue) {
        return null;
    }
}

package pacman.controllers.examples.po;

import pacman.controllers.CentralisedGhostController;
import pacman.controllers.examples.po.NN.NEAT.Genome;
import pacman.controllers.examples.po.NN.NeuralNetwork;
import pacman.game.Constants;
import pacman.game.Game;

public class CentralisedGhost extends CentralisedGhostController {

    public CentralisedGhost(Constants.GHOST ghost, Genome genome) {
        super(ghost);
    }

    public CentralisedGhost(Constants.GHOST ghost) {
        super(ghost);
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

package pacman.controllers.examples.po;

import com.fossgalaxy.object.annotations.ObjectDef;
import pacman.controllers.PacmanController;
import pacman.controllers.examples.po.NN.NeuralNetwork;
import pacman.game.Constants;
import pacman.game.Game;

import java.util.Random;

public class PORandomPacMan extends PacmanController {
    private Random rnd = new Random();
    private Constants.MOVE[] allMoves = Constants.MOVE.values();

    @ObjectDef("POP")
    public PORandomPacMan() {
    }

    /* (non-Javadoc)
     * @see pacman.controllers.Controller#getMove(pacman.game.Game, long)
     */
    @Override
    public Constants.MOVE getMove(Game game, long timeDue) {
        return allMoves[rnd.nextInt(allMoves.length)];
    }

    @Override
    public Constants.MOVE getMove(NeuralNetwork network, Game game, long timeDue) {
        return null;
    }

}

package pacman.controllers;

import com.fossgalaxy.object.annotations.ObjectDefStatic;
import pacman.controllers.examples.po.NN.NeuralNetwork;
import pacman.controllers.examples.po.NNGhost;
import pacman.game.Constants;
import pacman.game.Game;

import java.util.EnumMap;

public class NNGhostController extends Controller<EnumMap<Constants.GHOST, Constants.MOVE>> {

    private final boolean po;
    protected EnumMap<Constants.GHOST, DecentralisedGhostController> controllers = new EnumMap<>(Constants.GHOST.class);
    private EnumMap<Constants.GHOST, Constants.MOVE> myMoves = new EnumMap<Constants.GHOST, Constants.MOVE>(Constants.GHOST.class);

    public NNGhostController(boolean po, EnumMap<Constants.GHOST, DecentralisedGhostController> controllers) {
        this.po = po;
        this.controllers = controllers;
    }

    public NNGhostController(EnumMap<Constants.GHOST, DecentralisedGhostController> controllers) {
        this(true, controllers);
    }

    @ObjectDefStatic("NNGhostController")
    public static NNGhostController masControllerFactory(boolean po, DecentralisedGhostController blinky, DecentralisedGhostController inky,
                                                     DecentralisedGhostController pinky, DecentralisedGhostController sue) {
        EnumMap<Constants.GHOST, DecentralisedGhostController> ghosts = new EnumMap<>(Constants.GHOST.class);
        ghosts.put(Constants.GHOST.BLINKY, blinky);
        ghosts.put(Constants.GHOST.INKY, inky);
        ghosts.put(Constants.GHOST.PINKY, pinky);
        ghosts.put(Constants.GHOST.SUE, sue);
        return new NNGhostController(po, ghosts);
    }

    @Override
    public final EnumMap<Constants.GHOST, Constants.MOVE> getMove(NeuralNetwork network, Game game, long timeDue) {
        myMoves.clear();

        for (Constants.GHOST ghost : Constants.GHOST.values()) {
            myMoves.put(
                    ghost,
                    (Constants.MOVE) controllers.get(ghost).getMove(
                            network, (po) ? game.copy(ghost) : game.copy(),
                            timeDue));
        }
        return myMoves;
    }

    /**
     * This is a shallow copy used to alter the PO status to force it to a desired value
     *
     * @param po Should the copy enforce PO on the ghosts
     * @return The copy created
     */
    public final NNGhostController copy(boolean po) {
        NNGhostController copy = new NNGhostController(po, controllers);
        copy.setName(this.getName());
        return copy;
    }

    @Override
    public EnumMap<Constants.GHOST, Constants.MOVE> getMove(Game game, long timeDue) {
        return null;
    }
}

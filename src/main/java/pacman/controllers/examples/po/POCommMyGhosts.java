package pacman.controllers.examples.po;

import com.fossgalaxy.object.annotations.ObjectDef;
import pacman.controllers.IndividualGhostController;
import pacman.controllers.MASController;
import pacman.game.Constants;
import pacman.game.comms.BasicMessenger;
import pacman.game.comms.Messenger;

import java.util.EnumMap;

public class POCommMyGhosts extends MASController {

    private Messenger messenger;

    public POCommMyGhosts() {
        this(50);
    }

    @ObjectDef("POGC")
    public POCommMyGhosts(int TICK_THRESHOLD) {
        super(true, new EnumMap<Constants.GHOST, IndividualGhostController>(Constants.GHOST.class));
        messenger = new BasicMessenger();
        controllers.put(Constants.GHOST.BLINKY,
                new POCommMyGhost(Constants.GHOST.BLINKY, TICK_THRESHOLD, messenger));
        controllers.put(Constants.GHOST.INKY,
                new POCommMyGhost(Constants.GHOST.INKY, TICK_THRESHOLD, messenger));
        controllers.put(Constants.GHOST.PINKY,
                new POCommMyGhost(Constants.GHOST.PINKY, TICK_THRESHOLD, messenger));
        controllers.put(Constants.GHOST.SUE,
                new POCommMyGhost(Constants.GHOST.SUE, TICK_THRESHOLD, messenger));
    }

    @Override
    public String getName() {
        return "POGC";
    }

    /**
     * Updates the Messenger
     */
    public void updateComms() {

    }
}

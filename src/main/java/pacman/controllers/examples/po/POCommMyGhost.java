package pacman.controllers.examples.po;

import pacman.controllers.IndividualGhostController;
import pacman.game.Constants;
import pacman.game.Game;
import pacman.game.comms.BasicMessage;
import pacman.game.comms.BasicMessenger;
import pacman.game.comms.Message;
import pacman.game.comms.Messenger;
import java.util.Random;

public class POCommMyGhost extends IndividualGhostController {

    private final static float CONSISTENCY = 0.9f;    //attack Ms Pac-Man with this probability
    private final static int PILL_PROXIMITY = 15;        //if Ms Pac-Man is this close to a power pill, back away
    Random rnd = new Random();
    private int TICK_THRESHOLD;
    private int lastPacmanIndex = -1;
    private int tickSeen = -1;
    // Where this ghost last saw Ms PacMan, and at what time
    private int[] pacmanLastSeen;
    private Messenger messenger;

    public POCommMyGhost(Constants.GHOST ghost) {
        this(ghost, 5, new BasicMessenger());
    }

    public POCommMyGhost(Constants.GHOST ghost, int TICK_THRESHOLD, Messenger messenger) {
        super(ghost);
        this.TICK_THRESHOLD = TICK_THRESHOLD;
        this.pacmanLastSeen = new int[2];
        this.messenger = messenger;
    }

    @Override
    public Constants.MOVE getMove(Game game, long timeDue) {
        // Find where pacman was last seen by this ghost
        int pacmanLocation = whereIsPacman(game);
        if (pacmanLocation > -1) {
            // Get the current system time
            int time = game.getTotalTime();
            pacmanLastSeen[0] = time;
            pacmanLastSeen[1] = pacmanLocation;
            // Alert the other ghosts
            Message message = new BasicMessage(ghost, null,
                    Message.MessageType.PACMAN_SEEN, pacmanLocation, time);
            messenger.addMessage(message);
            System.out.println("PACMAN SPOTTED\tTime: " + time +
                    "\tNode: " + pacmanLocation +
                    "\tGhost: " + ghost.className);
        }

        int myLocation = game.getGhostCurrentNodeIndex(ghost);

        // Check to see if this ghost is edible
        if(amIEdible(game)) {
            // If this ghost is edible, move away from Ms PacMan's last known location
            return game.getNextMoveAwayFromTarget(myLocation, pacmanLastSeen[1], Constants.DM.PATH);
        }
        else {
            // Otherwise, move towards Ms PacMan
            return game.getNextMoveTowardsTarget(myLocation, pacmanLastSeen[1], Constants.DM.PATH);
        }
    }

    /**
     * Returns the location of pacman
     * @param game
     * @return
     */
    private int whereIsPacman(Game game) {
        int pacmanLocation = game.getPacmanCurrentNodeIndex();
        return pacmanLocation;
    }

    private Boolean amIEdible(Game game) {
        // If the time that this ghost is edible for is greater than 1, then it must be edible
        if (game.getGhostEdibleTime(ghost) > 0) {
            return true;
        }
        return false;
    }

    public int[] getPacmanLastSeen() {
        return pacmanLastSeen;
    }
}

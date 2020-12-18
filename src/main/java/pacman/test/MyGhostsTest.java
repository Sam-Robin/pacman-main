package pacman.test;

import pacman.controllers.PacmanController;
import pacman.controllers.examples.po.POCommGhosts;
import pacman.controllers.examples.po.POCommMyGhosts;
import pacman.controllers.examples.po.POPacMan;
import pacman.controllers.examples.po.PORandomPacMan;
import pacman.game.Constants;
import pacman.game.Game;
import pacman.game.GameView;

import java.util.EnumMap;
import java.util.Random;

public class MyGhostsTest {

    /**
     * Show 1 view for Pacman
     * in a game with POCommMyGhost objects
     *
     * @param args Not used
     */
    public static void main(String[] args) {
        // Use a random seed for the game
        Random r = new Random();
        long rand = 100 + (100 + 100) * r.nextLong();
        Game g = new Game(rand);

        GameView gView = new GameView(g).showGame();
        PacmanController pacman = new POPacMan();
        POCommMyGhosts ghosts = new POCommMyGhosts(50);

        while(!g.gameOver()) {
            try {
                Thread.sleep(40);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Update the communicator
            ghosts.updateComms();

            Constants.MOVE pacmanMove =
                    pacman.getMove(g.copy(5), 40);
            EnumMap<Constants.GHOST, Constants.MOVE> ghostMoves =
                    ghosts.getMove(g.copy(), -1);

            g.advanceGame(pacmanMove, ghostMoves);

            gView.paintImmediately(gView.getBounds());
        }
    }
}

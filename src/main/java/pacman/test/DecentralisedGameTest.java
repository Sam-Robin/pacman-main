package pacman.test;

import pacman.controllers.PacmanController;
import pacman.controllers.examples.po.NN.NeuralNetwork;
import pacman.controllers.examples.po.NNGhosts;
import pacman.controllers.examples.po.POCommMyGhosts;
import pacman.controllers.examples.po.POPacMan;
import pacman.game.Constants;
import pacman.game.Game;
import pacman.game.GameView;

import java.util.EnumMap;

public class DecentralisedGameTest {

    private static NeuralNetwork[] networks = new NeuralNetwork[4];
    private static Game g;

    /**
     * Show 1 view for each ghost, in PO mode
     *
     * @param args Not used
     */
    public static void main(String[] args) {
        g = new Game(0);
        GameView[] ghostViews = new GameView[]{
                new GameView(g, false).showGame(),
                new GameView(g, false).showGame(),
                new GameView(g, false).showGame(),
                new GameView(g, false).showGame()
        };
        for (Constants.GHOST ghost : Constants.GHOST.values()) {
            ghostViews[ghost.ordinal()].setPO(true, ghost);
        }

        PacmanController pacman = new POPacMan();
        NNGhosts ghosts = new NNGhosts();

        // Create a neural network
        for (int i = 0; i < 4; i++) {
            networks[i] = new NeuralNetwork();
        }

        while(!g.gameOver()) {
            try {
                Thread.sleep(40);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Constants.MOVE pacmanMove =
                    pacman.getMove(g.copy(5), 40);
            EnumMap<Constants.GHOST, Constants.MOVE> ghostMoves =
                    ghosts.getMove(g.copy(), -1);

            g.advanceGame(pacmanMove, ghostMoves);

            // Display every ghost's view
            for (GameView view : ghostViews) {
                view.paintImmediately(view.getBounds());
            }
        }
    }

    /**
     * Sets the initial parameters for the neural network
     */
    private void setNetwork() {
        // Setup inputs
    }

    /**
     * Updates the input that the neural network is sent
     * @return
     */
    private NeuralNetwork updateInput() {

        return null;
    }
}

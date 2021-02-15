package pacman.test;

import pacman.controllers.Controller;
import pacman.controllers.examples.po.NN.NeuralNetwork;
import pacman.game.Constants;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents all the data about an experiment
 *
 * @author Sam Robinson
 */
public class ExperimentData implements Comparable {

    private HashMap<Constants.GHOST, NeuralNetwork> networks;
    private int generation;
    private int finalScore;
    private int timeTaken;

    public ExperimentData() {

    }

    public ExperimentData(HashMap<Constants.GHOST, NeuralNetwork> networks, int generation,
                          int finalScore, int timeTaken) {
        this.networks = networks;
        this.generation = generation;
        this.finalScore = finalScore;
        this.timeTaken = timeTaken;
    }

    public HashMap<Constants.GHOST, NeuralNetwork> getNetworks() {
        return networks;
    }

    public void setNetworks(HashMap<Constants.GHOST, NeuralNetwork> networks) {
        this.networks = networks;
    }

    public int getGeneration() {
        return generation;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }

    public int getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(int finalScore) {
        this.finalScore = finalScore;
    }

    public int getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(int timeTaken) {
        this.timeTaken = timeTaken;
    }

    @Override
    public int compareTo(Object o) {
        return this.finalScore - ((ExperimentData) o).finalScore;
    }
}

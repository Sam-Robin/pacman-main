package pacman.test;

import pacman.controllers.examples.po.NN.NeuralNetwork;

import java.util.ArrayList;

/**
 * Represents all the data about an experiment
 *
 * @author Sam Robinson
 */
public class ExperimentData {

    private ArrayList<NeuralNetwork> networks;
    private int generation;
    private int finalScore;
    private int timeTaken;

    public ExperimentData() {

    }

    public ArrayList<NeuralNetwork> getNetworks() {
        return networks;
    }

    public void setNetworks(ArrayList<NeuralNetwork> networks) {
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
}

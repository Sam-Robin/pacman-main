package pacman.test;

import pacman.controllers.examples.po.NN.NEAT.Genome;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents all the data about an experiment
 *
 * @author Sam Robinson
 */
public class ExperimentData implements Comparable {

    private Genome genome;
    private int score;
    private int generation;
    private int timeTaken;

    public ExperimentData() {

    }

    public ExperimentData(Genome genome, int score,
                          int generation, int timeTaken) {
        this.genome = genome;
        this.score = score;
        this.generation = generation;
        this.timeTaken = timeTaken;
    }

    public Genome getGenome() {
        return genome;
    }

    public void setGenome(Genome genome) {
        this.genome = genome;
    }

    public int getGeneration() {
        return generation;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }

    public int getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(int timeTaken) {
        this.timeTaken = timeTaken;
    }

    @Override
    public int compareTo(Object o) {
        return this.score - ((ExperimentData) o).getScore();
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}

package pacman.controllers.examples.po.NN.NEAT;

import pacman.controllers.examples.po.NN.NEAT.Calculations.Calculator;

public class Client {

    private Genome genome;
    private Calculator calculator;
    private double score;
    private Species species;

    public void generateCalculator() {
        this.calculator = new Calculator(genome);
    }

    public double[] calculate(double... input) throws Exception {
        if (this.calculator == null) {
            generateCalculator();
        }
        return this.calculator.calculate(input);
    }

    public double distance(Client other) {
        return this.getGenome().distance(other.getGenome());
    }

    public void mutate() {
        getGenome().mutate(1);
    }

    public Genome getGenome() {
        return genome;
    }

    public void setGenome(Genome genome) {
        this.genome = genome;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Species getSpecies() {
        return species;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }
}

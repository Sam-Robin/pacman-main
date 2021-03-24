package pacman.test;

import pacman.controllers.examples.po.NN.NEAT.Frame.NetworkFrame;
import pacman.controllers.examples.po.NN.NEAT.Frame.NetworkPanel;
import pacman.controllers.examples.po.NN.NEAT.Genome;
import pacman.controllers.examples.po.NN.NEAT.Neat;

import java.util.ArrayList;
import java.util.Collections;

public class LogicGateNeatTest {

    private static ArrayList<Genome> genomes;
    private static double[] inputs;
    private static double expected;
    private enum NETWORK_TYPE {
        AND,
        OR,
        NAND,
        NOR
    }

    public static void main(String[] args) throws Exception {

        NetworkPanel panel = new NetworkPanel(null);
        NetworkFrame frame = new NetworkFrame(panel);
        frame.update();

        genomes = new ArrayList<>();
        inputs = new double[] { 1.0, 1.0 };
        int size = 20;
        genomes = createInitialPopulation(new Neat(2, 1, 1), size);
        evaluateGenomes();

        Genome bestGenome = genomes.get(0);

        for (int i = 0; i < 500; i++) {
            // Sort by score
            Collections.sort(genomes);

            // Crossover the genomes
            genomes = crossoverPopulation(genomes, size);

            // Assign each genome a score
            evaluateGenomes();

            // Find the best genome
            for (Genome genome : genomes) {
               // System.out.println("Inputs: " + inputs[0] + " " + inputs[1] + "\tOutput: " + genome.getCalculator().getOutput()[0] +
                //        "\tExpected: " + expected + "\tScore: " + genome.getScore());
                if (genome.getScore() > bestGenome.getScore()) {
                    bestGenome = genome;
                }
            }

            Thread.sleep(500);

            panel.setGenome(bestGenome);
            frame.update();
        }
    }

    private static void evaluateGenomes() throws Exception {
        for (Genome genome : genomes) {
           // genome.setScore(1.0 - costFunction(genome, NETWORK_TYPE.AND));
        }
    }

    private static ArrayList<Genome> createInitialPopulation(Neat neat, int size) {
        ArrayList<Genome> population = new ArrayList<>(size);

        Genome first = neat.emptyGenome(0);
        population.add(first);

        for (int i = 0; i < (size / 2) - 1; i++) {
            Genome next = first.deepCopy();
            next.mutate(200);
            population.add(next);
        }

        Genome second = neat.emptyGenome(0);
        population.add(second);

        for (int i = 0; i < (size / 2) - 1; i++) {
            Genome next = second.deepCopy();
            next.mutate(10);
            population.add(next);
        }

        for (Genome genome : population) {
            //genome.update();
        }

        return population;
    }

    private static ArrayList<Genome> crossoverPopulation(ArrayList<Genome> population, int size) {
        ArrayList<Genome> newPopulation = new ArrayList<>();
        Genome parent1, parent2;

        // Iterate through the population
        for (int i = 0; i < size; i++) {
            // Get the parent
            parent1 = population.get(i);

            // Choose the next-best genome
            try {
                // Try and get the (i + 1)'th genome
                parent2 = population.get(i + 1);
            }
            catch (Exception e) {
                // This must be the last genome in the list...
                // So get the best genome
                parent2 = population.get(0);
            }

            // Add the child to the new population
            newPopulation.add(Genome.crossover(parent1, parent2));
        }

        for (Genome genome : newPopulation) {
            //genome.update();
        }

        return newPopulation;
    }

//    private static double costFunction(Genome genome, NETWORK_TYPE type) throws Exception {
//        double[] outputs = genome.calculate(inputs);
//        if (type == NETWORK_TYPE.AND) {
//            expected = (Double)inputs[0] * (Double)inputs[1];
//        } else if (type == NETWORK_TYPE.OR) {
//            if ((Double)inputs[0] + (Double)inputs[1] > 1.0) {
//                expected = 1.0;
//            } else {
//                expected = 0.0;
//            }
//        } else if (type == NETWORK_TYPE.NAND) {
//            if ((Double)inputs[0] == 1.0D && (Double)inputs[1] == 1.0) {
//                expected = 0.0;
//            } else {
//                expected = 1.0;
//            }
//        }
//
//        double cost = Math.abs(expected - outputs[0]);
//        return cost;
//    }
}

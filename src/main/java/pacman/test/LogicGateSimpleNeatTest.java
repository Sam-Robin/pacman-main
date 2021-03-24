package pacman.test;


import pacman.controllers.examples.po.NN.NEAT.Frame.NetworkFrame;
import pacman.controllers.examples.po.NN.NEAT.Frame.NetworkPanel;
import pacman.controllers.examples.po.NN.NEAT.Genome;
import pacman.controllers.examples.po.NN.NEAT.Neat;

import java.util.ArrayList;

/**
 * Use a 1 + 1 genetic algorithm to learn logic gate behaviour
 */
public class LogicGateSimpleNeatTest {

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
        Neat neat = new Neat(2, 1, 1);
        Genome genome = neat.emptyGenome(100);
        //genome.update();
        inputs = new double[] { 0.0, 0.0 };

        //double score = 1.0 - costFunction(genome, NETWORK_TYPE.AND);
       // genome.setScore(score);

        Genome bestGenome = genome;

        for (int i = 0; i < 5000; i++) {
            Genome g1 = neat.emptyGenome(100);
            //g1.update();

            // Evaluate the score
            //score = 1.0 - costFunction(g1, NETWORK_TYPE.OR);
            //g1.setScore(score);

            if (g1.getScore() > bestGenome.getScore()) {
                bestGenome = g1;
            }

           // System.out.println("Inputs: " + inputs[0] + " " + inputs[1] + "\tOutput: " + g1.getCalculator().getOutput()[0] +
             //       "\tExpected: " + expected + "\tScore: " + g1.getScore());

            panel.setGenome(bestGenome);
            frame.update();
        }

        //System.out.println("BEST GENOME\n" + "Inputs: " + inputs[0] + " " + inputs[1] + "\tOutput: " + bestGenome.getCalculator().getOutput()[0] +
            //    "\tExpected: " + expected + "\tScore: " + bestGenome.getScore());
    }

    private static void reverseInputs() {
        for (int i = 0; i < inputs.length; i++) {
            if (inputs[i] == 1.0) {
                inputs[i] = 0.0;
            }
            else inputs[1] = 1.0;
        }
    }

    private static ArrayList<Genome> createInitialPopulation(Neat neat, int size) {
        ArrayList<Genome> population = new ArrayList<>(size);

        Genome first = neat.emptyGenome(10);
        population.add(first);

        for (int i = 0; i < (size / 2) - 1; i++) {
            Genome next = first.deepCopy();
            next.mutate(200);
            population.add(next);
        }

        Genome second = neat.emptyGenome(100);
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

//    private static double costFunction(Genome genome, NETWORK_TYPE type) throws Exception {
//        //double[] outputs = genome.calculate(inputs);
//        if (type == NETWORK_TYPE.AND) {
//            expected = (Double)inputs[0] * (Double)inputs[1];
//        } else if (type == NETWORK_TYPE.OR) {
//            if ((Double)inputs[0] + (Double)inputs[1] >= 1.0) {
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

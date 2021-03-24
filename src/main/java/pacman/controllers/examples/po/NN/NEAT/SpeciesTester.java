package pacman.controllers.examples.po.NN.NEAT;

public class SpeciesTester {

    public static void main(String[] args) throws Exception {
        Neat neat = new Neat(10, 1, 10);

        double[] in = new double[10];

        for (int i = 0; i < 10; i++) {
            in[i] = Math.random();
        }

        for (int i = 0; i < 100; i++) {
            for (Client c : neat.getClients()) {
                double score = c.calculate(in)[0];
                c.setScore(score);
            }
            neat.evolve();
            neat.printSpecies();
        }
    }
}

package pacman.controllers.examples.po.NN.NEAT;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class Species {

    private ArrayList<Client> clients;
    private Client representative;
    private double score;

    public Species(Client representative) {
        this.representative = representative;
        this.representative.setSpecies(this);
        this.clients = new ArrayList<>();
        clients.add(representative);
    }

    public boolean put(Client client) {
        if(client.distance(representative) < representative.getGenome().getNeat().getCP()) {
            client.setSpecies(this);
            clients.add(representative);

            return true;
        }
        return false;
    }

    public void forcePut(Client client) {
        client.setSpecies(this);
        clients.add(client);
    }

    public void goExtinct() {
        for (Client c : clients) {
            c.setSpecies(null);
        }
    }

    public double evaluateScore() {
        double eval = 0;
        for (Client c : clients) {
            eval += c.getScore();
        }
        eval = eval / clients.size();

        return eval;
    }

    public void reset() {
        Random random = new Random();
        representative = clients.get(random.nextInt(clients.size()));

        for (Client c : clients) {
            c.setSpecies(null);
        }

        clients.clear();

        clients.add(representative);
        representative.setSpecies(this);
        score = 0;
    }

    public void kill(double percentage) {
        clients.sort(
                new Comparator<Client>() {
                    @Override
                    public int compare(Client o1, Client o2) {
                        return Double.compare(o1.getScore(), o2.getScore());
                    }
                }
        );

        double amount = percentage * this.clients.size();
        for (int i = 0; i < amount; i++) {
            clients.get(0).setSpecies(null);
            clients.remove(0);
        }
    }

    public Genome breed() {
        Random random = new Random();
        Client c1 = clients.get(random.nextInt(clients.size()));
        Client c2 = clients.get(random.nextInt(clients.size()));
        while (c2 != c1) {
            c2 = clients.get(random.nextInt(clients.size()));
        }

        if (c1.getScore() > c2.getScore()) {
            return Genome.crossover(c1.getGenome(), c2.getGenome());
        }
        else return Genome.crossover(c2.getGenome(), c1.getGenome());
    }

    public int size() {
        return clients.size();
    }

    public ArrayList<Client> getClients() {
        return clients;
    }

    public Client getRepresentative() {
        return representative;
    }

    public double getScore() {
        return score;
    }
}

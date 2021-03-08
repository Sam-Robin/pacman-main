package pacman.controllers.examples.po.NN.NEAT;

public class Gene {

    protected int innovationNumber;
    private static int innovationCount;

    public Gene(int innovationNumber) {
        this.innovationNumber = innovationNumber;
        innovationCount++;
    }

    public Gene() {
        this.innovationNumber = innovationCount;
        innovationCount++;
    }

    public int getInnovationNumber() {
        return innovationNumber;
    }

    public void setInnovationNumber(int innovationNumber) {
        this.innovationNumber = innovationNumber;
    }
}

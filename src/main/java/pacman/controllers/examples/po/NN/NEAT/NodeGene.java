package pacman.controllers.examples.po.NN.NEAT;

public class NodeGene extends Gene implements Comparable {

    private double x, y;

    public NodeGene(int innovationNumber) {
        super(innovationNumber);
    }

    public NodeGene(int innovationNumber, double x, double y) {
        super(innovationNumber);
        this.x = x;
        this.y = y;
    }



    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        if (y > 1) {
            y = y / 2;
        }
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public boolean equals(Object o) {
        if (!(o instanceof NodeGene)) return false;
        return innovationNumber == ((NodeGene) o).getInnovationNumber();
    }

    public NodeGene deepCopy() {
        return new NodeGene(this.innovationNumber, this.x, this.y);
    }

    public int hashCode() {
        return innovationNumber;
    }

    @Override
    public int compareTo(Object o) {
        return Double.compare(this.x, ((NodeGene) o).getX());
    }
}

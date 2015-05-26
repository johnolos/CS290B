package applications.longestpath;

import java.util.List;

public class Path implements Comparable<Path> {

    final private List<Integer> path;
    final private double cost;

    public Path(List<Integer> path, double cost) {
        this.path = path;
        this.cost = cost;
    }
    @Override
    public int compareTo(Path otherPath) {
        return this.cost < otherPath.cost ? 1 : this.cost > otherPath.cost ? -1 : 0;
    }

    public double cost() {
        return cost;
    }


    @Override
    public String toString() { return path.toString() + "\n\tCost: " + cost; }

}

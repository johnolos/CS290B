package applications.longestpath;

import java.util.ArrayList;
import java.util.List;

public class Path implements Comparable<Path> {

    private List<Integer> path;
    private double cost;

    public Path(List<Integer> path, double cost) {
        this.path = path;
        this.cost = cost;
    }

    public List<Integer> getPath() {
		return path;
	}

	public void setPath(List<Integer> path) {
		this.path = path;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public Path() {
        path = new ArrayList<>();
        cost = 0;
    }

    public Path(Path path) {
        this.path = new ArrayList<>(path.path);
        this.cost = path.cost;
    }

    public void addNewNode(int node, double weight) {
        path.add(node);
        cost += weight;
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

package applications.longestpath;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Path implements Comparable<Path>, Serializable {

    static final private int NUM_PIXELS = 600;
    static final private int COST_X = 20;
    static final private int COST_Y = 565;
    static final private int PATH_X = 20;
    static final private int PATH_Y = 580;
    static final private int EDGE_MARGIN = 5;

    private List<Integer> path;
    private double cost;
    private int[][] graph;
    private int[][] coordinates;

    public Path(List<Integer> path, double cost, int[][] graph, int[][] coordinates) {
        this.path = new ArrayList<Integer>(path); 
        this.cost = cost;
        this.graph = graph;
        this.coordinates = coordinates;
    }

    public Path(int[][] graph, int[][] coordinates) {
        path = new ArrayList<>();
        cost = 0;
        this.graph = graph;
        this.coordinates = coordinates;
    }

    public Path(Path path) {
        this.path = new ArrayList<>(path.path);
        this.cost = path.cost;
        this.graph = path.getGraph();
        this.coordinates = path.getCoordinates();
    }

    public List<Integer> getPath() {
		return path;
	}

    public int[][] getGraph() { return graph; }
    public int[][] getCoordinates() { return coordinates; }

	public void setPath(List<Integer> path) {
		this.path = path;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

    public void addNewNode(int node, double weight) {
        path.add(node);
        cost += weight;
    }

    public int getLastNode() {
        return path.get(path.size() - 1);
    }
    
    public void addNodeToPath(int node, double dist) {
    	path.add(node);
    	cost = dist; 
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

    public JLabel createGraphicImage() {
        return Path.createGraphicImage(graph, coordinates, this);
    }

    public static JLabel createGraphicImage(final int[][] graph, final int[][] nodes, Path path) {
        final Image image = new BufferedImage(NUM_PIXELS, NUM_PIXELS, BufferedImage.TYPE_INT_ARGB);
        final Graphics graphics = image.getGraphics();

        // Information and variables
        List<Integer> intPath = path.getPath();

        // Draw edges as black lines
        int x1, x2, y1, y2;
        int edge_x, edge_y;
        graphics.setColor(Color.BLACK);
        for(int i = 0; i < graph.length; i++) {
            x1 = nodes[i][0];
            y1 = nodes[i][1];
            for(int j = 0; j < graph[i].length; j+=2) {
                x2 = nodes[graph[i][j]][0];
                y2 = nodes[graph[i][j]][1];
                graphics.drawLine(x1, y1, x2, y2);
                edge_x = x1 > x2 ? (x1 - x2) / 2 + x2 : (x2 - x1) / 2 + x1;
                edge_y = y1 > y2 ? (y1 - y2) / 2 + y2 : (y2 - y1) / 2 + y1;
                graphics.drawString(
                        "Edge: " + graph[i][j+1],
                        edge_x + EDGE_MARGIN,
                        edge_y - EDGE_MARGIN);
            }
        }

        // Draw path as green lines above black lines
        // i - 1 = old_node(x1,y2) i = new_node(x2,y2)
        graphics.setColor(Color.GREEN);
        for(int i = 1; i < intPath.size(); i++) {
            x1 = nodes[ intPath.get(i - 1) ][ 0 ];
            y1 = nodes[ intPath.get(i - 1) ][ 1 ];
            x2 = nodes[ intPath.get(i) ][ 0 ];
            y2 = nodes[ intPath.get(i) ][ 1 ];
            graphics.drawLine(x1, y1, x2, y2);
        }

        // Draw nodes as red circles
        graphics.setColor(Color.RED);
        final int VERTEX_DIAMETER = 6;
        for(int i = 0; i < nodes.length; i++) {
            int x = nodes[i][0];
            int y = nodes[i][1];
            graphics.fillOval(
                    x - VERTEX_DIAMETER/2, y - VERTEX_DIAMETER/2,
                    VERTEX_DIAMETER, VERTEX_DIAMETER
            );
            graphics.drawString("Node: " + i, x + EDGE_MARGIN, y - EDGE_MARGIN);
        }

        // Draw path cost at a stationary location.
        graphics.setColor(Color.BLACK);
        graphics.drawString("Path cost: " + path.cost(), COST_X, COST_Y);

        // Draw path at a stationary location.
        String pathStr = "";
        for(int i = 0; i < intPath.size(); i++) {
            pathStr += intPath.get(i) + " ";
        }
        pathStr.trim();
        graphics.drawString("Path: " + pathStr, PATH_X, PATH_Y);

        final ImageIcon imageIcon = new ImageIcon(image);
        return new JLabel(imageIcon);
    }


}

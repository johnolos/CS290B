

package applications.longestpath;

import api.*;
import system.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.rmi.registry.LocateRegistry;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
public class TaskLongestPath extends TaskRecursive<Path> {

    // Configure job
          static private File       GRAPH_FILE = Paths.get(".", "res", "exampleGraph30Nodes.txt").toFile();
    final static private String     FRAME_TITLE = "Longest Path Problem";
          static private Task       TASK;


    final static private int        PORT = 8202;
    final static public  String     SERVICE = "LongestPath";
          static private String     DOMAIN;
          static private int[][]    GRAPH;
          static private int[][]    COORDINATES;
          static private Path       GREEDY_PATH;
          static private SharedPath SHARED;


    public static void main(String args[]) throws Exception {
        DOMAIN = args.length == 0 ? "localhost" : args[ 0 ];
        if(args.length == 2) {
            GRAPH_FILE = Paths.get(".", "res", args[1]).toFile();
        }

        TASK = new TaskLongestPath();
        GRAPH = Graph.graphForNodes(GRAPH_FILE);
        COORDINATES = Graph.coordinatesOfNodes(GRAPH_FILE);
        GREEDY_PATH = Graph.greedyPath(GRAPH, COORDINATES);
        SHARED = new SharedPath(GREEDY_PATH);

        LongestPathController controller = new LongestPathController(DOMAIN, PORT);
        LocateRegistry.createRegistry(PORT).rebind(SERVICE, controller);

        new JobRunner(FRAME_TITLE, args, controller).run(TASK, SHARED, controller.getUrl());
    }

    private int[][] graph;
    private int[][] coordinates;
    private int node;
	private TaskLongestPath parentTask;

    public boolean[] visitedNodes;

    public TaskLongestPath() throws IOException {
        parentTask = null;
        node = -1;
        graph = Graph.graphForNodes(GRAPH_FILE);
        coordinates = Graph.coordinatesOfNodes(GRAPH_FILE);
        visitedNodes = new boolean[graph.length];
        for(int i = 0; i < graph.length; i++) {
            visitedNodes[i] = false;
        }
    }

    TaskLongestPath(TaskLongestPath parentTask, int node, int[][] graph, boolean[] visitedNodes, int[][] coordinates) {
        this.node = node;
        this.graph = graph;
        this.parentTask = parentTask;
        this.visitedNodes = visitedNodes;
        this.coordinates = coordinates;
    }

    @Override
    public boolean isAtomic() {
        // Checked and should work
        if(node == -1)
            return false;
        for(int i = 0; i < graph[node].length; i+=2) {
            if(!visitedNodes[graph[node][i]]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ReturnValue<Path> solve() {
        //TODO: Unfinished
        Path base = new Path(graph, coordinates);
        base.addNewNode(node, 0);
        return new ReturnValuePath(this, base);
    }

    @Override
    public ReturnDecomposition divideAndConquer() {
        // Checked.
        List<Task> children = new LinkedList<>();
        if(parentTask == null) {
            for(int i = 0; i < graph.length; i++) {
                boolean[] copyVisited = Arrays.copyOf(visitedNodes, visitedNodes.length);
                copyVisited[i] = true;
                TaskLongestPath child = new TaskLongestPath(this, i, graph, copyVisited, coordinates);
                children.add(child);
            }
            return new ReturnDecomposition(new LongestPath(graph, node), children);
        } else {
            for(int i = 0; i < graph[node].length; i+=2) {
                if(!visitedNodes[graph[node][i]]) {
                    boolean[] copyVisited = Arrays.copyOf(visitedNodes, visitedNodes.length);
                    copyVisited[graph[node][i]] = true;
                    TaskLongestPath child = new TaskLongestPath(this, graph[node][i], graph, copyVisited, coordinates);
                    children.add(child);
                }
            }
            return new ReturnDecomposition(new LongestPath(graph, node), children);
        }
    }

/*    private boolean isComplete() {
        for(int i = 0; i < graph.length; i++) {
            if(!visitedNodes[i]) {
                return false;
            }
        }
        return true;
    }*/

/*    private static Path findLongestPath(int srcNode, int[] neighbors, boolean[] visitedNodes, int[][] nodes) {
        Path currentPath = new Path();
        double max = 0.0;
        visitedNodes[srcNode] = true;
        for (int i = 0; i < neighbors.length; i+=2) {
            int dest = neighbors[i];
            if (!visitedNodes[dest]) {
                Path tempPath = findLongestPath(dest, nodes[dest], visitedNodes, nodes);
                final double dist = neighbors[i+1] + tempPath.getCost();
                if (dist > max) {
                    max = dist;
                    currentPath = new Path(tempPath.getPath(), tempPath.getCost());
                    currentPath.addNodeToPath(dest, dist);
                }
            }
        }
        visitedNodes[srcNode] = false;
        return currentPath;
    }*/

}

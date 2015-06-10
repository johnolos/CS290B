
package applications.longestpath;

import api.*;
import api.events.EventListener;
import system.Task;
import util.EuclideanGraph;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.rmi.registry.LocateRegistry;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
public class TaskLongestPath extends TaskRecursive<Path> {

    // Configure job
    final static private File       GRAPH_FILE = Paths.get(".", "res", "exampleGraph1.txt").toFile();
    final static private String     FRAME_TITLE = "Longest Path Problem";
          static private Task       TASK;

    final static private int        PORT = 8202;
    final static public  String     SERVICE = "LongestPath";
          static private String     DOMAIN;
          static private int[][]    GRAPH;
          static private Path       GREEDY_PATH;
          static private SharedPath SHARED;

    //static private final double UPPER_BOUND = tourDistance( CITIES, GREEDY_TOUR );
    //static private final Shared SHARED = new SharedTour( GREEDY_TOUR, UPPER_BOUND );

    public static void main(String args[]) throws Exception {
        DOMAIN = args.length == 0 ? "localhost" : args[ 0 ];

        TASK = new TaskLongestPath();
        GRAPH = Graph.graphForNodes(GRAPH_FILE);
        GREEDY_PATH = Graph.greedyPath(GRAPH);
        SHARED = new SharedPath(GREEDY_PATH);

        LongestPathController controller = new LongestPathController(DOMAIN, PORT);
        LocateRegistry.createRegistry(PORT).rebind(SERVICE, controller);

        new JobRunner(FRAME_TITLE, args, controller).run(TASK, SHARED, controller.getUrl());
    }

    private int[][] graph;
    private int node;
	private TaskLongestPath parentTask;

    public boolean[] visitedNodes;

    public TaskLongestPath() throws IOException {
        parentTask = null;
        node = -1;
        graph = Graph.graphForNodes(GRAPH_FILE);
        visitedNodes = new boolean[graph.length];
    }

    TaskLongestPath(TaskLongestPath parentTask, int node, int[][] graph, boolean[] visitedNodes) {
        this.node = node;
        this.graph = graph;
        this.parentTask = parentTask;
        this.visitedNodes = visitedNodes;
        visitedNodes[node] = true;
    }

    @Override
    public boolean isAtomic() {
        // Checked and should work
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
        Path base = new Path();
        base.addNewNode(node, 0);
        return new ReturnValuePath(this, base);
    }


    @Override
    public ReturnDecomposition divideAndConquer() {
        // Checked.
        List<Task> children = new LinkedList<>();
        if(parentTask == null) {
            for(int i = 0; i < graph.length; i++) {
                TaskLongestPath child = new TaskLongestPath(this, i, graph, visitedNodes);
                children.add(child);
            }
            return new ReturnDecomposition(new LongestPath(), children);
        } else {
            for(int i = 0; i < graph[node].length; i+=2) {
                if(!visitedNodes[graph[node][i]]) {
                    TaskLongestPath child = new TaskLongestPath(this, i, graph, visitedNodes);
                    children.add(child);
                }
            }
            return new ReturnDecomposition(new LongestPath(), children);
        }
    }

    private boolean isComplete() {
        for(int i = 0; i < graph.length; i++) {
            if(!visitedNodes[i]) {
                return false;
            }
        }
        return true;
    }

    private static Path findLongestPath(int srcNode, int[] neighbors, boolean[] visitedNodes, int[][] nodes) {
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
    }

}

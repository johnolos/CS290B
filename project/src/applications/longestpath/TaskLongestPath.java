
package applications.longestpath;

import api.*;
import api.events.EventListener;
import system.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TaskLongestPath extends TaskRecursive<Path> {

    // Configure job
    static final private File GRAPH_FILE = Paths.get(".", "res", "exampleGraph1.txt").toFile();
    static final private String FRAME_TITLE = "Longest Path Problem";
    static final private Task TASK = new TaskLongestPath();


    //static final private List<Integer> GREEDY_TOUR = EuclideanGraph.greedyTour(CITIES) ;
    //static private final double UPPER_BOUND = tourDistance( CITIES, GREEDY_TOUR );
    //static private final Shared SHARED = new SharedTour( GREEDY_TOUR, UPPER_BOUND );


    public static void main(String args[]) throws Exception {
        EventListener listener = new EventListener();
        LongestPathController handler = new LongestPathController();
        listener.register(handler);

        // Obviously not null on SHARED.
        new JobRunner(FRAME_TITLE, args, handler).run(TASK, null, listener);
    }

    private int[][] GRAPH;
    private int node;
    private Path partialPath;



    public TaskLongestPath() {
        partialPath = new Path(new ArrayList<>(), 0);
        try {
            GRAPH = Graph.graphForNodes(GRAPH_FILE);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    TaskLongestPath(TaskLongestPath parentTask, int node) {
        partialPath = new Path(parentTask.partialPath);
        partialPath.addNewNode(node, GRAPH[parentTask.node][node]);

    }

    @Override
    public boolean isAtomic() {
        return false;
    }

    @Override
    public ReturnValue<Path> solve() {
        return new ReturnValuePath(this, new Path());
    }

    @Override
    public ReturnDecomposition divideAndConquer() {
        // This is just a example of how it looks like and not an actually solution.

        List<Task> subtasks = new ArrayList<>();
        subtasks.add(new TaskLongestPath(this, 0));
        subtasks.add(new TaskLongestPath(this, 1));
        return new ReturnDecomposition(new LongestPath(), subtasks);
    }
}

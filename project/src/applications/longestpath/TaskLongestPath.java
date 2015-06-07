
package applications.longestpath;

import api.*;
import api.events.EventListener;
import system.Task;

import java.io.File;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class TaskLongestPath extends TaskRecursive<Path> {

    // Configure job
    static final private File GRAPH_FILE = Paths.get(".", "res", "exampleGraph1.txt").toFile();
    static final private String FRAME_TITLE = "Longest Path Problem";
    static final private Task TASK = new TaskLongestPath(null, 10, null, null);



    //static final private List<Integer> GREEDY_TOUR = EuclideanGraph.greedyTour(CITIES) ;
    //static private final double UPPER_BOUND = tourDistance( CITIES, GREEDY_TOUR );
    //static private final Shared SHARED = new SharedTour( GREEDY_TOUR, UPPER_BOUND );


    public static void main(String args[]) throws Exception {
        //EventListener listener = new EventListener();
        LongestPathController controller = new LongestPathController("localhost", 3292);
        //listener.register(controller);

        // Obviously not null on SHARED.
        new JobRunner(FRAME_TITLE, args, controller).run(TASK, null, null);
    }

    private int[][] graph;
    private int node;
    private Path currentPath;
    private Path tempPath;
	private TaskLongestPath parentTask;

    public boolean[] visitedNodes;

    public TaskLongestPath(TaskLongestPath parentTask, int node, int[][] graph, boolean[] visitedNodes) {
    	this.node = node; 
    	this.graph = graph; 
    	this.parentTask = parentTask;
        this.visitedNodes = visitedNodes;
    }

//    TaskLongestPath(TaskLongestPath parentTask, int node) {
//        
//    	//partialPath = new Path(parentTask.partialPath);
//        //partialPath.addNewNode(node, GRAPH[parentTask.node][node]);
//
//    }

    @Override
    public boolean isAtomic() {
        return false;
    }

    @Override
    public ReturnValue<Path> solve() {
        final Stack<TaskLongestPath> stack = new Stack<>();
        stack.push(this);
        // Shared something

        return new ReturnValuePath(this, new Path());
    }

    @Override
    public ReturnDecomposition divideAndConquer() {
        // Think this looks right
        List<Task> children = new LinkedList<>();
        for(int i = 0; i < graph.length; i++) {
            if(!visitedNodes[i]) {
                TaskLongestPath child = new TaskLongestPath(this, i, graph, visitedNodes);
                children.add(child);
            }

        }
        return new ReturnDecomposition(new LongestPath(), children);
    }

    private boolean isComplete() {
        for(int i = 0; i < graph.length; i++) {
            if(!visitedNodes[i]) {
                return false;
            }
        }
        return true;
    }
}

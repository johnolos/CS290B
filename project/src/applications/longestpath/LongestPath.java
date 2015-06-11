package applications.longestpath;

import api.ReturnValue;
import api.TaskCompose;

import java.util.List;

public class LongestPath extends TaskCompose<Path> {

    private int[][] graph;
    private int node;

    LongestPath(int[][] graph, int node) {
        this.graph = graph;
        this.node = node;
    }

    @Override
    public ReturnValue call() {
        List<Path> paths = args();
        if(node != -1) {
            for(Path path : paths) {
                int cost = getCost(graph, node, path.getLastNode());
                if(cost != -1) {
                    path.addNewNode(node, cost);
                }
            }
        }
        // Sorting results on largest path costs.
        paths.sort((path1, path2) -> path1.compareTo(path2));
        return new ReturnValuePath(this, paths.get(0));
    }

    public static int getCost(int[][] graph, int from, int to) {
        for(int i = 0; i < graph[from].length; i+=2) {
            if(graph[from][i] == to) {
                return graph[from][i+1];
            }
        }
        return -1;
    }

}

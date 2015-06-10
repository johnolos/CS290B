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
                path.addNewNode(node, graph[node][path.getLastNode()]);
            }
        }
        paths.sort((path1, path2) -> path1.compareTo(path2));
        return new ReturnValuePath(this, paths.get(0));
    }
}

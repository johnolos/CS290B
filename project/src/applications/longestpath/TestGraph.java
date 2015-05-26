package applications.longestpath;


import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class TestGraph {

    public static void main(String args[]) {
        int[][] graph;
        try {

            File file = Paths.get(".", "res", "exampleGraph1.txt").toFile();

            graph = Graph.graphForNodes(file);

            System.out.println();

            for(int i = 0; i < graph.length; i++) {
                if(graph[i].length > 0)
                    System.out.printf("Node %d edges:%n", i);
                for(int j = 0; j < graph[i].length; j+=2) {
                    System.out.printf("Node: %d\tWeight: %d%n", graph[i][j], graph[i][j+1]);
                }
                System.out.println();
            }

        } catch(IOException e) {
            e.printStackTrace();
        }

    }
}

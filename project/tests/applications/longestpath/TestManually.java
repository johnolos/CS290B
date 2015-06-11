package applications.longestpath;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class TestManually {

    public static void main(String args[]) throws IOException {
        testGraphicalUserInterface();
    }

    private static void testGraphicalUserInterface() throws IOException{
        JFrame frame = new JFrame();
        frame.setTitle("Test Graphic");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        int[][] graph = Graph.graphForNodes(new File("res/testgraph1.txt"));
        int[][] coordinates = Graph.coordinatesOfNodes(new File("res/testgraph1.txt"));

        List<Integer> intList = Arrays.asList(0, 1, 3, 4);
        int cost = 25;
        Path path = new Path(intList, cost, graph, coordinates);

        JLabel jLabel = Path.createGraphicImage(graph, coordinates, path);

        Container container = frame.getContentPane();
        container.setLayout( new BorderLayout() );
        container.add( new JScrollPane( jLabel), BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }


}

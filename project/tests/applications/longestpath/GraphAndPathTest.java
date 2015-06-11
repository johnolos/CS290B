package applications.longestpath;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import javax.swing.*;

public class GraphAndPathTest {

    @Test
    public void testCompareTo() throws Exception {
        List<Path> paths = new ArrayList<>();

        paths.add(new Path(new ArrayList<>(), 10.0));
        paths.add(new Path(new ArrayList<>(), 15.0));
        paths.add(new Path(new ArrayList<>(), 20.0));

        assertEquals(10.0, paths.get(0).cost(), 0.0);
        assertEquals(15.0, paths.get(1).cost(), 0.0);
        assertEquals(20.0, paths.get(2).cost(), 0.0);

        paths.sort((path1, path2) -> path1.compareTo(path2));

        assertEquals(20.0, paths.get(0).cost(), 0.0);
        assertEquals(15.0, paths.get(1).cost(), 0.0);
        assertEquals(10.0, paths.get(2).cost(), 0.0);
    }

    @Test
    public void testGreedyPath() throws Exception {
    	int[][] nodes = Graph.graphForNodes(new File("res/testgraph1.txt"));
    	Path res = Graph.greedyPath(nodes);
    	assertEquals(20.0, res.cost(), 0.0);

        nodes = Graph.graphForNodes(new File("res/testgraph2.txt"));
        res = Graph.greedyPath(nodes);
        assertEquals(31.0, res.cost(), 0.0);
    	
    }

    @Test
    public void testGraphForNodes() throws Exception {
        int[][] nodes = Graph.graphForNodes(new File("res/testgraph1.txt"));

        int[] node_0 = {1, 5};
        int[] node_1 = {0, 5, 2, 15, 3, 10};
        int[] node_2 = {1, 15};
        int[] node_3 = {1, 10, 4, 10};
        int[] node_4 = {3, 10};

        assertArrayEquals(node_0, nodes[0]);
        assertArrayEquals(node_1, nodes[1]);
        assertArrayEquals(node_2, nodes[2]);
        assertArrayEquals(node_3, nodes[3]);
        assertArrayEquals(node_4, nodes[4]);

    }

    @Test
    public void testCoordinatesOfNodes() throws Exception {
        int[][] coordinates = Graph.coordinatesOfNodes(new File("res/testgraph1.txt"));

        int[] node_0 = {100, 100};
        int[] node_1 = {100, 300};
        int[] node_2 = {500, 300};
        int[] node_3 = {150, 500};
        int[] node_4 = {500, 500};

        assertArrayEquals(node_0, coordinates[0]);
        assertArrayEquals(node_1, coordinates[1]);
        assertArrayEquals(node_2, coordinates[2]);
        assertArrayEquals(node_3, coordinates[3]);
        assertArrayEquals(node_4, coordinates[4]);

    }

    @Test
    public void testGraphicalInterface() throws Exception {
        JFrame frame = new JFrame();
        frame.setTitle("Test Graphic");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        int[][] graph = Graph.graphForNodes(new File("res/testgraph1.txt"));
        int[][] nodes = Graph.coordinatesOfNodes(new File("res/testgraph1.txt"));

        List<Integer> intList = Arrays.asList(0, 1, 3, 4);
        int cost = 25;
        Path path = new Path(intList, cost);

        JLabel jLabel = Path.createGraphicImage(graph, nodes, path);

        Container container = frame.getContentPane();
        container.setLayout( new BorderLayout() );
        container.add( new JScrollPane( jLabel), BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }

}
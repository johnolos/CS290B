package applications.longestpath;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Logic {
	
	static Graph graph = new Graph(); 
	static int nodes[][]; 
	static ArrayList<Integer> path = new ArrayList<>();
	
	static boolean[] visitedNodes;
			
	public Logic() {
		visitedNodes = new boolean[nodes.length];
	}
	
	public static void getNodes() {
		//File file = new File("res/exampleGraph1.txt");
		File file = new File("res/exampleGraph2.txt");
		try {
			nodes = graph.graphForNodes(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	

	
	public Path findLongestPath(int srcNode, int[] neighbors, boolean[] visitedNodes) {
		Path currentPath = new Path();
		double max = 0.0;
		visitedNodes[srcNode] = true;
		for (int i = 0; i < neighbors.length; i+=2) {
			int dest = neighbors[i]; 
			if (!visitedNodes[dest]) {
				Path tempPath = findLongestPath(dest, nodes[dest], visitedNodes);
				final double dist = neighbors[i+1] + tempPath.getCost();
				if (dist > max) {
					max = dist;
					currentPath = tempPath;
					currentPath.addNewNode(dest, dist);
				}
			}
		}
		
		visitedNodes[srcNode] = false;
		return currentPath;
	}
	
	
	
	
	
	
	
	public static void main(String[] args){
		getNodes();
		Logic l = new Logic();
		System.out.println("==============\n" + l.findLongestPath(0, nodes[0], l.visitedNodes));
		System.out.println("----------------");
		System.out.println(l.longestPath.getCost());
		System.out.println("----------------");
		
	}
	
	

}

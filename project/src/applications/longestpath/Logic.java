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
	
	

	
	Path longestPath = new Path(); 
	
	public Path findLongestPath(int srcNode, int[] neighbors, boolean[] visitedNodes) {
		double max = 0.0;
		visitedNodes[srcNode] = true; 
		for (int i = 0; i < neighbors.length; i+=2) {
			int dest = neighbors[i]; 
			if (!visitedNodes[dest]) {
				final double dist = (neighbors[i+1] + findLongestPath(dest, nodes[dest], visitedNodes).getCost());
				if (dist > max) {
					max = dist;  
				}
			}
		}
		
		visitedNodes[srcNode] = false;
		longestPath.setCost(max);
		return longestPath; 
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

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
		File file = new File("res/exampleGraph30Nodes.txt");
		try {
			nodes = graph.graphForNodes(file);
		} catch (IOException e) {
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
					currentPath = new Path(tempPath.getPath(), tempPath.getCost()); 
					currentPath.addNodeToPath(dest, dist); 
				}
			}
		}
		visitedNodes[srcNode] = false;
		return currentPath;
	}
	
	
	public Path findLongestSolution() {
		Path longestSolution = new Path(); 
		for (int n = 0; n < nodes.length; n++) {
			if (findLongestPath(n, nodes[n], visitedNodes).getCost() > longestSolution.getCost()) {
				longestSolution.setCost(findLongestPath(n, nodes[n], visitedNodes).getCost());
				ArrayList<Integer> templist = (ArrayList<Integer>) findLongestPath(n, nodes[n], visitedNodes).getPath(); 
				templist.add(n); 
				longestSolution.setPath(templist);
			}
		}
		return longestSolution; 
	}
	
	
	public static void main(String[] args){
		getNodes();
		Logic l = new Logic();
		
		Path longestSolution = l.findLongestSolution(); 
		System.out.println("----------------");
		System.out.println("Cost: " +  longestSolution.getCost());
		System.out.println("Path: ");
		for (int i = 0; i < longestSolution.getPath().size(); i++) {
			System.out.println(longestSolution.getPath().get(i));
		}
		
	}
	
	

}

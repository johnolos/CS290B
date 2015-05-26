package application.longestpath;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Scanner;

public class Graph {

	private static int numberOfNodes; 
	
	public static int[][] graphForNodes(String filename) throws FileNotFoundException{
		
		int nodes[][]; 
		
		try(Scanner in = new Scanner(new FileReader(filename))){
			
			numberOfNodes = in.hasNextInt() ? in.nextInt() : -1;
			
			if(numberOfNodes > 0) {
				nodes = new int[numberOfNodes][];
				
				for (int i = 0; i < numberOfNodes; i++) {
					nodes[i] = new int[0]; 
				}
	
				while (in.hasNextLine()) {
					int fromNode = in.nextInt(); 
					int toNode = in.nextInt();
					int value = in.nextInt(); 
					int arrayLength = nodes[fromNode].length; 
					int[] replacement = Arrays.copyOf(nodes[fromNode], arrayLength + 2);
					replacement[arrayLength] =  toNode; 
					replacement[arrayLength + 1] = value; 
					nodes[fromNode] = replacement;
				} in.close();
				
				// reduce any fragmentation
				int[][] newNodes = new int[numberOfNodes][];
				for (int i = 0; i < numberOfNodes; i++) {
					newNodes[i] = Arrays.copyOf(nodes[i], nodes[i].length);
				}
				return nodes; 
			}  
		} catch(FileNotFoundException e){}
		throw new FileNotFoundException("Format wrong");
	}
	

	
	
}

package applications.longestpath;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Graph {
	
	public static int[][] graphForNodes(File file) throws IOException {

		int numberOfNodes;
		
		int nodes[][];
		
		try(Scanner in = new Scanner(new FileReader(file))) {
			
			numberOfNodes = in.hasNextInt() ? in.nextInt() : -1;
			
			if(numberOfNodes > 0) {
				nodes = new int[numberOfNodes][];

				for (int i = 0; i < numberOfNodes; i++) {
					nodes[i] = new int[0];
				}

				while (in.hasNextLine()) {
					int fromNode, toNode, value;

					if(in.hasNextInt())
						fromNode = in.nextInt();
					else
						throw new WrongFileFormatException("Didn't find a fromNode integer value");
					if(in.hasNextInt())
						toNode = in.nextInt();
					else
						throw new WrongFileFormatException("Didn't find a toNode integer value");
					if(in.hasNextInt())
						value = in.nextInt();
					else
						throw new WrongFileFormatException("Didn't find a weight integer value");

					int arrayLength = nodes[fromNode].length;
					int[] replacement = Arrays.copyOf(nodes[fromNode], arrayLength + 2);
					replacement[arrayLength] = toNode;
					replacement[arrayLength + 1] = value;
					nodes[fromNode] = replacement;
				}
				in.close();

				// Reduce possible fragmentation.

				int[][] newNodes = new int[numberOfNodes][];
				for (int i = 0; i < numberOfNodes; i++) {
					newNodes[i] = Arrays.copyOf(nodes[i], nodes[i].length);
				}
				
				return nodes;
			}

		}

		throw new FileNotFoundException("Couldn't find file.");
	}

	public static Path greedyPath(int[][] graph) {
		int node = 0;
		boolean[] visited = new boolean[graph.length];
		List<Integer> path = new ArrayList<>();
		path.add(node);
		double cost = 0;
		visited[node] = true;
		while(true) {
			for(int i = 0; i < graph[node].length; i+=2) {
				if(!visited[graph[node][i]]) {
					cost += graph[node][i+1];
					node = graph[node][i];
					path.add(node);
					continue;
				}
			}
			break;
		}
		return new Path(path, cost);
	}

	public static class WrongFileFormatException extends IOException {
		public WrongFileFormatException(String msg) {
			super(msg);
		}
	}
	

	
	
}

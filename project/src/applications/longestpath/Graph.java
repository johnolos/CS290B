package applications.longestpath;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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

					int copyLength = nodes[fromNode].length;

					int[] arrayCopy = Arrays.copyOf(nodes[fromNode], copyLength + 2);

					arrayCopy[copyLength] = toNode;
					arrayCopy[copyLength + 1] = value;
					nodes[fromNode] = arrayCopy;
				}
				in.close();
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
		double totalCost = 0;
		double tempCost, cost;
		int index = -1;
		boolean altered;
		while(true) {
			cost = 0;
			altered = false;
			visited[node] = true;
			for(int i = 0; i < graph[node].length; i+=2) {
				if(!visited[graph[node][i]]) {
					tempCost = graph[node][i+1];
					if(tempCost > cost) {
						cost = tempCost;
						index = graph[node][i];
						altered = true;
					}
				}
			}
			if(altered) {
				totalCost += cost;
				path.add(index);
				node = index;
				continue;
			}
			break;
		}
		Collections.reverse(path);
		return new Path(path, totalCost);
	}


	public static int getCost(int[][] graph, int from, int to) {
		for(int i = 0; i < graph[from].length; i+=2) {
			if(graph[from][i] == to) {
				return graph[from][i+1];
			}
		}
		return -1;
	}

	public static class WrongFileFormatException extends IOException {
		public WrongFileFormatException(String msg) {
			super(msg);
		}
	}
	

	
	
}

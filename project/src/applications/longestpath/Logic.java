package applications.longestpath;

import java.io.File;
import java.io.IOException;

public class Logic {
	
	static Graph graph = new Graph(); 
	static int nodes[][]; 

	public static void getNodes() {
		File file = new File("res/exampleGraph1.txt");
		try {
			nodes = graph.graphForNodes(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public void findLongestPath() {
		
		
		
	}
	
	
	
	
	
	public static void main(String[] args){
		getNodes(); 
	}
	
	

}

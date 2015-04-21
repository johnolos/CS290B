package results;

import java.util.List;

import api.Result;

public class TSPResult extends Result<List<Integer>> {

	private final double distance; 
	
	public TSPResult(List<Integer> resultList, double distance, int id) {
		super(resultList, id, 0); //0 = time
		this.distance = distance; 
	}

	public double getDistance() {
		return distance;
	}
	
}

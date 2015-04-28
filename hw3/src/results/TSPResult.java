package results;

import java.util.List;
import java.util.UUID;

import api.Result;

public class TSPResult extends Result<List<Integer>> {

	private final double distance; 
	
	public TSPResult(List<Integer> resultList, double distance) {
		super(resultList);
		this.distance = distance; 
	}

	public double getDistance() {
		return distance;
	}
	
}

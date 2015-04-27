package results;

import java.util.List;
import java.util.UUID;

import api.Result;

public class TSPResult extends Result<List<Integer>> {

	private final double distance; 
	
	public TSPResult(int distance, UUID id, List<Integer> resultList) {
		super(id, resultList, distance); //0 = time
		this.distance = distance; 
	}

	public double getDistance() {
		return distance;
	}
	
}

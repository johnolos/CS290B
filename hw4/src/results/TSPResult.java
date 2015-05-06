package results;

import java.util.List;
import java.util.UUID;

import api.Result;

/**
 * Result class for TSP
 */
public class TSPResult extends Result<List<Integer>> {

	private final double distance; 
	
	/**
	 * Constructor for TSPResult
	 * @param resultList
	 * @param distance
	 */
	public TSPResult(List<Integer> resultList, double distance) {
		super(resultList);
		this.distance = distance; 
	}
	
	/**
	 * Returns the distance
	 * @return <double> distance
	 */
	public double getDistance() {
		return distance;
	}
}

package results;

import api.Result;

public class FibonacciResult extends Result<Integer> {

	private final int startCity; 
	private final double lenght; 
	
	
    public FibonacciResult(int taskReturnValue, int taskId) {
        super(taskReturnValue, taskId);
    }


}

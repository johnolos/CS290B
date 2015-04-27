package results;

import api.Result;

import java.util.UUID;

public class FibonacciResult extends Result<Integer> {

    public FibonacciResult(UUID taskAddrId, int taskReturnValue) {
        super(taskAddrId, taskReturnValue, 0);
    }


}

package tasks;

import api.Task;
import system.Computer;
import system.Core;

import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

/**
 * Task for computing fibonacci 
 */
public class FibonacciTask extends Task {

    private final int n;

    /**
     * Constructor for FibonacciTask
     * @param parentId
     * @param n
     */
    public FibonacciTask(UUID parentId, int n) {
        super(parentId);
        assert n > 0;
        this.n = n;
    }


    @Override
    /**
     * Executes the computation of the fibonacci.
     * @param <Computer> computer
     */
    public void execute(Core core) {
        if(n < 2) {
            core.setArg(getParentId(), n);
        } else {
            FibSum sum = new FibSum(getParentId());
            core.compute(sum);
            core.compute(new FibonacciTask(sum.getTaskId(), n - 1));
            core.compute(new FibonacciTask(sum.getTaskId(), n - 2));
        }
    }

    @Override
    public void addResult(Object result) {
    }

    @Override
    public boolean isReadyToExecute() {
        return true;
    }

}
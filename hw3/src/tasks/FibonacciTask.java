package tasks;

import api.Task;
import system.Computer;

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
     * @param <UUID> parentId
     * @param <int> n
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
    public void execute(Computer computer) throws RemoteException {
        if(n < 2) {
            computer.setArg(getParentId(), n);
        } else {
            FibSum sum = new FibSum(getParentId());
            computer.compute(sum);
            computer.compute(new FibonacciTask(sum.getTaskId(), n - 1));
            computer.compute(new FibonacciTask(sum.getTaskId(), n - 2));
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
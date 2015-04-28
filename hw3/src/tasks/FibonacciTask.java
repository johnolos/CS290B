package tasks;


import api.Task;
import system.Computer;

import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

public class FibonacciTask extends Task {

    private final int n;

    public FibonacciTask(UUID parentId, int n) {
        super(parentId);
        assert n > 0;
        this.n = n;
    }


    @Override
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
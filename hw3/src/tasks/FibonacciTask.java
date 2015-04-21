package tasks;


import api.Task;
import results.FibonacciResult;

import java.util.List;

public class FibonacciTask implements Task<FibonacciResult> {

    private final int parentId, taskId;


    public FibonacciTask(int parentId, int taskId) {
        this.parentId = parentId;
        this.taskId = taskId;
    }


    @Override
    public FibonacciResult compose() {
        return null;
    }

    @Override
    public List<Task<FibonacciResult>> decompose() {
        return null;
    }

    public void addResult(FibonacciResult result) {

    }

    @Override
    public boolean isReadyToCompose() {
        return false;
    }

    @Override
    public int getId() {
        return 0;
    }


}
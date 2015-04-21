package tasks;


import api.Task;
import results.FibonacciResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FibonacciTask implements Task<FibonacciResult> {

    private final int parentId, taskId;
    private List<Integer> subTaskIds;
    private Map<Integer, FibonacciResult> tHashMap;
    //private long startTime;


    public FibonacciTask(int parentId, int taskId) {
        this.parentId = parentId;
        this.taskId = taskId;
        tHashMap = new ConcurrentHashMap<Integer, FibonacciResult>();
    }


    @Override
    public FibonacciResult compose() {
        if(taskId >= 2) {
            FibonacciResult resultOne = tHashMap.get(taskId - 1);
            FibonacciResult resultTwo = tHashMap.get(taskId - 2);
            int resultValue = resultOne.getTaskReturnValue() + resultTwo.getTaskReturnValue();
            return new FibonacciResult(resultValue, taskId);
        }
        return new FibonacciResult(1, taskId);
    }

    @Override
    public List<Task<FibonacciResult>> decompose() {
        //startTime = System.currentTimeMillis();
        List<Task<FibonacciResult>> subTasks = new ArrayList<Task<FibonacciResult>>();

        if(taskId >= 2) {
            Task<FibonacciResult> firstSubTask = new FibonacciTask(this.taskId, this.taskId - 1);
            Task<FibonacciResult> secondSubTask = new FibonacciTask(this.taskId, this.taskId - 2);
            subTasks.add(firstSubTask);
            subTasks.add(secondSubTask);
            subTaskIds.add(taskId - 1);
            subTaskIds.add(taskId - 2);
        }

        return subTasks;
    }

    public void addResult(FibonacciResult result) {
        tHashMap.putIfAbsent(result.getTaskId(), result);
    }

    @Override
    public boolean isReadyToCompose() {
        if(taskId >= 2) {
            return tHashMap.size() == subTaskIds.size();
        } else if(taskId >= 0) {
            return true;
        }
        return false;
    }

    @Override
    public int getId() {
        return taskId;
    }

    public int getParentId() {
        return parentId;
    }


}
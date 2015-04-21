package api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Interface for a DAC Task API that can be executed by calling call method.
 * @param <T> the task return type.
 */
public abstract class Task2<T> implements Serializable {

    final int parentId;
    final int taskId;
    private List<Integer> subTaskIds;
    private Map<Integer, T> tHashMap;


    public Task2(int parentId, int taskId) {
        assert parentId > 0;
        assert taskId > 0;
        this.parentId = parentId;
        this.taskId = taskId;
        subTaskIds = new ArrayList<Integer>();
        tHashMap = new ConcurrentHashMap<Integer, T>();
    }

    /**
     * Method call to compose the subtasks and calculate this task's result.
     * @return result of task
     */
    abstract public T compose();

    /**
     * Method to decompose the task to smaller subtasks.
     * @return List of subtasks.
     */
    abstract public List<Task2<T>> decompose();

    public void addResult(Result<T> result) {
        tHashMap.putIfAbsent(result.getTaskId(), result.getTaskReturnValue());
    }


    public boolean isReadyToExecute() {
        return tHashMap.size() == subTaskIds.size();
    }



}

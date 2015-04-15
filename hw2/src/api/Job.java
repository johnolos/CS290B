package api;

// PeterCappello/cs290aHw1 github

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Job<T,V> implements Serializable {

    List<Task<T>> tasks;
    List<Result<V>> results;

    abstract void createTasks();

    abstract V calculateSolution();

    public Job() {
        tasks = new ArrayList<Task<T>>();
        results = new ArrayList<Result<V>>();
    }

    public void addResult(Result<V> result) {
        results.add(result);
    }

    public List<Task<T>> getTasks() {
        return tasks;
    }

    public int numOfTasks() {
        return tasks.size();
    }


}

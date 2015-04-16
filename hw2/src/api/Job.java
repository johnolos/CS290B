package api;

// PeterCappello/cs290aHw1 github

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class for Task T
 * @param <V> Return value from the task executed.
 * @param <R> Return value of the solution from the job
 */
public abstract class Job<V, R> implements Serializable {

    List<Task<V>> tasks;
    List<Result<V>> results;

    /**
     * Divides current Job into smaller task which is able to be computed in simultaneously.
     */
    abstract public void createTasks();

    /**
     * Calculates the solution of the job given the results. If not enough results are available an exception will
     * be thrown
     * @throws api.NotEnoughResultsException
     */
    abstract public R calculateSolution() throws NotEnoughResultsException;

    /**
     * Constructor of class Job
     * Initiates task-list and result-list.
     */
    public Job() {
        tasks = new ArrayList<Task<V>>();
        results = new ArrayList<Result<V>>();
    }

    /**
     * Adds a Result to the list of Results.
     * @param result Result of type V
     */
    public void addResult(Result<V> result) {
        results.add(result);
    }

    /**
     * Returns a List of Results.
     * @return List<Result<V>> List of Results of type V.
     */
    public List<Result<V>> getResults() {
        return results;
    }

    /**
     * Get a specific task given it's index.
     * @param i Index location of task
     * @return Task of type V
     */
    public Task<V> getTask(int i) {
        return tasks.get(i);
    }

    /**
     * Returns a List of Tasks
     * @return List<Task<V>> List of Tasks of type V
     */
    public List<Task<V>> getTasks() {
        return tasks;
    }

    /**
     * Adds a Task of type V to the list of tasks.
     * @param t Task of type V
     */
    public void addTask(Task<V> t) {
        tasks.add(t);
    }

    /**
     * Number of tasks
     * @return Number of tasks.
     */
    public int numOfTasks() {
        return tasks.size();
    }


}

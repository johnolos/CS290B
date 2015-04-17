package api;

import java.io.Serializable;
import java.util.concurrent.Callable;

/**
 * Interface for a Task that can be executed by calling call method.
 * @param <V> the task return type.
 */
public abstract class Task<V> implements Serializable, Callable<V> {

    /** Id of the task */
    private int id;
    /** Id of the Job the task is intended. */
    private String jobId;

    /**
     * Constructor for Task
     * @param jobId String id for Job it's intended.
     * @param id Id of the task itself.
     */
    public Task(String jobId, int id) {
        this.jobId = jobId;
        this.id = id;
    }

    /**
     * Get method to retrieve id.
     * @return Int id.
     */
    public int getId() {
        return id;
    }

    /**
     * Get method to retrieve JobId.
     * @return String JobId.
     */
    public String getJobId() {
        return jobId;
    }

    /**
     * Method call to execute task.
     * @return <V> Result of the task execution of type V
     */
    @Override
    abstract public V call();

}
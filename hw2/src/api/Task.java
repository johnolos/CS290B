package api;

import java.io.Serializable;
import java.util.concurrent.Callable;

/**
 *
 * @param <V> the task return type.
 */
public abstract class Task<V> implements Serializable, Callable<V> {

    private int id;
    private String jobId;

    public Task(String jobId, int id) {
        this.jobId = jobId;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getJobId() {
        return jobId;
    }

    @Override
    abstract public V call();
}
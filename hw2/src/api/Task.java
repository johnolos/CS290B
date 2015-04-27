package api;

import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * Interface for a Task that can be executed by calling call method.
 * @param <V> the task return type.
 */
public abstract class Task<V> implements Serializable, Callable<V> {

    /** Id of the task */
    private final UUID taskId;

    /** Id of parent task */
    private final UUID parentId;

    /**
     * Constructor for Task
     */
    public Task(UUID parentId) {
        this.taskId = UUID.randomUUID();
        this.parentId = parentId;
    }

    /**
     * Get method to retrieve id.
     * @return UUID taskId.
     */
    public UUID getId() {
        return taskId;
    }

    /**
     * Get method for parentId
     * @return UUID parentId
     */
    public UUID getParentId() { return parentId; }

    /**
     * Method call to execute task.
     * @return <V> Result of the task execution of type V
     */
    @Override
    abstract public V call();

}
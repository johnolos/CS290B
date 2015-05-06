package api;

import system.Computer;
import system.Core;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.UUID;

/**
 * Interface for a DAC Task API that can be executed by calling call method.
 * @param <T> the task return type.
 */
public abstract class Task<T> implements Serializable {

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

    abstract public void execute(Core core);

    /**
     * Add result to task.
     * @param result Result of a subtask to be added.
     */
    abstract public void addResult(T result);

    /**
     * Checks if task is ready to be composed.
     * @return Returns true if task is ready and false otherwise.
     */
    abstract public boolean isReadyToExecute();

    /**
     * Return task id.
     * @return <UUID> taskId Id of task.
     */
    public UUID getTaskId() { return taskId; }

    /**
     * Return parent id.
     * @return <UUID> parentId Id of parent task.
     */
    public UUID getParentId() {return parentId; }

}

package api;

import java.io.Serializable;
import java.util.List;

/**
 * Interface for a DAC Task API that can be executed by calling call method.
 * @param <T> the task return type.
 */
public interface Task<T> extends Serializable {

    /**
     * Method call to compose the subtasks and calculate this task's result.
     * @return result of task
     */
    public T compose();

    /**
     * Method to decompose the task to smaller subtasks.
     * @return List of subtasks.
     */
    public List<Task<T>> decompose();

    /**
     * Add result to task.
     * @param result Result of a subtask to be added.
     */
    public void addResult(Result<T> result);

    /**
     * Checks if task is ready to be composed.
     * @return Returns true if task is ready and false otherwise.
     */
    public boolean isReadyToCompose();

    /**
     * Return task id.
     * @return Id of task.
     */
    public int getId();



}

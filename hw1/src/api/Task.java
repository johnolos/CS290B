package api;

/**
 * Interface for a task. It is generalized in a way that the return type of the task is given by implementation.
 * @param <T> T object
 */
public interface Task<T> {

    /**
     * Task to be executed
     * @return T generalized object.
     */
    public abstract T execute();

}

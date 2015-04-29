package api;
import java.io.Serializable;
import java.util.UUID;

/**
 * Result class which is used after solving a task.
 * @param <T> type of return value of corresponding Task.
 */
public class Result<T> implements Serializable
{
    private final T taskReturnValue;

    /**
     * Constructor for Result
     * @param <T> taskReturnValue Return value of the Task.
     */
    public Result(T taskReturnValue)
    {
        assert taskReturnValue != null;
        this.taskReturnValue = taskReturnValue;
    }

    /**
     * Returns the return value of the task.
     * @return <T> Return value
     */
    public T getTaskReturnValue() { return taskReturnValue; }

}
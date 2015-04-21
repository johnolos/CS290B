package api;
import java.io.Serializable;

/**
 * Result class which is used after solving a task.
 * @param <T> type of return value of corresponding Task.
 */
public class Result<T> implements Serializable
{
    private final T taskReturnValue;
    private final int taskId;

    public Result(T taskReturnValue, int taskId)
    {
        assert taskReturnValue != null;
        assert taskId > 0;
        this.taskReturnValue = taskReturnValue;
        this.taskId = taskId;
    }

    public T getTaskReturnValue() { return taskReturnValue; }

    public int getTaskId() {
        return taskId;
    }



}
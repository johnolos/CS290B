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
    private final long time; 

    public Result(T taskReturnValue, int taskId, long time)
    {
        assert taskReturnValue != null;
        assert taskId > 0;
        assert time > 0; 
        this.taskReturnValue = taskReturnValue;
        this.taskId = taskId;
        this.time = time; 
    }

    public T getTaskReturnValue() { return taskReturnValue; }

    public int getTaskId() {
        return taskId;
    }
    
    public long getTime() {
    	return time; 
    }
 


}
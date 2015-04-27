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
    private final UUID taskAddrId;
    private final long time; 

    public Result(UUID taskAddrId, T taskReturnValue, long time)
    {
        assert taskAddrId != null;
        assert taskReturnValue != null;
        assert time > 0; 
        this.taskReturnValue = taskReturnValue;
        this.taskAddrId = taskAddrId;
        this.time = time; 
    }

    public T getTaskReturnValue() { return taskReturnValue; }

    public UUID getTaskAddrId() {
        return taskAddrId;
    }
    
    public long getTime() {
    	return time; 
    }
 


}
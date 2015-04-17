package api;
import java.io.Serializable;

/**
 * Result class which is used after solving a task.
 * @param <T> type of return value of corresponding Task.
 */
public class Result<T> implements Serializable
{
    /** Return value of the Result */
    private final T taskReturnValue;
    /** Time spent solving the task */
    private final long taskRunTime;
    /** Result id for corresponding task id */
    private final int id;
    /** JobId for the result */
    private final String jobId;

    /**
     * Constructor for Result
     * @param jobId String id job the result done for.
     * @param id Number of result corresponding for task
     * @param taskReturnValue Return value of the Task done
     * @param taskRunTime Time spent solving the task.
     */
    public Result(String jobId, int id, T taskReturnValue, long taskRunTime)
    {
        assert taskReturnValue != null;
        assert taskRunTime >= 0;
        assert id > 0;
        this.taskReturnValue = taskReturnValue;
        this.taskRunTime = taskRunTime;
        this.jobId = jobId;
        this.id = id;
    }

    /**
     * Get task return value
     * @return <T> Return value of type T
     */
    public T getTaskReturnValue() { return taskReturnValue; }

    /**
     * Get time spent solving the task
     * @return long Task runtime
     */
    public long getTaskRunTime() { return taskRunTime; }

    /**
     * Get id of the result.
     * @return Int id
     */
    public int getId() {
        return id;
    }

    /**
     * Get JobId for the Result
     * @return String JobId the result is intended.
     */
    public String getJobId() {
        return jobId;
    }

    /**
     * Convinient toString method to print execution time and and return value.
     * @return String information of the result object
     */
    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append( getClass() );
        stringBuilder.append( "\n\tExecution time:\n\t" ).append( taskRunTime );
        stringBuilder.append( "\n\tReturn value:\n\t" ).append( taskReturnValue.toString() );
        return stringBuilder.toString();
    }
}
package jobs;

import api.Job;
import api.Task;
import tasks.FibonacciTask;

/**
 * This task computes the fibonacci value for the nth fibonacci number. 
 */
public class FibonacciJob implements Job<Integer> {

    private final int number;
    private int value;

    /**
     * FibonacciJob - The job that computes the nth fibonacci number.
     * @param number The number in the fibonacci sequence to be computed.
     */
    public FibonacciJob(int number) {
        this.number = number;
        value = -1;
    }

    @Override
    /**
     * @return FibonacciTask
     */
    public Task runJob() {
        return new FibonacciTask(null, number);
    }

    @Override
    /**
     * @return value 
     */
    public Integer value() {
        return value;
    }

    @Override
    /**
     * Set method for 'value'
     */
    public void setValue(Integer value) {
        this.value = value;
    }

}

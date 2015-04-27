package jobs;

import api.Job;
import api.Task;
import tasks.FibonacciTask;

public class FibonacciJob implements Job<Integer> {

    private final int number;
    private int value;

    public FibonacciJob(int number) {
        this.number = number;
        value = -1;
    }

    @Override
    public Task runJob() {
        return new FibonacciTask(null, number);
    }

    @Override
    public Integer value() {
        return value;
    }

    @Override
    public void setValue(Integer value) {
        this.value = value;
    }

}

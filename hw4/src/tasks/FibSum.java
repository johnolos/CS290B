package tasks;

import api.Task;
import system.Computer;
import system.Core;

import java.rmi.RemoteException;
import java.util.UUID;

/**
 * Task class for the adding two fibonacci numbers.
 */
public class FibSum extends Task<Integer> {

    private int index = 0;
    private Integer[] values;
    private boolean isReady;

    /**
     * Constructor for FibSum
     */
    public FibSum(UUID dstAddr) {
        super(dstAddr);
        values = new Integer[2];
        isReady = false;

    }

    /**
     * Execute method.
     * @param core
     * @throws RemoteException
     */
    public void execute(Core core) {
        int sum = values[0] + values[1];
        core.setArg(getParentId(), sum);
    }

    @Override
    /**
     * Adds value to the array of values.
     * @param <Integer> value
     */
    public void addResult(Integer value) {
        if(index < values.length) {
            values[index++] = value;
            if(index == values.length)
                isReady = true;
        }
    }

    @Override
    public boolean isReadyToExecute() {
        return isReady;
    }

}

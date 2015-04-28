package tasks;

import api.Task;
import system.Computer;

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
     * FibSum
     * @param dstAddr
     */
    public FibSum(UUID dstAddr) {
        super(dstAddr);
        values = new Integer[2];
        isReady = false;

    }

    /**
     * Execute method.
     * @param computer
     * @throws RemoteException
     */
    public void execute(Computer computer) throws RemoteException {
        int sum = values[0] + values[1];
        computer.setArg(getParentId(), sum);
    }

    @Override
    /**
     * Adds value to the array of values.
     * @param value
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

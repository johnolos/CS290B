package tasks;

import api.Task;
import system.Computer;

import java.rmi.RemoteException;
import java.util.UUID;

public class Sum extends Task<Integer> {

    private int index = 0;
    private Integer[] values;
    private boolean isReady;

    public Sum(UUID dstAddr) {
        super(dstAddr);
        values = new Integer[2];
        isReady = false;

    }

    public void execute(Computer computer) throws RemoteException {
        int sum = values[0] + values[1];
        computer.setArg(getParentId(), sum);
    }

    @Override
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

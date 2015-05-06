package tasks;

import api.Task;
import results.TSPResult;
import system.Computer;
import system.Core;

import java.rmi.RemoteException;
import java.util.UUID;

/**
 * Task class for adding distances.
 */
public class TSPSum extends Task<TSPResult> {

    private int index = 0;
    private TSPResult[] values;
    private boolean isReady;

    /**
     * TSPSum
     * @param dstAddr
     * @param numOfResults
     */
    public TSPSum(UUID dstAddr, int numOfResults) {
        super(dstAddr);
        values = new TSPResult[numOfResults];
        isReady = false;

    }

    /**
     * Execute method.
     * @param core
     * @throws RemoteException 
     */
    public void execute(Core core) {
        double distance = values[0].getDistance();
        int tripIndex = 0;
        for(int i = 0; i < values.length; i++) {
            if(values[i].getDistance() < distance) {
                distance = values[i].getDistance();
                tripIndex = i;
            }
        }
        core.setArg(getParentId(), values[tripIndex]);
    }

    @Override
    /**
     * Adds the value to the array of values.
     * @param value
     */
    public void addResult(TSPResult value) {
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

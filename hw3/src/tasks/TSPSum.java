package tasks;

import api.Task;
import results.TSPResult;
import system.Computer;

import java.rmi.RemoteException;
import java.util.UUID;

public class TSPSum extends Task<TSPResult> {

    private int index = 0;
    private TSPResult[] values;
    private boolean isReady;

    public TSPSum(UUID dstAddr, int numOfResults) {
        super(dstAddr);
        values = new TSPResult[numOfResults];
        isReady = false;

    }

    public void execute(Computer computer) throws RemoteException {
        double distance = values[0].getDistance();
        int tripIndex = 0;
        for(int i = 0; i < values.length; i++) {
            if(values[i].getDistance() < distance) {
                distance = values[i].getDistance();
                tripIndex = i;
            }
        }
        computer.setArg(getParentId(), values[tripIndex]);
    }

    @Override
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

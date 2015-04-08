package com.homework1;

import java.rmi.RemoteException;

public class ComputerImpl implements Computer {


    @Override
    public <T> T execute(Task<T> t) throws RemoteException {
        return t.execute();
    }

}

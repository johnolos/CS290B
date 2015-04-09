package com.homework1;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Computer extends Remote {

    public static final int PORT = 1099;
    public static final String SERVICE_NAME = "bubbafett";

    <T> T execute(Task<T> t) throws RemoteException;

}

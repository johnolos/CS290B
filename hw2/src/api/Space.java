package api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import system.Computer;

public interface Space extends Remote
{
    public static int PORT = 2923;
    public final static int CAPACITY = 2000;
    public static String SERVICE_NAME = "Space";

    <T> void putAll (List<Task<T>> taskList ) throws RemoteException;

    Result take() throws RemoteException;

    <V> void put(Result<V> result) throws RemoteException;

    void exit() throws RemoteException;

    void register( Computer computer ) throws RemoteException;
}
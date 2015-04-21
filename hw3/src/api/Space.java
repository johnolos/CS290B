package api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import system.Computer;

/**
 * Space interface.
 * Space is a resource where Client can order Computers to compute for them.
 */
public interface Space extends Remote {

    /**
     * Port where Space is reachable.
     */
    public static int PORT = 2923;


    /**
     * Service name for Space.
     */
    public static String SERVICE_NAME = "Space";

    /**
     * putAll takes a list of tasks and handles the task accordingly
     * @param taskList List of Task
     * @param <T> Value of Task
     * @throws RemoteException
     */
    <T> void putAll (List<Task<T>> taskList ) throws RemoteException;

    /**
     * Take a result from the space
     * @return
     * @throws RemoteException
     */
    Result take() throws RemoteException;

    /**
     * Put a result on Space
     * @param result Result to be added
     * @param <V> Value of Result added.
     * @throws RemoteException
     */
    <V> void put(Result<V> result) throws RemoteException;

    /**
     * Order space to exit what is currently is doing.
     * @throws RemoteException
     */
    void exit() throws RemoteException;

    /**
     * Register a computer on the space which is available for computation.
     * @param computer Computer to be added
     * @throws RemoteException
     */
    void register( Computer computer ) throws RemoteException;
}
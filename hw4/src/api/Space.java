package api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import results.SetArg;
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
     * @param t Task
     * @throws RemoteException
     */
    public void put(Task t) throws RemoteException;

    public void putAllWaitQ(Collection<Task> t) throws RemoteException;

    public void putAllReadyQ(Collection<Task> t) throws RemoteException;

    public void setAllArgs(Collection<SetArg> setArgs) throws RemoteException;

    /**
     * take a result from the space
     * @return <Result>
     * @throws RemoteException
     */
    public Result take() throws RemoteException;

    /**
     * Order space to exit what is currently is doing.
     * @throws RemoteException
     */
    public void exit() throws RemoteException;

    /**
     * Register a computer on the space which is available for computation.
     * @param computer Computer to be added
     * @throws RemoteException
     */
    void register( Computer computer ) throws RemoteException;
}
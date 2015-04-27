package system;

import api.Result;
import api.Task;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

/**
 * Interface for a computer that is able to receive a task implementation and execute it.
 */
public interface Computer extends Remote {


    /**
     * Execute function for a task.
     * @param t Object input of a specific task.
     * @param <T> Specified return value for task to be executed.
     * @return V object.
     * @throws RemoteException
     */
    <T> void execute(Task<T> t) throws RemoteException;

    public <T> void compute(Task<T> t) throws RemoteException;

    public <T> void setArg(UUID id, T r) throws RemoteException;

    public void exit() throws RemoteException;

}

package system;

import api.Task;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface for a computer that is able to receive a task implementation and execute it.
 */
public interface Computer extends Remote {

    /**
     * Execute function for a task.
     * @param t Object input of a specific task.
     * @param <T> Specified return value for task to be executed.
     * @return T object.
     * @throws RemoteException
     */
    <T> T execute(Task<T> t) throws RemoteException;

}

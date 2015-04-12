package system;

import api.Task;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface for a computer that is able to receive a task implementation and execute it.
 */
public interface Computer extends Remote {

    /** PORT to listen too. **/
    public static final int PORT = 1099;
    /** SERVICE_NAME registered at NameService for RMI */
    public static final String SERVICE_NAME = "bubbafett";

    /**
     * Execute function for a task.
     * @param t Object input of a specific task.
     * @param <T> Specified return value for task to be executed.
     * @return T object.
     * @throws RemoteException
     */
    <T> T execute(Task<T> t) throws RemoteException;

}

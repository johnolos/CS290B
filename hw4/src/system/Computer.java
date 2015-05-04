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

    /**
     * 
     * @param <Task<T>> t The task to be computed.
     * @throws RemoteException
     */
    public <T> void compute(Task<T> t) throws RemoteException;

    /**
     * setArg sets results to subtasks. The argument is sent to the parent task which handles what it should do with it.
     * @param <UUID> id The Id of the task.
     * @param <T> r The result
     * @throws RemoteException
     */
    public <T> void setArg(UUID id, T r) throws RemoteException;

    /**
     * Exits the computer
     * @throws RemoteException
     */
    public void exit() throws RemoteException;

}
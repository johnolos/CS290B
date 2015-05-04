package system;

import api.Task;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

/**
 * Interface for a core on a computer that takes tasks the computer has and executes them.
 */
public interface Core extends Remote, Runnable {


    /**
     * Execute function for a task.
     * @param t Object input of a specific task.
     * @param <T> Specified return value for task to be executed.
     */
    <T> void execute(Task<T> t);

    /**
     *
     * @param t The task to be computed.
     */
    public <T> void compute(Task<T> t);

    /**
     * setArg sets results to subtasks. The argument is sent to the parent task which handles what it should do with it.
     * @param id The Id of the task.
     */


    public <T> void setArg(UUID id, T r);

    /**
     * Exits the computer
     * @throws java.rmi.RemoteException
     */
    public void exit();

}
package api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

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
     * @throws RemoteException
     */
    public void putAll (List<Task> taskList ) throws RemoteException;

    /**
     * put adds a task on Space
     * @param task Task to be added
     * @throws RemoteException
     */
    public void put(Task task) throws RemoteException;

    /**
     * putWaitQ puts a task to the wait queue
     * @param t The task to be added to the wait queue.
     * @throws RemoteException
     */
    public void putWaitQ(Task t) throws RemoteException;

    /**
     * putReadyQ puts a task to the ready queue
     * @param t The task to be added to the ready queue
     * @throws RemoteException
     */
    public void putReadyQ(Task t) throws RemoteException;

    /**
     * setArg sets results to subtasks. The argument is sent to the parent task which handles what it should do with it.
     * @param id The Id of the task.
     * @param <T> r The result
     * @throws RemoteException
     */
    public <T> void setArg(UUID id, T r) throws RemoteException;

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
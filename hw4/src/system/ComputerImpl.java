package system;

import api.Result;
import api.Space;
import api.Task;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ComputerImplementation of the interface given by Computer
 */
public class ComputerImpl extends UnicastRemoteObject implements Computer {

    private int numOfTasks = 0;
    private final Space space;

    /**
     * Constructor of ComputerImpl
     * @param <Space> space The space which computer relates to
     * @throws RemoteException
     */
    public ComputerImpl(Space space) throws RemoteException {
        this.space = space;
    }

    /**
     * Implementation of execute
     * @param <Task> t Task to be executed
     * @throws RemoteException
     */
    @Override
    public void execute(Task t) throws RemoteException {
        numOfTasks++;
        t.execute(this);
    }

    @Override
    /**
     * Puts task t on the space to be computed
     * @param <Task<T>> t The task to be computed
     */
    public <T> void compute(Task<T> t) throws RemoteException {
        if(t.isReadyToExecute()) {
            space.putReadyQ(t);
        } else {
            space.putWaitQ(t);
        }
    }

    @Override
    /**
     * setArg sets results to subtasks. The argument is sent to the parent task which handles what it should do with it.
     * @param <UUID> id The Id of the task.
     * @param <T> r The result
     * @throws RemoteException
     */
    public <T> void setArg(UUID id, T r) throws RemoteException {
        space.setArg(id, r);
    }


    @Override
    /**
     * Exits the computer
     * @throws RemoteException
     */
    public void exit() throws RemoteException {
        System.out.printf("Computer completed %d tasks.%n", numOfTasks);
        System.exit(0);
    }

    /**
     * Main function to start register and the name service and bind a stub of this computer to SERVICE_NAME given in
     * the computer interface.
     * @param <String[]> args unused
     * @throws RemoteException
     */
    public static void main(String[] args) throws Exception {

        if(System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        if(args.length < 1) {
            System.exit(-1);
        }

        String domain = args[0];

        String url = "rmi://" + domain + ":" + Space.PORT + "/" + Space.SERVICE_NAME;
        final Space space = (Space) Naming.lookup(url);
        space.register(new ComputerImpl(space));

        System.out.println("Computer initiated.");
    }

}

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

    public ComputerImpl(Space space) throws RemoteException {
        this.space = space;
    }

    /**
     * Implementation of execute
     * @param t Task to be executed
     * @throws java.rmi.RemoteException
     */
    @Override
    public void execute(Task t) throws RemoteException {
        numOfTasks++;
        t.execute(this);
    }

    @Override
    public <T> void compute(Task<T> t) throws RemoteException {
        if(t.isReadyToExecute()) {
            space.putReadyQ(t);
        } else {
            space.putWaitQ(t);
        }
    }

    @Override
    public <T> void setArg(UUID id, T r) throws RemoteException {
        space.setArg(id, r);
    }


    @Override
    public void exit() throws RemoteException {
        System.out.printf("Computer completed %d tasks.%n", numOfTasks);
        System.exit(0);
    }

    /**
     * Main function to start register and the name service and bind a stub of this computer to SERVICE_NAME given in
     * the computer interface.
     * @param args unused
     * @throws java.rmi.RemoteException
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

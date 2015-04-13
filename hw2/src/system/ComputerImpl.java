package system;

import api.Space;
import api.Task;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * ComputerImplementation of the interface given by Computer
 */
public class ComputerImpl extends UnicastRemoteObject implements Computer, Runnable {

    private String domain;
    private Space space;

    public ComputerImpl(String domain) throws RemoteException {
        this.domain = domain;
    }

    /**
     * Implementation of execute
     * @param t Task to be executed
     * @param <T> Result object of the task executed.
     * @return T object as result specified by the task.
     * @throws java.rmi.RemoteException
     */
    public <T> T execute(Task<T> t) throws RemoteException {
        return t.call();
    }

    /**
     * Main function to start register and the name service and bind a stub of this computer to SERVICE_NAME given in
     * the computer interface.
     * @param args unused
     * @throws java.rmi.RemoteException
     */
    public static void main(String[] args) throws Exception {

        System.setSecurityManager(new SecurityManager());

        String domain;
        if(args.length == 0) {
            domain = "localhost";
        } else {
            domain = args[0];
        }

        System.setProperty("java.rmi.server.hostname", domain);

        System.out.println(domain);

        Thread thread = new Thread(new ComputerImpl(domain));

        thread.start();

        System.out.println("Thread ComputerImpl.main Ready.");
    }

    @Override
    public void run() {
        try {

            String url = "rmi://" + domain + ":" + Space.PORT + "/" + Space.SERVICE_NAME;

            space = (domain == null) ? SpaceImpl.getInstance() : (Space) Naming.lookup(url);

            Computer stub = (Computer) UnicastRemoteObject.exportObject(this, 0);
            space.register(stub);

            System.out.println("Computer registered in Space");

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }
}

package system;

import api.Result;
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

    /**
     * String domain of the computer
     */
    private String domain;

    /**
     * Space
     */
    private Space space;

    /**
     * Computer id
     */
    private int id;

    /**
     * Number of computers to be created
     */
    private final static int NUMBER_OF_COMPUTERS = 10;

    /**
     * Base port for computers
     */
    private final static int BASE_PORT = 4350;

    /**
     * finished boolean value to exit while-loop in thread.
     */
    private boolean finished;

    /**
     * Computer stub for this ComputerImpl
     */
    private Computer stub;


    public ComputerImpl(String domain, int id) throws RemoteException {
        this.domain = domain;
        this.id = id;
        finished = false;
    }

    /**
     * Implementation of execute
     * @param t Task to be executed
     * @param <V> Result object of the task executed.
     * @return T object as result specified by the task.
     * @throws java.rmi.RemoteException
     */
    public <V> void execute(Task<V> t) throws RemoteException {
        long start = System.currentTimeMillis();
        V resultValue = t.call();
        long end = System.currentTimeMillis();
        Result<V> result = new Result<V>(t.getJobId(), t.getId(), resultValue, end - start);
        space.put(result);
        space.register(stub);
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

        String domain;
        if(args.length == 0) {
            domain = "localhost";
        } else {
            domain = args[0];
        }

        System.out.println(domain);

        int i = 0;
        while(i < NUMBER_OF_COMPUTERS) {
            Thread thread = new Thread(new ComputerImpl(domain, i));
            thread.start();
            i++;
        }

        System.out.println("ComputerImpl.Main: " + NUMBER_OF_COMPUTERS + " computer(s) has been initiated.");
    }

    @Override
    public void run() {
        try {
            String url = "rmi://" + domain + ":" + Space.PORT + "/" + Space.SERVICE_NAME;

            space = (Space) Naming.lookup(url);

            //space = (domain == null) ? SpaceImpl.getInstance() : (Space) Naming.lookup(url);

            UnicastRemoteObject.unexportObject(this, true);

            stub = (Computer) UnicastRemoteObject.exportObject(this, BASE_PORT + id);

            space.register(stub);

            System.out.println("Computer " + id + " registered in Space");

            while(!finished) {
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }
}

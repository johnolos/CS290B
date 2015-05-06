package system;

import api.Result;
import api.Space;
import api.Task;
import results.SetArg;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class implementation of the Space interface
 */
public class SpaceImpl extends UnicastRemoteObject implements Space {

    private TaskQueue readyQ;

    private TaskMap waitQ;

    private Result result;
    /**
     * Field to get space. Implements the singleton pattern.
     */
    private static SpaceImpl spaceImplInstance;

    /**
     * Computer Ids
     */
    private static int computerIds = 0;

    /**
     * Map of Computers to ComputerProxies
     */
    private final Map<Computer, ComputerProxy> computerProxies;


    /**
     * Constructor for SpaceImpl
     * @throws RemoteException
     */
    private SpaceImpl() throws RemoteException {
        readyQ = new TaskQueue();
        waitQ = new TaskMap();
        computerProxies = new ConcurrentHashMap<Computer, ComputerProxy>();
        spaceImplInstance = this;
        result = new Result(null);
        Logger.getLogger( SpaceImpl.class.getName() ).log( Level.INFO, "Space started." );
    }

    /**
     * Get method to get the running instance of SpaceImpl
     * @return <SpaceImpl> spaceImplInstance
     * @throws RemoteException
     */
    public static SpaceImpl getInstance() throws RemoteException {
        if(spaceImplInstance == null) {
            new SpaceImpl();
        }
        return spaceImplInstance;
    }

    @Override
    public void put(Task t) throws RemoteException {
        if(t.isReadyToExecute()) {
            readyQ.push(t);
        } else {
            waitQ.put(t);
        }
    }

    @Override
    public void putAllWaitQ(Collection<Task> tasks) throws RemoteException {
        for(Task t : tasks) {
            waitQ.put(t);
        }
    }


    @Override
    public void putAllReadyQ(Collection<Task> tasks) throws RemoteException {
        for(Task t : tasks) {
            readyQ.push(t);
        }
    }


    @Override
    public <T> void setAllArgs(Collection<SetArg<T>> setArgs) throws RemoteException {
        for(SetArg<T> setArg : setArgs) {
            if(setArg.getUUID() == null) {
                result = new Result<T>(setArg.getArg());
                System.out.println("Result has been found");
            } else {
                waitQ.setArg(setArg.getUUID(), setArg.getArg());

                Task t = waitQ.grabIfReady(setArg.getUUID());
                if(t != null)
                    readyQ.push(t);
            }
        }
    }

    @Override
    /**
     * Method to take a result from result queue
     * @return <Result> result
     * @throws RemoteException
     */
    public Result take() throws RemoteException {
        if(result.getTaskReturnValue() != null) {
            Result tempResult = result;
            readyQ.clear();
            waitQ.clear();
            result = new Result(null);
            return tempResult;
        }
        return result;
    }

    /**
     * exit closes the space
     * @throws RemoteException
     */
    @Override
    public void exit() throws RemoteException {
        for(ComputerProxy proxy : computerProxies.values())
            proxy.exit();
        System.exit(0);
    }

    /**
     * register computer on Space
     * @param computer The computer to be registered
     * @throws RemoteException
     */
    @Override
    public void register(Computer computer) throws RemoteException {
        final ComputerProxy computerProxy = new ComputerProxy(computer);
        computerProxies.put(computer, computerProxy);
        computerProxy.start();
        System.out.printf("Computer %d started.%n", computerProxy.computerId);
    }

    /**
     * Main method to run SpaceImpl.
     * Creates a thread of SpaceImpl and runs it.
     * @param  args domain
     * @thows Exception
     */
    public static void main(String[] args) throws Exception {

        if(System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        if(args.length < 1) {
            System.exit(-1);
        }

        String domain = args[0];

        System.setProperty("java.rmi.server.hostname", domain);

        SpaceImpl spaceImpl = getInstance();

        // Unexport to ensure no exceptions
        UnicastRemoteObject.unexportObject(spaceImpl, true);

        Space stub = (Space) UnicastRemoteObject.exportObject(spaceImpl, 0);

        Registry registry = LocateRegistry.createRegistry(Space.PORT);
        registry.rebind(Space.SERVICE_NAME, stub);

        System.out.println("SpaceImpl.main Registered and Ready.");
    }

    /**
     * 
     */
    private class ComputerProxy extends Thread implements Computer {
        final private Computer computer;
        final private int computerId = computerIds++;

        /**
         * Constructor of ComputerProxy
         * @param computer
         */
        ComputerProxy(Computer computer) {
            this.computer = computer;
        }

        @Override
        /**
         * execute task t
         * @param <Task> t
         * @throws RemoteException
         */
        public void execute(Task t) throws RemoteException {
            computer.execute(t);
        }


        @Override
        /**
         * exits the computer
         */
        public void exit() {
            try {
                computer.exit();
            } catch(RemoteException e) {
            }
        }
        
        /**
         * Run method
         */
        @Override
        public void run() {
            while(true) {
                Task task = null;
                try {
                    task = readyQ.pop();
                    execute(task);
                } catch(RemoteException ignore) {
                    readyQ.push(task);
                    computerProxies.remove(computer);
                    Logger.getLogger(SpaceImpl.class.getName()).log(Level.WARNING, "Computer {0} failed.", computerId);
                    break;
                } catch(InterruptedException e) {
                    Logger.getLogger(SpaceImpl.class.getName()).log(Level.WARNING, null, e);
                }
            }
        }
    }

}


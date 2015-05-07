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
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class implementation of the Space interface
 */
public class SpaceImpl extends UnicastRemoteObject implements Space {

    private BlockingQueue<Task> readyQ;

    private ConcurrentHashMap<UUID, Task> waitQ;

    private Result result;
    /**
     * Field to get space. Implements the singleton pattern.
     */
    private static SpaceImpl spaceImplInstance;

    /**
     * Computer Ids
     */
    private static int computerIds = 1;

    /**
     * Map of Computers to ComputerProxies
     */
    private final Map<Computer, ComputerProxy> computerProxies;

    /**
     * Constructor for SpaceImpl
     * @throws RemoteException
     */
    private SpaceImpl() throws RemoteException {
        readyQ = new LinkedBlockingQueue<Task>();
        waitQ = new ConcurrentHashMap<UUID, Task>();
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
            try {
                readyQ.put(t);
                logQueues();
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            waitQ.put(t.getTaskId(), t);
        }
    }

    @Override
    public void putAllWaitQ(Collection<Task> tasks) throws RemoteException {
        for(Task t : tasks) {
            waitQ.put(t.getTaskId(), t);
        }
    }

    @Override
    public void putAllReadyQ(Collection<Task> tasks) throws RemoteException {
        try {
            for(Task t : tasks) {
                readyQ.put(t);
            }
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setAllArgs(Collection<SetArg> setArgs) throws RemoteException {
        for(SetArg setArg : setArgs) {
            if (setArg.getUUID() == null) {
                result = new Result(setArg.getArg());
                System.out.println("Result has been found");
            } else {
                Task t = waitQ.get(setArg.getUUID());
                if (t != null) {
                    t.addResult(setArg.getArg());
                    if (t.isReadyToExecute()) {
                        waitQ.remove(t.getTaskId());
                        try {
                            readyQ.put(t);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    System.out.println("A SetArg got lost.");
                }
            }
        }
    }

    @Override
    public void reportTaskCompleted(Collection<UUID> taskIds) throws RemoteException {

    }

    private void logQueues() {
        System.out.printf("ReadyQ: %d.%n", readyQ.size());
        System.out.printf("WaitQ: %d.%n", waitQ.size());
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
        System.out.printf("Computer %d registered.%n", computerProxy.computerId);
    }

    /**
     * Unregister computer on Space
     * @param computer The computer to be registered
     * @throws RemoteException
     */
    @Override
    public void unregister(Computer computer) throws RemoteException {
        System.out.printf("Computer %d unregistered.%n", computerProxies.remove(computer).computerId);
    }

    /**
     * ComputerProxy
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
         * compute task t
         * @param <Task> t
         * @throws RemoteException
         */
        public void compute(Task t) throws RemoteException {
            computer.compute(t);
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
                    task = readyQ.take();
                    System.out.printf("Task picked up by: %d.%n", computerId);
                    compute(task);
                } catch(RemoteException ignore) {
                    try {
                        readyQ.put(task);
                    } catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                    computerProxies.remove(computer);
                    System.out.printf("Computer %d failed.%n", computerId);
                    break;
                } catch(InterruptedException e) {
                    Logger.getLogger(SpaceImpl.class.getName()).log(Level.WARNING, null, e);
                }
            }
        }
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

}


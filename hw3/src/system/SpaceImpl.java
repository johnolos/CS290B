package system;

import api.Result;
import api.Space;
import api.Task;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class implementation of the Space interface
 */
public class SpaceImpl extends UnicastRemoteObject implements Space {

    /**
     * Blocking queue of tasks to ensure thread-safe execution.
     */
    private ConcurrentHashMap<Integer, TaskProxy> taskProxies;

    /**
     * Blocking queue of results to ensure thread-safe execution.
     */
    private BlockingQueue<Result> results;

    /**
     * Blocking queue of computers to ensure thread-safe execution.
     */
    private BlockingQueue<Computer> computers;

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
    private final Map<Computer, ComputerProxy> computerProxies = new HashMap<Computer, ComputerProxy>();

    /**
     * Constructor for SpaceImpl
     * @throws RemoteException
     */
    private SpaceImpl() throws RemoteException {
        taskProxies = new ConcurrentHashMap<Integer, TaskProxy>();
        results = new LinkedBlockingQueue<Result>();
        computers = new LinkedBlockingQueue<Computer>();
        spaceImplInstance = this;
        Logger.getLogger( SpaceImpl.class.getName() ).log( Level.INFO, "Space started." );
    }

    /**
     * Get method to get the running instance of SpaceImpl
     * @return
     * @throws RemoteException
     */
    public static SpaceImpl getInstance() throws RemoteException {
        if(spaceImplInstance == null) {
            new SpaceImpl();
        }
        return spaceImplInstance;
    }

    /**
     * Help method to add multiple tasks to task queue.
     * @param taskList List of tasks to be added.
     * @param <T> Task result have value V.
     * @throws RemoteException
     */
    @Override
    public <T> void putAll(List<Task<T>> taskList) throws RemoteException {
        try {
            for(Task<T> t : taskList)
                tasks.put(t);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to take a Result from result queue.
     * @return Result
     * @throws RemoteException
     */
    @Override
    public Result take() throws RemoteException {
        try {
            return results.take();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Method to put result in result queue.
     * @param result Result to be added.
     * @param <V> Result has result value V.
     * @throws RemoteException
     */
    @Override
    public <V> void put(Result<V> result) throws RemoteException {
        try {
            results.put(result);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Close space
     * @throws RemoteException
     */
    @Override
    public void exit() throws RemoteException {
        for(ComputerProxy proxy : computerProxies.values())
            proxy.exit();
        System.exit(0);
    }

    /**
     * Register computer on Space
     * @param computer Computer to be registered
     * @throws RemoteException
     */
    @Override
    public void register(Computer computer) throws RemoteException {
        final ComputerProxy computerProxy = new ComputerProxy(computer);
        computerProxies.put(computer, computerProxy);
        computerProxy.start();
        Logger.getLogger(SpaceImpl.class.getName()).log(Level.INFO, "Computer {0} started.", computerProxy.computerId);
    }

    /**
     * Main method to run SpaceImpl.
     * Creates a thread of SpaceImpl and runs it.
     * @param args domain
     * @throws Exception
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

        System.setProperty("java.rmi.server.hostname", domain);

        SpaceImpl spaceImpl = getInstance();

        // Unexport to ensure no exceptions
        UnicastRemoteObject.unexportObject(spaceImpl, true);

        Space stub = (Space) UnicastRemoteObject.exportObject(spaceImpl, 0);

        Registry registry = LocateRegistry.createRegistry(Space.PORT);
        registry.rebind(Space.SERVICE_NAME, stub);

        System.out.println("SpaceImpl.main Registered and Ready.");
    }


    private class ComputerProxy extends Thread implements Computer {
        final private Computer computer;
        final private int computerId = computerIds++;

        ComputerProxy(Computer computer) {
            this.computer = computer;
        }

        @Override
        public Result execute(Task t) throws RemoteException {
            return computer.execute(t);
        }

        @Override
        public void exit() {
            try {
                computer.exit();
            } catch(RemoteException e) {
            }
        }

        @Override
        public void run() {
            while(true) {
                Task task = null;
                try {
                    task = tasks.take();
                    results.add(execute(task));
                } catch(RemoteException ignore) {
                    tasks.add(task);
                    computerProxies.remove(computer);
                    Logger.getLogger(SpaceImpl.class.getName()).log(Level.WARNING, "Computer {0} failed.", computerId);
                    break;
                } catch(InterruptedException e) {
                    Logger.getLogger(SpaceImpl.class.getName()).log(Level.INFO, null, e);
                }
            }
        }
    }

    private class TaskProxy<T> implements Task<T> {
        Task<T> task;

        TaskProxy(Task<T> task) {
            this.task = task;
        }

        @Override
        public T compose() {
            return task.compose();
        }

        @Override
        public List<Task<T>> decompose() {
            return task.decompose();
        }

        @Override
        public void addResult(Result<T> result) {
            task.addResult(result);
        }

        @Override
        public boolean isReadyToCompose() {
            return task.isReadyToCompose();
        }

        @Override
        public int getId() {
            return task.getId();
        }
    }
}


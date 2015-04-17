package system;

import api.Result;
import api.Space;
import api.Task;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Class implementation of the Space interface
 */
public class SpaceImpl extends UnicastRemoteObject implements Space, Runnable {

    /**
     * Blocking queue of tasks to ensure thread-safe execution.
     */
    private BlockingQueue<Task> tasks;

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
     * Sleep duration when space has nothing to do.
     */
    private long sleepDuration = 300;

    /**
     * Task termination time
     */
    private final static long TERMINATE = 600000;

    /**
     * Last action done by SpaceImpl.
     */
    private long lastActionTime;

    /**
     * Constructor for SpaceImpl
     * @throws RemoteException
     */
    private SpaceImpl() throws RemoteException {
        tasks = new LinkedBlockingQueue<Task>(Space.CAPACITY);
        results = new LinkedBlockingQueue<Result>(Space.CAPACITY);
        computers = new LinkedBlockingQueue<Computer>();
        spaceImplInstance = this;
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
            logTime();
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
            logTime();
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
            logTime();
            results.put(result);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Exit method for the space
     * @throws RemoteException
     */
    @Override
    public void exit() throws RemoteException {
        logTime();
        tasks.clear();
        results.clear();
    }

    /**
     * Register computer on Space
     * @param computer Computer to be registered
     * @throws RemoteException
     */
    @Override
    public void register(Computer computer) throws RemoteException {
        logTime();
        computers.add(computer);
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
        Thread thread = new Thread(spaceImpl);
        thread.start();

        // Unexport to ensure no exceptions
        UnicastRemoteObject.unexportObject(spaceImpl, true);

        Space stub = (Space) UnicastRemoteObject.exportObject(spaceImpl, 0);

        Registry registry = LocateRegistry.createRegistry(Space.PORT);
        registry.rebind(Space.SERVICE_NAME, stub);

        System.out.println("SpaceImpl.main Registered and Ready.");
    }

    /**
     * Log current time.
     */
    public void logTime() {
        lastActionTime = System.currentTimeMillis();
    }

    /**
     * Run method for the thread.
     * SpaceImpl will assign a task to a computer if there is one available.
     * It will terminate all tasks and result present if TERMINATE time in ms has passed.
     */
    @Override
    public void run() {
        // For as long Space operates.
        while(true) {
            try {
                // If there is a task and a computer; assign task to computer
                if(tasks.size() > 0 && computers.size() > 0) {
                    logTime();
                    Task t = tasks.take();
                    Computer computer = computers.take();
                    computer.execute(t);
                    System.out.println("Number of tasks left: " + tasks.size());
                    continue;
                }

                // Terminate tasks and results if something haven't happen for a while
                if(System.currentTimeMillis() > lastActionTime + TERMINATE) {
                    exit();
                }

                // Else sleep [ms]
                else {
                    Thread.sleep(sleepDuration);
                }
            } catch(InterruptedException e) {
                e.printStackTrace();
            } catch(RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}


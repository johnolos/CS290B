package system;

import api.Result;
import api.Space;
import api.Task;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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

    private int numOfJobsCompleed = 0;

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

    /**
     * Help method to add multiple tasks to task queue.
     * @param taskList List of tasks to be added.
     * @throws RemoteException
     */
    @Override
    public void putAll(List<Task> taskList) throws RemoteException {
        for (Task t : taskList) {
            readyQ.push(t);
        }
    }

    @Override
    /**
     * put puts the task on the ready queue
     * @param <Task> task 
     * @throws RemoteException
     */
    public void put(Task task) throws RemoteException {
        readyQ.push(task);
        // System.out.printf("%d tasks in ReadyQ.%n", readyQ.getSize());
    }


    @Override
    /**
     * putWaitQ puts the task on the wait queue
     * @param <Task> t 
     * @throws RemoteException
     */
    public void putWaitQ(Task t) throws RemoteException {
        waitQ.put(t);
        // System.out.printf("%d tasks in WaitQ.%n", waitQ.getSize());
    }

    @Override
    /**
     * putReadyQ puts the task on the ready queue
     * @param <Task> task 
     * @throws RemoteException
     */
    public void putReadyQ(Task t) throws RemoteException {
        readyQ.push(t);
        // System.out.printf("%d tasks in ReadyQ.%n", readyQ.getSize());
    }

    @Override
    /**
     * setArg sets results to subtasks. The argument is sent to the parent task which handles what it should do with it.
     * @param <UUID> id The Id of the task.
     * @param <T> r The result
     * @throws RemoteException
     */
    public <T> void setArg(UUID id, T r) throws RemoteException {
        if(id == null) {
            result = new Result<T>(r);
            System.out.println("Result has been found.");
            return;
        }

        waitQ.setArg(id, r);

        Task t = waitQ.grabIfReady(id);
        if(t != null) {
            putReadyQ(t);
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


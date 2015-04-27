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

    /**
     * Constructor for SpaceImpl
     * @throws RemoteException
     */
    private SpaceImpl() throws RemoteException {
        readyQ = new TaskQueue();
        waitQ = new TaskMap();
        computerProxies = new ConcurrentHashMap<Computer, ComputerProxy>();
        spaceImplInstance = this;
        result = new Result(null, null, 0);
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
        for (Task<T> t : taskList) {
            readyQ.push(t);
        }
    }

    @Override
    public <T> void put(Task task) throws RemoteException {
        readyQ.push(task);
        System.out.printf("%d tasks in ReadyQ.%n", readyQ.getSize());
    }


    @Override
    public <T> void putWaitQ(Task<T> t) throws RemoteException {
        waitQ.put(t);
        System.out.printf("%d tasks in WaitQ.%n", waitQ.getSize());
    }

    @Override
    public <T> void putReadyQ(Task<T> t) throws RemoteException {
        readyQ.push(t);
        System.out.printf("%d tasks in ReadyQ.%n", readyQ.getSize());
    }

    @Override
    public <T> void setArg(UUID id, T r) throws RemoteException {
        if(id == null) {
            result = new Result<T>(null, r, 0);
        }
        boolean value = waitQ.setArg(id, r);

        System.out.println("Boolean in setArg: " + value);

        if(waitQ.isReady(id)) {
            Task t = waitQ.getTask(id);
            putReadyQ(t);
        }
        
    }

    @Override
    public Result take() throws RemoteException {
        if(result.getTaskReturnValue() != null) {
            Result tempResult = result;
            readyQ.clear();
            waitQ.clear();
            result = new Result(null, null, 0);
            return tempResult;
        }
        return result;
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
        System.out.printf("Computer %d started.%n", computerProxy.computerId);
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


    private class ComputerProxy extends Thread implements Computer {
        final private Computer computer;
        final private int computerId = computerIds++;

        ComputerProxy(Computer computer) {
            this.computer = computer;
        }

        @Override
        public void execute(Task t) throws RemoteException {
            computer.execute(t);
        }

        @Override
        public <T> void compute(Task<T> t) throws RemoteException {
        }

        @Override
        public <T> void setArg(UUID id, T r) throws RemoteException {
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
                    task = readyQ.pop();
                    System.out.printf("Task acquired in computer %d.%n", computerId);
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


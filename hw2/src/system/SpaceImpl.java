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

public class SpaceImpl extends UnicastRemoteObject implements Space, Runnable {

    private BlockingQueue<Task> tasks;
    private BlockingQueue<Result> results;
    private BlockingQueue<Computer> computers;
    private static SpaceImpl spaceImplInstance;
    private long sleepDuration = 300;

    private SpaceImpl() throws RemoteException {
        tasks = new LinkedBlockingQueue<Task>(10);
        results = new LinkedBlockingQueue<Result>(10);
        computers = new LinkedBlockingQueue<Computer>();
        spaceImplInstance = this;
    }

    public static SpaceImpl getInstance() throws RemoteException {
        if(spaceImplInstance == null) {
            new SpaceImpl();
        }
        return spaceImplInstance;
    }

    @Override
    public void putAll(List<Task> taskList) throws RemoteException {
        try {
            for(Task t : taskList)
                tasks.put(t);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Result take() throws RemoteException {
        try {
            return results.take();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T> void put(Result<T> result) throws RemoteException {
        try {
            results.put(result);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exit() throws RemoteException {
    }

    @Override
    public void register(Computer computer) throws RemoteException {
        computers.add(computer);
    }

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

        SpaceImpl spaceImpl = new SpaceImpl();
        Thread thread = new Thread(spaceImpl);
        thread.start();

        // Unexport to ensure no exceptions
        UnicastRemoteObject.unexportObject(spaceImpl, true);

        Space stub = (Space) UnicastRemoteObject.exportObject(spaceImpl, 0);

        Registry registry = LocateRegistry.createRegistry(Space.PORT);
        registry.rebind(Space.SERVICE_NAME, stub);

        System.out.println("SpaceImpl.main Registered and Ready.");
    }

    @Override
    public void run() {
        // For as long Space operates.
        while(true) {
            try {
                // If there is a task and a computer; assign task to computer
                if(tasks.size() > 0 && computers.size() > 0) {
                    Task t = tasks.take();
                    Computer computer = computers.take();
                    computer.execute(t);
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


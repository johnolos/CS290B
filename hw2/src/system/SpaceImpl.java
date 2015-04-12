package system;

import api.Result;
import api.Space;
import api.Task;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class SpaceImpl extends UnicastRemoteObject implements Space {

    private BlockingQueue<Task> tasks;
    private BlockingQueue<Result> results;

    private static SpaceImpl spaceImplInstance;

    private SpaceImpl() throws RemoteException {
        tasks = new ArrayBlockingQueue<Task>(10);
        results = new ArrayBlockingQueue<Result>(10);
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
    public void exit() throws RemoteException {

    }

    @Override
    public void register(Computer computer) throws RemoteException {

    }
}

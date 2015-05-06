package system;

import api.Task;

import java.rmi.RemoteException;
import java.util.*;

/**
 * TaskMap:
 * Custom thread-safe map implementation that support seaching for task id.
 */
public class TaskMap {
    private Map<UUID, Task> map = new HashMap<UUID, Task>();
    private final Object lock = new Object();

    /**
     * put puts a task into the task map
     * @param task
     */
    public void put(Task task) {
        synchronized (lock) {
            map.put(task.getTaskId(), task);
            if(map.size() == 1) {
                lock.notify();
            }
        }
    }

    /**
     * setArg sets results to subtasks. The argument is sent to the parent task which handles what it should do with it.
     * @param id The Id of the task.
     * @param <T> r The result
     * @throws RemoteException
     */
    public <T> boolean setArg(UUID taskId, T arg) {
        synchronized (lock) {
            Task t = map.get(taskId);
            if(t != null) {
                t.addResult(arg);
                return true;
            }
            return false;
        }
    }

    /**
     * grabIfReady returns a task if it is ready
     * @param taskId
     * @return <Task> t
     */
    public Task grabIfReady(UUID taskId) {
        synchronized (lock) {
            Task t = map.get(taskId);
            if(t != null) {
                if(t.isReadyToExecute()) {
                    map.remove(taskId);
                    return t;
                }
            }
            return null;
        }
    }

    /**
     * clears the task map
     */
    public void clear() {
        synchronized (lock) {
            map.clear();
        }
    }
}

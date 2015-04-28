package system;

import api.Task;
import java.util.*;

/**
 * TaskMap:
 * Custom thread-safe map implementation that support seaching for task id.
 */
public class TaskMap {
    private Map<UUID, Task> map = new HashMap<UUID, Task>();
    private final Object lock = new Object();

    public void put(Task task) {
        synchronized (lock) {
            map.put(task.getTaskId(), task);
            lock.notifyAll();
        }
    }


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

    public boolean isReady(UUID taskId) {
        synchronized (lock) {
            Task t = map.get(taskId);
            if(t != null) {
                return t.isReadyToExecute();
            }
            return false;
        }
    }

    public Task getTask(UUID taskId) {
        synchronized (lock) {
            return map.remove(taskId);
        }
    }

    public int getSize() {
        return map.size();
    }

    public void clear() {
        synchronized (lock) {
            map.clear();
        }
    }
}

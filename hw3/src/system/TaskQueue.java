package system;

import api.Task;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.UUID;

/**
 * ThreadQueue:
 * Custom thread-safe queue implementation that support seaching for task id.
 */
public class TaskQueue {
    private LinkedList<Task> queue = new LinkedList<Task>();
    private final Object lock = new Object();

    public void push(Task task) {
        synchronized (lock) {
            queue.add(task);
            lock.notify();
        }
    }

    public Task pop() throws InterruptedException {
        synchronized (lock) {
            if(queue.isEmpty()) {
                lock.wait();
            }
            return queue.removeFirst();
        }
    }

    public Task getTask(UUID taskId) throws InterruptedException {
        synchronized (lock) {
            while(queue.isEmpty()) {
                lock.wait();
            }
            Iterator<Task> itr = queue.iterator();
            Task task;
            while(itr.hasNext()) {
                task = itr.next();
                if(task.getTaskId().equals(taskId)) {
                    itr.remove();
                    return task;
                }
            }
            return null;
        }
    }

/*    public <T> void setTaskArg(UUID taskId, T arg) {
        synchronized(lock) {
            Iterator<Task> itr = queue.iterator();
            Task task;
            while(itr.hasNext()) {
                task = itr.next();
                if(task.getTaskId().equals(taskId)) {
                    task.addResult(arg);
                }
            }
        }

    }*/

    public int getSize() {
        return queue.size();
    }

    public void clear() {
        synchronized (lock) {
            queue.clear();
        }
    }
}

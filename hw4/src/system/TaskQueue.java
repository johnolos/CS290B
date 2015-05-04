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

    /**
     * pushes a task into the task queue
     * @param <Task> task
     */
    public void push(Task task) {
        synchronized (lock) {
            queue.add(task);
            if(queue.size() == 1) {
                lock.notify();
            }
        }
    }

    /**
     * pops the first task in the task queue
     * @return <Task> 
     * @throws InterruptedException
     */
    public Task pop() throws InterruptedException {
        synchronized (lock) {
            while(queue.isEmpty()) {
                lock.wait();
            }
            return queue.removeFirst();
        }
    }

    /**
     * clears the task queue
     */
    public void clear() {
        synchronized (lock) {
            queue.clear();
        }
    }
}

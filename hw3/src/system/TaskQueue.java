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
            if(queue.size() == 1) {
                lock.notify();
            }
        }
    }

    public Task pop() throws InterruptedException {
        synchronized (lock) {
            while(queue.isEmpty()) {
                lock.wait();
            }
            return queue.removeFirst();
        }
    }


    public void clear() {
        synchronized (lock) {
            queue.clear();
        }
    }
}

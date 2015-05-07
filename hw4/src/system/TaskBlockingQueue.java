package system;

import api.Task;

import java.util.Stack;

public class TaskBlockingQueue {

    private Stack<Task> tasks;
    private int size;
    private Object lock = new Object();

    public TaskBlockingQueue() {
        tasks = new Stack<Task>();
        size = -1;
    }

    public TaskBlockingQueue(int size) {
        assert size > 0;
        tasks = new Stack<Task>();
        this.size = size;
    }

    public synchronized Task pop() {
        while(tasks.size() == 0) {
            try {
                wait();
            } catch(InterruptedException e) {

            }
        }
        Task t = tasks.pop();
        if(tasks.size() + 1 == size) {
            notifyAll();
        }
        return t;
    }

    public synchronized void push(Task t) {
        if(size == -1) {
            tasks.push(t);
        } else {
            while(tasks.size() > size) {
                try {
                    wait();
                } catch(InterruptedException e) {

                }
            }
            tasks.push(t);
        }
        if(tasks.size() == 1) {
            notifyAll();
        }
    }

    public int size() {
        return tasks.size();
    }



}

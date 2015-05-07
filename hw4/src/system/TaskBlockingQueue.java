package system;

import api.Task;

import java.util.Stack;

public class TaskBlockingQueue {

    private Stack<Task> tasks;

    public TaskBlockingQueue() {
        tasks = new Stack<Task>();
    }

    public synchronized Task pop() {
        while(tasks.size() == 0) {
            try {
                wait();
            } catch(InterruptedException e) {

            }
        }
        return tasks.pop();
    }

    public synchronized void push(Task t) {
        tasks.push(t);
        if(tasks.size() == 1) {
            notify();
        }
    }



}

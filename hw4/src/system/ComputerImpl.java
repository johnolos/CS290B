package system;

import api.Space;
import api.Task;
import results.SetArg;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * ComputerImplementation of the interface given by Computer
 */
public class ComputerImpl extends UnicastRemoteObject implements Computer {

    private final Space space;
    private BlockingQueue<Task> readyTaskQ;
    private BlockingQueue<Task> taskSpace;
    private BlockingQueue<Task> waitTaskQ;
    private BlockingQueue<SetArg> setArgQ;
    private final int availableProcessors;
    private List<Core> cores;
    private boolean exit = false;
    private boolean multiCore;
    private boolean ameliorateLatency;

    /**
     * Constructor of ComputerImpl
     * @param space The space which computer relates to
     * @throws RemoteException
     */
    public ComputerImpl(Space space, boolean ameliorateLatency, boolean multiCore) throws RemoteException {
        this.space = space;
        readyTaskQ = new LinkedBlockingDeque<Task>();
        waitTaskQ = new LinkedBlockingDeque<Task>();
        setArgQ = new LinkedBlockingDeque<SetArg>();
        taskSpace = new LinkedBlockingQueue<Task>();
        cores = new ArrayList<Core>();
        availableProcessors = Runtime.getRuntime().availableProcessors();
        this.multiCore = multiCore;
        this.ameliorateLatency = ameliorateLatency;
        if(multiCore) {
            int i = 0;
            while(i < availableProcessors) {
                cores.add(startCore(i));
                i++;
            }
        } else {
            cores.add(startCore(0));
        }
        System.out.println("Number of processors: " + availableProcessors);
        if(ameliorateLatency) {
            readyTaskQ = new LinkedBlockingDeque<Task>();
        } else {
            if(multiCore) {
                readyTaskQ = new LinkedBlockingDeque<Task>(availableProcessors);
            } else {
                readyTaskQ = new LinkedBlockingDeque<Task>(1);
            }
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("Shutdown hook 1");
            }
        });
    }

    private Core startCore(int i) {
        Core core = new CoreImpl(this, i);
        Thread t = new Thread(core);
        t.start();
        cores.add(core);
        return core;
    }

    public void run() {
        List<Task> tasks = new ArrayList<Task>();
        List<SetArg> setArgs = new ArrayList<SetArg>();
        long last = System.currentTimeMillis();
        while(!exit) {
            try {
                // WaitQTasks to space
                if(waitTaskQ.size() > 0) {
                    try {
                        while (waitTaskQ.size() > 0) {
                            tasks.add(waitTaskQ.take());
                        }
                    } catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                    space.putAllWaitQ(tasks);
                    tasks.clear();
                }

                // ReadyQTasks to space
                if(taskSpace.size() > 0) {
                    try {
                        while (taskSpace.size() > 0) {
                            tasks.add(taskSpace.take());
                        }
                    } catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                    space.putAllReadyQ(tasks);
                    tasks.clear();
                }

                // SetArgs to space
                if(setArgQ.size() > 0) {
                    try {
                        while (setArgQ.size() > 0) {
                            setArgs.add(setArgQ.take());
                        }
                    } catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                    space.setAllArgs(setArgs);
                    setArgs.clear();
                }

                if(System.currentTimeMillis() > last + 5000) {
                    last = System.currentTimeMillis();
                    logQueues();
                }

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Implementation of compute
     * @param t Task to be executed
     * @throws RemoteException
     */
    @Override
    public void compute(Task t) throws RemoteException {
        try {
            readyTaskQ.put(t);
            logQueues();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("LOL");
    }

    private void logQueues() {
        System.out.printf("ReadyQ: %d.%n", readyTaskQ.size());
        System.out.printf("WaitQ: %d.%n", waitTaskQ.size());
        System.out.printf("TaskSpace: %d.%n", taskSpace.size());
    }

    @Override
    /**
     * Exits the computer
     * @throws RemoteException
     */
    public void exit() throws RemoteException {
        System.out.printf("Computer completed %d tasks.%n", 0);
        System.exit(0);
    }


    class CoreImpl implements Core {
        final Computer computer;
        private int numOfTasks = 0;
        private final int id;
        private boolean exit;
        private Task task;

        private CoreImpl(Computer computer, int id) {
            this.computer = computer;
            this.id = id;
            exit = false;

            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    System.out.println("Shutdown hook ki - core");
                }
            });
        }

        @Override
        public void run() {
            System.out.printf("Core %d running.%n", id);
            while(!exit) {
                try {
                    task = readyTaskQ.take();
                    System.out.printf("Task taken on %d.%n", id);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
                execute(task);
                numOfTasks++;
                logQueues();
            }
        }

        @Override
        public void shutdownGracefully() {
            exit();
            if(task != null) {
                taskSpace.offer(task);
                System.out.printf("Computer Core %d recovered task before shutdown.%n", id);
                System.out.printf("Computer Core %d completed %d task(s) before it shutdown.%n", id, numOfTasks);
            }
        }

        @Override
        public <T> void execute(Task<T> t) {
            if(!exit) {
                t.execute(this);
            }
        }

        @Override
        public <T> void compute(Task<T> t) {
            if(exit)
                return;
            try {
                if(t.isReadyToExecute()) {
                    taskSpace.put(t);
                } else {
                    waitTaskQ.put(t);
                }
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public <T> void setArg(UUID id, T r) {
            setArgQ.offer(new SetArg(id, r));
        }

        @Override
        public void exit() {
            exit = true;
        }

        public int getNumberOfTasks() {
            return numOfTasks;
        }
    }

    /**
     * Main function to start register and the name service and bind a stub of this computer to SERVICE_NAME given in
     * the computer interface.
     * @param args unused
     * @throws RemoteException
     */
    public static void main(String[] args) throws Exception {

        if(System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        if(args.length < 3) {
            System.exit(-1);
        }
        String domain = args[0];
        boolean multicore = Boolean.valueOf(args[1]);
        boolean ameliorateLatency = Boolean.valueOf(args[2]);

        String url = "rmi://" + domain + ":" + Space.PORT + "/" + Space.SERVICE_NAME;
        final Space space = (Space) Naming.lookup(url);

        ComputerImpl computer = new ComputerImpl(space, ameliorateLatency, multicore);

        space.register(computer);

        computer.run();

        System.out.println("Computer initiated.");

        Runtime.getRuntime().gc();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("Shutdown hook");
            }
        });
    }

}

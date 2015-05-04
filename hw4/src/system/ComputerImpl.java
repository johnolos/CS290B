package system;

import api.Space;
import api.Task;
import results.SetArg;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * ComputerImplementation of the interface given by Computer
 */
public class ComputerImpl extends UnicastRemoteObject implements Computer {

    private final Space space;
    private BlockingQueue<Task> readyTaskQ;
    private BlockingQueue<Task> waitTaskQ;
    private BlockingQueue<SetArg> setArgQ;
    private final int availableProcessors;
    private List<Core> cores;

    /**
     * Constructor of ComputerImpl
     * @param space The space which computer relates to
     * @throws RemoteException
     */
    public ComputerImpl(Space space, boolean multiCore) throws RemoteException {
        this.space = space;
        readyTaskQ = new LinkedBlockingDeque<Task>();
        waitTaskQ = new LinkedBlockingDeque<Task>();
        setArgQ = new LinkedBlockingDeque<SetArg>();
        availableProcessors = Runtime.getRuntime().availableProcessors();
        if(multiCore) {
            int i = 0;
            while(i < availableProcessors) {
                cores.add(startCore());
            }
        } else {
            cores.add(startCore());
        }
    }

    private Core startCore() {
        Core core = new CoreImpl(this);
        Thread t = new Thread(core);
        t.start();
        cores.add(core);
        return core;
    }

    /**
     * Implementation of execute
     * @param t Task to be executed
     * @throws RemoteException
     */
    @Override
    public void execute(Task t) throws RemoteException {
        readyTaskQ.offer(t);
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
        private boolean exit;

        private CoreImpl(Computer computer) {
            this.computer = computer;
            exit = false;
        }

        @Override
        public void run() {
            Task task;
            while(!exit) {
                task = readyTaskQ.poll();
                execute(task);
                numOfTasks++;
            }

        }

        @Override
        public <T> void execute(Task<T> t) {
            t.execute(this);
        }

        @Override
        public <T> void compute(Task<T> t) {
            if(t.isReadyToExecute()) {
                readyTaskQ.offer(t);
            } else {
                waitTaskQ.offer(t);
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

        if(args.length < 2) {
            System.exit(-1);
        }
        String domain = args[0];
        boolean multicore = Boolean.valueOf(args[1]);

        String url = "rmi://" + domain + ":" + Space.PORT + "/" + Space.SERVICE_NAME;
        final Space space = (Space) Naming.lookup(url);
        space.register(new ComputerImpl(space, multicore));

        System.out.println("Computer initiated.");
    }

}

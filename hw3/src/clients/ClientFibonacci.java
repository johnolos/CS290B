package clients;

import api.Result;
import api.Space;
import api.Task;
import jobs.FibonacciJob;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientFibonacci {

    private Space space;

    private FibonacciJob job;

    private long start;
    private long end;
    private int number;


    public ClientFibonacci(String domain, int number) throws Exception {
        this.number = number;
        job = new FibonacciJob(number);
        String url = "rmi://" + domain + ":" + Space.PORT + "/" + Space.SERVICE_NAME;
        space = (Space) Naming.lookup(url);
    }

    public void run() {
        Logger.getLogger(ClientFibonacci.class.getName()).log(Level.INFO, "Calculating the fibonacci number {0}", number);
        start = System.currentTimeMillis();
        Task t = job.runJob();
        Integer value = null;
        try {
            space.put(t);
            while(value == null) {
                try {
                    Thread.sleep(300);
                } catch(InterruptedException e) {
                }
                Result result = space.take();
                value = (Integer)result.getTaskReturnValue();
            }
        } catch (RemoteException e) {
        }
        job.setValue(value);
        end = System.currentTimeMillis();
        System.out.printf("The fibonacci value of is %d.%n", value);
        System.out.printf("Calculation time: %d.%n", end - start);
    }


    public static void main(String args[]) {
        System.setSecurityManager( new SecurityManager() );

        if(args.length < 2) {
            System.exit(-1);
        }

        String domain = args[0];
        int number = Integer.valueOf(args[1]);

        try {
            ClientFibonacci clientFibonacci = new ClientFibonacci(domain, number);
            clientFibonacci.run();
        } catch(Exception e) {
        }

    }


}

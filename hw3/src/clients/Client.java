package clients;
import api.*;

import java.awt.BorderLayout;
import java.awt.Container;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

/**
 * Client class that has a job and executes it.
 * @param <S> return type of the job performed.
 * @param <T> return type the Task that this Client executes.
 */
public class Client<S, T> extends JFrame
{
    /** Job to be executed. */
    final protected Job<S, T> job;
    /** Space resource */
    final protected Space space;
    /** Return value of the job done of type R */
    protected S jobReturnValue;
    /** Time when client started **/
    private long clientStartTime;

    /**
     * Constructor for Client class
     * @param title Title
     * @param domainName String Domainname to reach Space on.
     * @param job <V,R> Job where V is the return type of task of that job
     * and R is the solution type of the job itself.
     * @throws RemoteException
     * @throws NotBoundException
     * @throws MalformedURLException
     */
    public Client( final String title, final String domainName, final Job<S, T> job )
            throws RemoteException, NotBoundException, MalformedURLException
    {
        this.job = job;
        setTitle( title );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        String url = "rmi://" + domainName + ":" + Space.PORT + "/" + Space.SERVICE_NAME;
        space = (Space) Naming.lookup(url);
    }

    /**
     * Begin time recording
     */
    public void begin() {
        clientStartTime = System.nanoTime();
    }

    /**
     * End time recording
     */
    public void end()
    {
        Logger.getLogger( Client.class.getCanonicalName() )
                .log(Level.INFO, "Client time: {0} ms.", ( System.nanoTime() - clientStartTime) / 1000000 );

        long sumOfTimes = 0;
        for(Result r : job.getResults()) {
            sumOfTimes+=r.getTaskRunTime();
        }

        Logger.getLogger( Client.class.getCanonicalName() )
            .log(Level.INFO, "Task times: {0} ms.", ( sumOfTimes ));
    }

    /**
     * Add label
     * @param jLabel Label to add.
     */
    public void add( final JLabel jLabel )
    {
        final Container container = getContentPane();
        container.setLayout( new BorderLayout() );
        container.add( new JScrollPane( jLabel ), BorderLayout.CENTER );
        pack();
        setVisible( true );
    }

    /**
     * Method to run the job.
     * @return R return type of job to be executed.
     * @throws RemoteException
     */
    public S runJob() throws RemoteException {
        job.createTasks();
        System.out.println("Client.runTasks: Sending " + job.numOfTasks() + " tasks.");
        space.putAll(job.getTasks());
        int numOfReceivedResults = 0;
        Result<T> partialResult;
        while(numOfReceivedResults < job.numOfTasks()) {
            partialResult = space.take();
            if(partialResult == null) {
                try {
                    Thread.sleep(100);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                ++numOfReceivedResults;
                System.out.println("Client.runTasks: Received result number: " + numOfReceivedResults);
                job.addResult(partialResult);
            }
        }
        S returnValue = null;
        try {
            returnValue = job.calculateSolution();
        } catch(NotEnoughResultsException e) {
            e.printStackTrace();
        }
        return returnValue;
    }
}
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
 *
 * @param <V> return type the Task that this Client executes.
 */
public class Client<V, R> extends JFrame
{
    final protected Job<V, R> job;
    final protected Space space;
    protected R jobReturnValue;
    private long clientStartTime;

    public Client( final String title, final String domainName, final Job<V, R> job )
            throws RemoteException, NotBoundException, MalformedURLException
    {
        this.job = job;
        setTitle( title );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        String url = "rmi://" + domainName + ":" + Space.PORT + "/" + Space.SERVICE_NAME;
        space = (Space) Naming.lookup(url);
    }

    public void begin() {
        clientStartTime = System.nanoTime();
    }

    public void end()
    {
        Logger.getLogger( Client.class.getCanonicalName() )
                .log(Level.INFO, "Client time: {0} ms.", ( System.nanoTime() - clientStartTime) / 1000000 );
    }

    public void add( final JLabel jLabel )
    {
        final Container container = getContentPane();
        container.setLayout( new BorderLayout() );
        container.add( new JScrollPane( jLabel ), BorderLayout.CENTER );
        pack();
        setVisible( true );
    }

    public V runTask() throws RemoteException
    {
        final long taskStartTime = System.nanoTime();
        final V value = null;
        final long taskRunTime = ( System.nanoTime() - taskStartTime ) / 1000000;
        //Logger.getLogger( Client.class.getCanonicalName() )
        //        .log(Level.INFO, "Task {0}Task time: {1} ms.", new Object[]{task, taskRunTime});
        return value;
    }


    public R runJob() throws RemoteException {
        job.createTasks();
        System.out.println("Client.runTasks: Sending " + job.numOfTasks() + " tasks.");
        space.putAll(job.getTasks());
        int numOfReceivedResults = 0;
        Result<V> partialResult;
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
        R returnValue = null;
        try {
            returnValue = job.calculateSolution();
        } catch(NotEnoughResultsException e) {
            e.printStackTrace();
        }
        return returnValue;
    }

}
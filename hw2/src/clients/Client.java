package clients;
import api.Job;
import api.Result;
import api.Space;
import api.Task;

import java.awt.BorderLayout;
import java.awt.Container;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

/**
 *
 * @param <T> return type the Task that this Client executes.
 */
public class Client<T, V> extends JFrame
{
    final protected Job<T, V> job;
    final private Space space;
    protected V jobReturnValue;
    private long clientStartTime;

    public Client( final String title, final String domainName, final Job<T, V> job )
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

    public T runTask() throws RemoteException
    {
        final long taskStartTime = System.nanoTime();
        final T value = null;
        final long taskRunTime = ( System.nanoTime() - taskStartTime ) / 1000000;
        //Logger.getLogger( Client.class.getCanonicalName() )
        //        .log(Level.INFO, "Task {0}Task time: {1} ms.", new Object[]{task, taskRunTime});
        return value;
    }


    public void runJob(List<Task> tasks) throws RemoteException {
        System.out.println("Client.runTasks: Sending " + tasks.size() + " tasks.");
        space.putAll(job.getTasks());
        int numOfReceivedResults = 0;
        Result<V> partialResult;
        while(numOfReceivedResults < job.numOfTasks()) {
            partialResult = space.take();
            if(partialResult == null) {
                try {
                    Thread.sleep(100);
                } catch(InterruptedException e) {
                }
            } else {
                ++numOfReceivedResults;
                System.out.println("Client.runTasks: Received result number: " + numOfReceivedResults);
                job.addResult(partialResult);
            }
        }
    }

}
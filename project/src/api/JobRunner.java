/*
 * The MIT License
 *
 * Copyright 2015 peter.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package api;

import api.events.EventController;
import api.events.EventControllerUrl;
import api.events.EventListener;
import api.events.EventView;
import system.Task;
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
import system.ComputerImpl;
import system.Configuration;
import system.SpaceImpl;

/**
 * The class used to "run" the Job.
 * @author Peter Cappello
 * @param <T> type of value returned by value.
 */
public class JobRunner<T> extends JFrame implements EventView<JLabel>
{
    final private Space  space;
    final private long   startTime = System.nanoTime();

    private EventController eventController = null;
    
    /**
     *
     * @param title the String to be displaced on the JPanel containing the JLabel.
     * @param args command line args - 0th element is Space domain name.
     * @throws RemoteException occurs if there is a communication problem or
     * the remote service is not responding
     * @throws NotBoundException There is no Space service bound in the RMI registry.
     * @throws MalformedURLException the URL provided for the Space RMI registry is malformed.
     */
    public JobRunner( String title, String[] args ) 
           throws RemoteException, NotBoundException, MalformedURLException
    { 
        System.setSecurityManager( new SecurityManager() );
        setTitle(title);
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        if ( args.length == 0 )
        {
            space = new SpaceImpl();
            int numComputers = Configuration.MULTI_COMPUTERS 
                             ? Runtime.getRuntime().availableProcessors() : 1;
            for ( int i = 0; i < numComputers; i++ )
            {
                space.register( new ComputerImpl( space ), SpaceImpl.PROXIES_PER_PROCESSOR * numComputers );
            }
        }
        else
        {
            final String url = "rmi://" 
                             + args[ 0 ] 
                             + ":" 
                             + Space.PORT 
                             + "/" 
                             + Space.SERVICE_NAME;
            space = (Space) Naming.lookup( url );
        }
    }

    public JobRunner(String title, String[] args, EventController eventController)
            throws RemoteException, NotBoundException, MalformedURLException {
        this(title, args);
        this.eventController = eventController;
        //eventController.register(this);
    }
    
    /**
     * Run the Job: Generate the tasks, retrieve the results, compose a solution
     * to the original problem, and display the solution.
     * @param task the task that defines the job.
     * @throws RemoteException occurs if there is a communication problem or
     * the remote service is not responding
     */
    public void run( final Task task ) throws RemoteException
    {
        ReturnValue<T> returnValue = space.compute( task );
        view( returnValue.view() );
        Logger.getLogger( this.getClass().getCanonicalName() )
              .log( Level.INFO, "Job run time: {0} ms.", ( System.nanoTime() - startTime ) / 1000000 );
    }
    
    /**
     * Run the Job: Generate the tasks, retrieve the results, compose a solution
     * to the original problem, and display the solution.
     * @param task the task that defines the job.
     * @param shared
     * @throws RemoteException occurs if there is a communication problem or
     * the remote service is not responding
     */
    public void run( final Task task, Shared shared ) throws RemoteException
    {
        view( space.compute( task, shared ).view() );
        Logger.getLogger( this.getClass().getCanonicalName() )
              .log( Level.INFO, "Job run time: {0} ms.", ( System.nanoTime() - startTime ) / 1000000 );
    }

    /**
     * Run the Job: Generate the tasks, retrieve the results, compose a solution
     * to the original problem, and display the solution.
     * @param task the task that defines the job.
     * @param shared
     * @param url to the EventController at the application side
     * @throws RemoteException occurs if there is a communication problem or
     * the remote service is not responding
     */
    public void run( final Task task, Shared shared, EventControllerUrl url) throws RemoteException
    {
        view( space.compute( task, shared, url).view() );
        Logger.getLogger(this.getClass().getCanonicalName())
                .log(Level.INFO, "Result found");
        Logger.getLogger( this.getClass().getCanonicalName() )
                .log( Level.INFO, "Job run time: {0} ms.", ( System.nanoTime() - startTime ) / 1000000 );
    }

    @Override
    public void view( final JLabel jLabel )
    {
        Container container = getContentPane();
        container.setLayout( new BorderLayout() );
        container.add( new JScrollPane( jLabel ), BorderLayout.CENTER );
        pack();
        setVisible( true );
    }

    @Override
    public void viewIfCapable(Object data) {
        try {
            view((JLabel)data);
        } catch(ClassCastException e) {
        }
    }


}

/*
 * The MIT License
 *
 * Copyright 2015 peter.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a replaceWith
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, replaceWith, modify, merge, publish, distribute, sublicense, and/or sell
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
package system;

import api.*;
import api.events.Event;
import api.events.EventControllerUrl;
import api.events.EventListener;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import static system.Configuration.EVENT_BROADCAST;
import static system.Configuration.SPACE_CALLABLE;

/**
 * SpaceImpl implements the space for coordinating sending/receiving Task and Result objects.
 * @author Peter Cappello
 */
public final class SpaceImpl extends UnicastRemoteObject implements Space
{
    static final public int PROXIES_PER_PROCESSOR = 2;
    static final public int FINAL_RETURN_VALUE = -1;
    static final private AtomicInteger computerIds = new AtomicInteger();
    
    final private AtomicInteger taskIds = new AtomicInteger();
    final private BlockingQueue<Task>     readyTaskQ = new LinkedBlockingQueue<>();
    final private BlockingQueue<ReturnValue> resultQ = new LinkedBlockingQueue<>();
    final private BlockingQueue<Event>        eventQ = new LinkedBlockingQueue<>();
    final private Map<Computer, ComputerProxy> computerProxies = Collections.synchronizedMap( new HashMap<>() );
    final private Map<UUID, TaskCompose>        waitingTaskMap = Collections.synchronizedMap( new HashMap<>() );
    final private AtomicInteger numTasks = new AtomicInteger();
    final private AtomicInteger numEvents = new AtomicInteger();
    final private ComputerImpl computerInternal;
    final private Boolean sharedLock = true;
          private UUID rootTaskReturnValue;
          private Shared shared = new NullShared();
          private EventListenerMap listenerMap;
          private long t1   = 0;
          private long tInf = 0;
    
    public SpaceImpl() throws RemoteException 
    {
        if ( SPACE_CALLABLE )
        {
            computerInternal = new ComputerImpl( this );
        }
        if ( EVENT_BROADCAST ) {
            listenerMap = new EventListenerMap();
            listenerMap.start();
        }
        Logger.getLogger( getClass().getName() )
              .log( Level.INFO, "Space started." );
    }
    
    public Computer computer() { return computerInternal; }
    
    /**
     * Compute a Task and return its Return.
     * To ensure that the correct Return is returned, 
     * this must be the only computation the Space is servicing.
     * 
     * @param rootTask task that encapsulates the overall computation.
     * @return the Task's Return object.
     */
    @Override
    public ReturnValue compute( Task rootTask )
    {
        initTimeMeasures();
        execute( rootTask );
        return take();
    }
    
    /**
     *
     * @param rootTask
     * @param shared
     * @return
     */
    @Override
    public ReturnValue compute( Task rootTask, Shared shared )
    {
        initTimeMeasures();
        this.shared = shared;
        execute( rootTask );
        ReturnValue result = take();
        reportTimeMeasures( result );
        return result;
    }

    @Override
    public ReturnValue compute(Task rootTask, Shared shared, EventControllerUrl url) throws RemoteException {
        initTimeMeasures();
        //this.shared = shared;
        EventListener listener = null;
        try {
            listener = (EventListener) Naming.lookup(url.url());
            listenerMap.addEventListener(listener);
        } catch(RemoteException|MalformedURLException|NotBoundException e) {
            Logger.getLogger( getClass().getName() )
                    .log( Level.WARNING, "Listener failed" );
        }
        execute(rootTask);
        ReturnValue result = take();
        if(listener != null)
            listenerMap.removeEventListener(listener);
        reportTimeMeasures(result);
        return result;
    }

    /**
     * Put a task into the Task queue.
     * @param rootTask
     */
    private void execute( Task rootTask ) 
    { 
//        rootTask.id( UUID.randomUUID() );
        rootTaskReturnValue = UUID.randomUUID();
        rootTask.composeId( rootTaskReturnValue );
        readyTaskQ.add( rootTask );
        addEvent(new Event(Event.Type.STATUS, "Task started"));
    }
    
    @Override
    synchronized public void putAll( final List<Task> taskList ) { readyTaskQ.addAll( taskList ); }

    /**
     * Take a Return from the Return queue.
     * @return a Return object.
     */
    @Override
    public ReturnValue take() 
    {
        try { return resultQ.take(); } 
        catch ( InterruptedException ignore ) 
        {
            Logger.getLogger( getClass().getName() )
                  .log(Level.INFO, null, ignore );
        }
        assert false; // should never reach this point
        return null;
    }
    
    public Shared shared() { return shared; }

    /**
     * Register Computer with Space.  
     * Will override existing key-value pair, if any.
     * @param computer
     * @param numProcessors
     * @throws RemoteException
     */
    @Override
    public void register( Computer computer, int numProcessors ) throws RemoteException
    {
        final ComputerProxy computerProxy = new ComputerProxy( computer, PROXIES_PER_PROCESSOR* numProcessors );
        computerProxies.put( computer, computerProxy );
        computerProxy.startWorkerProxies();
        Logger.getLogger( getClass().getName() )
              .log( Level.INFO, "Registered computer {0}.", computerProxy.computerId );    
    }
    
    /**
     *
     * @param args unused.
     * @throws Exception
     */
    public static void main( final String[] args ) throws Exception
    {
        System.setSecurityManager( new SecurityManager() );
        LocateRegistry.createRegistry( Space.PORT )
                      .rebind(Space.SERVICE_NAME, new SpaceImpl() );
    }

    synchronized public void processResult( final Task parentTask, final Return result )
    { 
        result.process( parentTask, this );
        shared = newerShared( result.shared() );
        t1 += result.taskRunTime();
        numTasks.getAndIncrement();
    }
    
    private Shared newerShared( final Shared that )
    {
        synchronized ( sharedLock )
        {
            return this.shared.isOlderThan( that ) ? that : this.shared;
        }
    }
    
    public int makeTaskId() { return taskIds.incrementAndGet(); }
    
    public TaskCompose getCompose( final UUID composeId ) { return waitingTaskMap.get( composeId ); }
            
    public void putCompose( final TaskCompose compose )
    {
        assert waitingTaskMap.get( compose.id() ) == null : compose.id(); 
        waitingTaskMap.put( compose.id(), compose );
        assert waitingTaskMap.get( compose.id() ) != null;
    }
    
    public void putReadyTask( final Task task ) 
    { 
        assert waitingTaskMap.get( task.composeId() ) != null || task.composeId() == rootTaskReturnValue : task.composeId();
        readyTaskQ.add( task ); 
    }

    public void putReadyTasks( final List<? extends Task> tasks ) {
        readyTaskQ.addAll( tasks );
    }
    
    public void removeWaitingTask( final UUID composeId ) { waitingTaskMap.remove( composeId ); }
    
    public void putResult( final ReturnValue result ) {
        resultQ.add( result );
        if(result.event() != null)
            addEvent(result.event());

    }

    
    public void tInf( final long tInf ) { this.tInf = tInf; }
    
    private void initTimeMeasures()
    {
        numTasks.getAndSet( 0 );
        t1 = 0;
        tInf = 0;
        numEvents.getAndSet( 0 );
    }
    
    public UUID rootTaskReturnValue() { return rootTaskReturnValue; }
    
    private void reportTimeMeasures( final Return result )
    {
        Logger.getLogger( getClass().getCanonicalName() )
              .log( Level.INFO, 
                    "\n\tTotal tasks: {0} \n\tT_1: {1}ms.\n\tT_inf: {2}ms.\n\tT_1 / T_inf: {3}\n\tTotal events: {4}",
                    new Object[]{ numTasks, result.t1() / 1000000, result.tInf() / 1000000, result.t1() / result.tInf(), numEvents } );
    }

    private void addEvent(Event event) {
        this.eventQ.add(event);
        numEvents.getAndIncrement();
    }


    private class EventListenerMap extends Thread {

        private Map<EventListener, EventListenerProxy> listenerProxies =
                Collections.synchronizedMap(new HashMap<>());

        @Override
        public void run() {
            Logger.getLogger( getClass().getName() )
                    .log( Level.INFO, "ListenerMap started." );
            while(true) {
                Event event = null;
                try {
                    Logger.getLogger(getClass().getName()).log(Level.INFO,
                            String.format("There are %d events in eventQ.%n", eventQ.size()));
                    event = eventQ.take();
                    notifyAll(event);
                }
                catch ( InterruptedException ex ) {
                    Logger.getLogger( getClass().getName() )
                            .log( Level.INFO, null, ex );
                }
            }
        }

        protected void notifyAll(Event event) {
            for(EventListenerProxy listener : listenerProxies.values()) {
                try {
                    listener.notify(event);
                } catch (RemoteException e) {
                    listenerProxies.remove(listener);
                }
            }
        }

        protected void addEventListener(final EventListener listener) {
            final EventListenerProxy listenerProxy = new EventListenerProxy(listener);
            listenerProxies.put(listener, listenerProxy);
        }

        protected void removeEventListener(final EventListener listener) {
            listenerProxies.remove(listener);
        }

        private class EventListenerProxy implements EventListener {
            private EventListener eventListener;

            private EventListenerProxy(EventListener eventListener) {
                this.eventListener = eventListener;
            }

            @Override
            public void notify(Event event) throws RemoteException {
                eventListener.notify(event);
            }
        }

    }
    
    private class ComputerProxy
    {
        final private Computer computer;
        final private int computerId = computerIds.getAndIncrement();
        final private Map<Integer, WorkerProxy> workerMap = new HashMap<>();

        ComputerProxy( final Computer computer, final int numWorkerProxies )
        { 
            this.computer = computer;
            for ( int id = 0; id < numWorkerProxies; id++ )
            {
                WorkerProxy workerProxy = new WorkerProxy( id );
                workerMap.put( id, workerProxy );
            }
        }
        
        private void startWorkerProxies()
        {
            for ( WorkerProxy workerProxy : workerMap.values() )
            {
                workerProxy.start();
            }
        }
        
        private void unregister( final Task task, final Computer computer, final int workerProxyId )
        {
            readyTaskQ.add( task );
            workerMap.remove( workerProxyId );
            Logger.getLogger( getClass().getName() )
                  .log( Level.WARNING, "Computer {0}: Worker failed.", workerProxyId );
            if ( workerMap.isEmpty() )
            {
                computerProxies.remove( computer );
                Logger.getLogger( getClass().getName() )
                      .log( Level.WARNING, "Computer {0} failed.", computerId );
            }
        }
             
        private class WorkerProxy extends Thread
        {
            final private Integer id;
            
            private WorkerProxy( final int id ) { this.id = id; }
            
            @Override
            public void run()
            {
                while ( true )
                {
                    Task task = null;
                    try 
                    { 
                        task = readyTaskQ.take();
                        processResult( task, computer.execute( task, shared ) );
                    }
                    catch ( RemoteException ignore )
                    {
                        unregister( task, computer, id );
                        ignore.printStackTrace();
                        return;
                    } 
                    catch ( InterruptedException ex ) 
                    { 
                        Logger.getLogger( getClass().getName() )
                              .log( Level.INFO, null, ex ); 
                    }
                }
            }   
        }
    }
}

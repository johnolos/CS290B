/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package api;

import java.rmi.RemoteException;
import system.Task;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import system.Configuration;
import system.SpaceImpl;

/**
 *
 * @author Peter Cappello
 * @param <I> input type.
 */
public abstract class TaskCompose<I> extends Task
{
    private int numUnsetArgs;
    private List<I> args;
    private long decomposeTaskRunTime;
    private long sumChildT1;
    private long maxChildTinf;
    
    @Override
    abstract public ReturnValue call();
    
    /**
     *
     * @return the List of inputs.
     */
    synchronized public List<I> args() { return args; }
    
    /**
     * Set one of this task's inputs.
     * @param argNum the index of this input.
     * @param argValue the value of this input.
     * @param space if this is the last input this task is waiting for, put
     * the task in the space's ready task queue; remove it from the waiting task map.
     */
    synchronized public void arg( final int argNum, final I argValue, SpaceImpl space ) 
    { 
        assert numUnsetArgs > 0 &&  argValue != null && args.get( argNum ) == null; 
        args.set( argNum, argValue );
        if ( --numUnsetArgs == 0 )
        {
            if ( Configuration.SPACE_CALLABLE )
            {
                try 
                { 
                    // assumes TaskCompose is SPACE_CALLABLE.
                    space.processResult( this, space.computer().execute( this, space.shared() ) );
                }
                catch ( RemoteException ignore ) {} 
            }
            else
            {
                space.putReadyTask( this );
            }
            space.removeWaitingTask( id() );
        }
    }
    
    synchronized public void numArgs( int numArgs )
    {
        assert numArgs >= 0;
        numUnsetArgs = numArgs;
        args = Collections.synchronizedList( new ArrayList<>( numArgs ) ) ;
        for ( int i = 0; i < numArgs; i++ )
        {
            args.add( null );
        }
        assert args.size() == numArgs;
    }
        
    public void decomposeTaskRunTime( long time ) { decomposeTaskRunTime = time; }
    public long decomposeTaskRunTime() { return decomposeTaskRunTime; }
    
    public long sumChildT1() { return sumChildT1; }
    public void sumChildT1( long time ) { sumChildT1 +=  time; }
    
    public long maxChildTInf() { return maxChildTinf; }
    public void maxChildTInf( long time ) { maxChildTinf = maxChildTinf < time ? time : maxChildTinf; }
}

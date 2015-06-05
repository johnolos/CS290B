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
package system;

import api.Shared;
import api.events.Event;

import java.io.Serializable;

/**
 * Time measures:
 * T1 = atomic task ? Telapsed : Telapsed.decompose + sum{ T1.child } + T1.compose
 * Tinf = atomic task ? Telapsed : Telapsed.decompose + max{ Tinf.child } + Tinf.compose
 * @author Peter Cappello
 */
abstract public class Return implements Serializable
{
    private long taskRunTime;   // elapsed time
    private long t1;            // work
    private long tInf;          // critical path length
    private Shared shared;
    
    public Shared shared() { return shared; }
    public Return shared( final Shared shared )
    { 
        this.shared = shared; 
        return this; 
    }
    
    public long taskRunTime() { return taskRunTime; }
    public Return taskRunTime( final long taskRunTime ) 
    { 
        this.taskRunTime = taskRunTime; 
        return this; 
    }
    
    public long t1() { return t1; }
    public void t1( final long t1 ) { this.t1 = t1; }
    public long tInf() { return tInf; }
    public void tInf( final long tInf ) { this.tInf = tInf; }
    
    public Return setIds( Task parentTask ){ return this; }
        
    /**
     *
     * @param parentTask
     * @param space the receiving the Temp objects.
     */
    abstract public void process( final Task parentTask, final SpaceImpl space );
    
    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append( getClass() );
        stringBuilder.append( "\n\tExecution time:\t" ).append( taskRunTime );
        stringBuilder.append( "\n\tT1 time:\t" ).append( t1 );
        stringBuilder.append( "\n\tTInf time:\t" ).append( tInf );
        return stringBuilder.toString();
    }
}

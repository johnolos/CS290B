/*
 * The MIT License
 *
 * Copyright 2015 Peter Cappello.
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

import java.util.UUID;
import javax.swing.JLabel;
import system.Task;
import system.Return;
import system.SpaceImpl;

/**
 *
 * @author Peter Cappello
 * @param <T>
 */
abstract public class ReturnValue<T> extends Return
{    
    final private UUID composeId;
    final private int composeArgNum;
    final private T value;
    
    public ReturnValue( final Task task, final T value ) 
    { 
        assert task != null;
        composeId = task.composeId();
        composeArgNum = task.composeArgNum();
        this.value = value; 
    }
    
    public T value() { return value; }
    
    /**
     * Update the taskCompose task that is waiting for this input.
     * @param associatedTask unused - the task whose Result is to be processed.
     * @param space containing the taskCompose task that is waiting for this value.
     */
    @Override
    public void process( final Task associatedTask, final SpaceImpl space )
    {
        if ( associatedTask instanceof TaskCompose )
        {
            final TaskCompose task = (TaskCompose) associatedTask;
            final long commonTime = task.decomposeTaskRunTime() + taskRunTime();
            t1(   commonTime + task.sumChildT1() );
            tInf( commonTime + task.maxChildTInf() );
        }
        else
        {
            t1(   taskRunTime() );
            tInf( taskRunTime() );
        }
        if ( composeId.equals( space.rootTaskReturnValue() ) )
        {
            space.tInf( tInf() );
            space.putResult( this );
            return;
        }
        final TaskCompose taskCompose = space.getCompose( composeId );
        assert taskCompose != null;
        taskCompose.arg( composeArgNum, value, space );
        taskCompose.sumChildT1( t1() );
        taskCompose.maxChildTInf( tInf() );
    }
    
    abstract public JLabel view();
}

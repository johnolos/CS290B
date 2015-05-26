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
import api.Space;
import api.TaskCompose;
import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 *
 * @author Peter Cappello
 */
abstract public class Task implements Serializable, Callable<Return> 
{ 
    private UUID id;
    private UUID composeId;
    private int composeArgNum;
    private final Boolean sharedLock = true;
    private Shared shared;
    protected Space space;
    
    @Override
    abstract public Return call(); 
        
    public UUID  id() { return id; }
    public void id( final UUID id ) { this.id = id; }
    
    public int  composeArgNum() { return composeArgNum; }
    public void composeArgNum( final int composeArgNum ) { this.composeArgNum = composeArgNum; }
    
    public UUID  composeId() { return composeId; }
    public void composeId( final UUID composeId ) { this.composeId = composeId; }
        
    public Shared shared() { return shared; }
    public Task   shared( final Shared shared ) 
    { 
        this.shared = newerShared( shared ); 
        return this;
    }
    
    private Shared newerShared( final Shared that )
    {
        synchronized( sharedLock )
        {
            return this.shared == null || this.shared.isOlderThan( that ) ? that : this.shared;
        }
    }
    
    public boolean isSpaceCallable() { return this instanceof TaskCompose; }
}

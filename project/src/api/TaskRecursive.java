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
import system.Task;
import system.Return;

/**
 *
 * @author Peter Cappello
 * @param <T> type of the solution to this recursive problem.
 */
abstract public class TaskRecursive<T> extends Task
{    
    /**
     * If this task does not decompose, return a ReturnValue object, 
     * otherwise return a ReturnDecomposition object.
     * @return Either a ReturnValue object of a ReturnDecomposition object.
     */
    @Override
    public Return call() { return isAtomic() ? solve() : divideAndConquer(); }
    
    /**
     *
     * @return true if and only if this task does not decompose.
     */
    abstract public boolean isAtomic();
    
    /**
     *
     * @return the ReturnValue object.
     */
    abstract public ReturnValue<T> solve();
    
    /**
     *
     * @return the ReturnDecomposition object.
     */
    abstract public ReturnDecomposition divideAndConquer();
}

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
package applications.mandelbrotset;

import api.ReturnValue;
import api.TaskCompose;
import static applications.mandelbrotset.TaskMandelbrotSet.BLOCK_SIZE;
import static applications.mandelbrotset.TaskMandelbrotSet.N_PIXELS;

/**
 *
 * @author Peter Cappello
 */
public class AddBlocks extends TaskCompose<IterationCounts>
{    
    @Override
    public ReturnValue call() 
    {
        Integer[][] counts = new Integer[N_PIXELS][N_PIXELS];
        for ( IterationCounts iterationCounts : args() ) 
        {            
            // copy blockCounts into counts array
            Integer[][] blockCounts = iterationCounts.counts();
            int blockRow = iterationCounts.row();
            int blockCol = iterationCounts.col();
            for ( int row = 0; row < BLOCK_SIZE; row++ )
            {
                System.arraycopy( blockCounts[ row ], 0, counts[ blockRow * BLOCK_SIZE + row ], blockCol * BLOCK_SIZE, BLOCK_SIZE );
            }
        }
        return new ReturnValueIterationCounts( this, new IterationCounts( counts, 0, 0 ) );
    }
}

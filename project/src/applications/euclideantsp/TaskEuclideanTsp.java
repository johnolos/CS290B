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
 * FITNESS FOR ONE PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package applications.euclideantsp;

import api.JobRunner;
import api.ReturnDecomposition;
import api.ReturnValue;
import api.Shared;
import system.Task;
import api.TaskRecursive;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import util.EuclideanGraph;
import static util.EuclideanGraph.tourDistance;

/**
 * Find a tour of minimum cost among those that start with city 0, 
 followed by city secondCity.
 * @author Peter Cappello
 */
public class TaskEuclideanTsp extends TaskRecursive<Tour>
{ 
    // Configure Job
    static final public double[][] CITIES =
    {
	{ 1, 1 },
	{ 8, 1 },
	{ 8, 8 },
	{ 1, 8 },
	{ 2, 2 },
	{ 7, 2 },
	{ 7, 7 },
	{ 2, 7 },
	{ 3, 3 },
	{ 6, 3 },
	{ 6, 6 },
	{ 3, 6 },
	{ 4, 4 },
	{ 5, 4 },
	{ 5, 5 },
	{ 4, 5 }
    };
    static final private String FRAME_TITLE = "Euclidean TSP";
    static final private Task TASK = new TaskEuclideanTsp();
    static final private List<Integer> GREEDY_TOUR = EuclideanGraph.greedyTour( CITIES ) ;
    static private final double UPPER_BOUND = tourDistance( CITIES, GREEDY_TOUR );
    static private final Shared SHARED = new SharedTour( GREEDY_TOUR, UPPER_BOUND );
    
    public static void main( final String[] args ) throws Exception
    {
        new JobRunner( FRAME_TITLE, args ).run( TASK, SHARED );
    }
    
    static final Integer ONE = 1;
    static final Integer TWO = 2;
    static final Integer MAX_UNVISITED_CITIES = 12;
    
    private List<Integer> partialTour;
    private List<Integer> unvisitedCities;
    private LowerBound lowerBound;
    private boolean partialTourContains1;
    private boolean pruneMe;
            
    public TaskEuclideanTsp()
    {
        partialTour = new ArrayList<>();
        partialTour.add( 0 );
        this.unvisitedCities = new ArrayList<>();
        for ( int city = 1; city < CITIES.length; city++ )
        {
            unvisitedCities.add( city );
        }
        lowerBound = new LowerBoundNearestNeighbors();
//        lowerBound = new LowerBoundPartialTour( partialTour );
    }
    
    TaskEuclideanTsp( TaskEuclideanTsp parentTask, Integer newCity )
    {
        if ( ! parentTask.partialTourContains1 && newCity.equals( TWO ) )
        {
            pruneMe = true;
            return;
        }
        partialTour = new ArrayList<>( parentTask.partialTour );
        lowerBound = parentTask.lowerBound.make( parentTask, newCity );
        unvisitedCities = new LinkedList<>( parentTask.unvisitedCities ); 
        partialTour.add( newCity );
        unvisitedCities.remove( newCity );
        if ( newCity.equals( ONE ) )
        {
            partialTourContains1 = true;
        }
    }
    
    @Override
    public boolean isAtomic() {
        return unvisitedCities.size() <= MAX_UNVISITED_CITIES;
    }
    
    /**
     * Produce a tour of minimum cost from the set of tours, having as its
     * elements each tour consisting of the sequence of cities in partial tour 
     * followed by a permutation of the unvisited cities.
     * @return a tour of minimum cost.
     */
     @Override
    public ReturnValue solve() 
    {
        final Stack<TaskEuclideanTsp> stack = new Stack<>();
        stack.push( this );
        SharedTour sharedTour = ( SharedTour ) shared();
        List<Integer> shortestTour = sharedTour.tour();
        double shortestTourCost = sharedTour.cost();
        while ( ! stack.isEmpty() ) 
        {
            TaskEuclideanTsp currentTask = stack.pop();
            
            // get children with lower bound < current upper bound.
            List<TaskEuclideanTsp> children = currentTask.children( shortestTourCost );
            for ( TaskEuclideanTsp child : children )
            { 
                if ( child.isComplete() )
                { 
                    shortestTour = child.tour();
                    shortestTourCost = child.lowerBound().cost();
                } 
                else 
                { 
                    stack.push( child );
                } 
            }  
        } 
        shared( new SharedTour( shortestTour, shortestTourCost ) );
        return new ReturnValueTour( this, new Tour( shortestTour, shortestTourCost ) );
    }

    @Override
    public ReturnDecomposition divideAndConquer() 
    {
        final List<Task> children = new  LinkedList<>();
        for ( Integer city : unvisitedCities )
        {
            TaskEuclideanTsp child = new TaskEuclideanTsp( this, city );
            if ( ! child.pruneMe )
            {
                children.add( child );
            }
            else
            {
                // update prune statistics
            }
        }
        return new ReturnDecomposition( new MinTour(), children );
    }
    
    public LowerBound lowerBound() { return lowerBound; }
    
    /**
     * Get children whose lower bound is less than the current upper bound.
     * @param upperBound
     * @return 
     */
    private List<TaskEuclideanTsp> children( double upperBound )
    {
        List<TaskEuclideanTsp> children = new LinkedList<>();
        for ( Integer city : unvisitedCities )
        {
            TaskEuclideanTsp child = new TaskEuclideanTsp( this, city );
            if ( ! child.pruneMe && child.lowerBound().cost() < upperBound )
            {
                    children.add( child );
            }
            else
            {
                // update prune statistics
            }
        }
        return children;
    }
    
    public double cost() { return lowerBound().cost(); }
    
    public List<Integer> tour() { return partialTour; }
    
    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append( getClass() );
        stringBuilder.append( " Partial tour: \n" );
        partialTour.stream().forEach(( city ) -> 
        {
            stringBuilder.append( city ).append( ": " );
            stringBuilder.append( CITIES[ city ][ 0 ] ).append( " " ).append( CITIES[ city ][ 1 ] ).append( '\n' );
        } );
        stringBuilder.append( "\n\tUnvisited cities: " );
        unvisitedCities.stream().forEach(( city ) -> 
        {
            stringBuilder.append( city ).append( " " );
        } );
        return stringBuilder.toString();
    }
    
    public List<Integer> unvisitedCities() { return unvisitedCities; }
   
   private boolean isComplete() { return unvisitedCities.isEmpty(); }
}

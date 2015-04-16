package jobs;

import api.Job;
import api.Result;
import util.PermutationEnumerator;
import api.Task;
import tasks.EuclideanTSPTask;
import java.util.ArrayList;
import java.util.List;

/**
 * This task computes a solution to a Euclidean TSP problem instance.
 * @author Peter Cappello
 */
public class EuclideanTSPJob extends Job<Double,List<Integer>>
{
    final static Integer ONE = 1;
    final static Integer TWO = 2;

    final private double[][] cities;
    static private double[][] distances;

    private static final String jobId = "EuclideanTSPJob";
    private List<Integer> partialCityList;
    private List<Integer> shortestTour;
    private double shortestTourDistance;

    public EuclideanTSPJob( double[][] cities )
    {
        this.cities = cities;
        // Initialize comparison references
        initializeDistances();
        partialCityList = initialTour();
        shortestTour = new ArrayList<Integer>(partialCityList);
        shortestTour.add(0, 0);
        shortestTourDistance = tourDistance(shortestTour);
    }


    public List<Integer> calculateSolution() {
        return shortestTour;
    }

    @Override
    public void addResult(Result<Double> result) {
        super.addResult(result);
        if(result.getTaskReturnValue() < shortestTourDistance) {
            shortestTourDistance = result.getTaskReturnValue();
            EuclideanTSPTask t = (EuclideanTSPTask)getTask(result.getId());
            shortestTour = t.getPermutation();
        }
    }

    public void createTasks() {
        // Use my permutation enumerator
        PermutationEnumerator<Integer> permutationEnumerator = new PermutationEnumerator<Integer>( partialCityList );
        int id = 0;
        for ( List<Integer> subtour = permutationEnumerator.next(); subtour != null; subtour = permutationEnumerator.next() )
        {
            List<Integer> tour = new ArrayList<Integer>( subtour );
            tour.add( 0, 0 );
            if ( tour.indexOf( ONE ) >  tour.indexOf( TWO ) )
            {
                continue; // skip tour; it is the reverse of another.
            }
            EuclideanTSPTask t = new EuclideanTSPTask(jobId, id++, cities, tour);
            addTask(t);
        }
    }

    private List<Integer> initialTour()
    {
        List<Integer> tour = new ArrayList<Integer>();
        for ( int city = 1; city < cities.length; city++ )
        {
            tour.add( city );
        }
        return tour;
    }

    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append( getClass() );
        stringBuilder.append( "\n\tCities:\n\t" );
        for ( int city = 0; city < cities.length; city++ )
        {
            stringBuilder.append( city ).append( ": ");
            stringBuilder.append( cities[ city ][ 0 ] ).append(' ');
            stringBuilder.append( cities[ city ][ 1 ] ).append("\n\t");
        }
        return stringBuilder.toString();
    }

    private double tourDistance( final List<Integer> tour  )
    {
        double cost = distances[ tour.get( tour.size() - 1 ) ][ tour.get( 0 ) ];
        for ( int city = 0; city < tour.size() - 1; city ++ )
        {
            cost += distances[ tour.get( city ) ][ tour.get( city + 1 ) ];
        }
        return cost;
    }

    private static double distance( final double[] city1, final double[] city2 )
    {
        final double deltaX = city1[ 0 ] - city2[ 0 ];
        final double deltaY = city1[ 1 ] - city2[ 1 ];
        return Math.sqrt( deltaX * deltaX + deltaY * deltaY );
    }

    private void initializeDistances()
    {
        distances = new double[ cities.length][ cities.length];
        for ( int i = 0; i < cities.length; i++ )
            for ( int j = 0; j < i; j++ )
            {
                distances[ i ][ j ] = distances[ j ][ i ] = distance( cities[ i ], cities[ j ] );
            }
    }
}
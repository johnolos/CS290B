package jobs;

import api.Job;
import api.NotEnoughResultsException;
import api.Result;
import tasks.EuclideanTSPTask;
import java.util.ArrayList;
import java.util.List;

/**
 * This task computes a solution to a Euclidean TSP problem instance.
 */
public class EuclideanTSPJob extends Job<List<Integer>,List<Integer>> {

    /**
     * Cities in TSP problem
     */
    final private double[][] cities;

    /**
     * Distance table for the cities
     */
    static private double[][] distances;

    /**
     * JobId
     */
    private static final String jobId = "EuclideanTSPJob";

    /**
     * ShortestTour in TSP
     */
    private List<Integer> shortestTour;

    /**
     * Distance in shortest tour.
     */
    private double shortestTourDistance;

    public EuclideanTSPJob( double[][] cities )
    {
        this.cities = cities;
        shortestTour = null;
        shortestTourDistance = 0.0;

        // Initialize comparison references
        initializeDistances();
    }


    public List<Integer> calculateSolution() throws NotEnoughResultsException {
        if(getResults().size() < getTasks().size()) throw new NotEnoughResultsException("Not enough results");
        return shortestTour;
    }

    @Override
    public void addResult(Result<List<Integer>> result) {
        super.addResult(result);
        if(shortestTour == null) {
            shortestTour = result.getTaskReturnValue();
            shortestTourDistance = tourDistance(shortestTour);
        } else {
            List<Integer> tour = result.getTaskReturnValue();
            double tourDistance = tourDistance(tour);
            if(tourDistance < shortestTourDistance) {
                shortestTour = tour;
                shortestTourDistance = tourDistance;
            }
        }
    }

    public void createTasks()
    {
        int id = 0;
        for(int city = 1; city < cities.length; city++) {
            List<Integer> prefix = new ArrayList<Integer>();
            prefix.add(0);
            prefix.add(city);
            List<Integer> partialCityList = new ArrayList<Integer>();
            for(int i = 1; i < cities.length; i++) {
                if(i != city) {
                    partialCityList.add(i);
                }
            }
            EuclideanTSPTask t = new EuclideanTSPTask(jobId, id++, cities, prefix, partialCityList);
            addTask(t);
        }
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
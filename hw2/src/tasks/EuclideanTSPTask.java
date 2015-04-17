package tasks;

import api.Task;
import util.PermutationEnumerator;

import java.util.ArrayList;
import java.util.List;

public class EuclideanTSPTask extends Task<List<Integer>> {

    /**
     * List of prefix cities for this branch of TSP.
     */
    private final List<Integer> prefix;

    /**
     * List of cities to be permuted over.
     */
    private final List<Integer> partialCityList;

    /**
     * List of cities and their locations.
     */
    final private double[][] cities;

    /**
     * Distance table between cities to ease computation.
     */
    private double[][] distances;

    final static Integer ONE = 1;
    final static Integer TWO = 2;

    /**
     * Class for EuclideanTSPTask
     * @param jobId String id for job.
     * @param id Id of task.
     * @param cities Cities to be considered and their locations.
     * @param prefix Prefix for this branch.
     * @param partialCityList Cities to be permuted over.
     */
    public EuclideanTSPTask(String jobId, int id, double[][] cities, List<Integer> prefix, List<Integer> partialCityList) {
        super(jobId, id);
        assert prefix != null;
        assert partialCityList != null;
        assert cities.length == (prefix.size() + partialCityList.size());
        assert id > 0;
        this.cities = cities;
        this.prefix = prefix;
        this.partialCityList = partialCityList;
        initializeDistances();
    }

    /**
     * Call method to execute TSP task.
     * Calculates all premutations given by this TSP branch.
     * @return Return list containing best TSP tour for this branch.
     */
    public List<Integer> call() {
        List<Integer> shortestTour = new ArrayList<Integer>(partialCityList);
        shortestTour.addAll(0, prefix);
        double shortestTourDistance = tourDistance(shortestTour);


        // Using Permutation Enumerator
        PermutationEnumerator<Integer> permutationEnumerator = new PermutationEnumerator<Integer>( partialCityList );
        for ( List<Integer> subtour = permutationEnumerator.next(); subtour != null; subtour = permutationEnumerator.next() )
        {
            List<Integer> tour = new ArrayList<Integer>( subtour );
            tour.addAll(0, prefix);
            if ( tour.indexOf( ONE ) >  tour.indexOf( TWO ) )
            {
                continue; // skip tour; it is the reverse of another.
            }
            double tourDistance = tourDistance(tour);
            if(tourDistance < shortestTourDistance) {
                shortestTour = tour;
                shortestTourDistance = tourDistance;
            }
        }
        return shortestTour;
    }


    /**
     * Distance of a TSP tour.
     * @param tour TSP tour as a list of cities.
     * @return double distance of tour
     */
    private double tourDistance( final List<Integer> tour  )
    {
        double cost = distances[ tour.get( tour.size() - 1 ) ][ tour.get( 0 ) ];
        for ( int city = 0; city < tour.size() - 1; city ++ )
        {
            cost += distances[ tour.get( city ) ][ tour.get( city + 1 ) ];
        }
        return cost;
    }

    /**
     * Euclidean distance algorithm to compute distance between two cities.
     * @param city1 City one
     * @param city2 City two
     * @return double distance
     */
    private static double distance( final double[] city1, final double[] city2 )
    {
        final double deltaX = city1[ 0 ] - city2[ 0 ];
        final double deltaY = city1[ 1 ] - city2[ 1 ];
        return Math.sqrt( deltaX * deltaX + deltaY * deltaY );
    }

    /**
     * Initialise distance table to ease computation of TSP.
     */
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

package tasks;

import api.Task;
import util.PermutationEnumerator;

import java.util.ArrayList;
import java.util.List;

public class EuclideanTSPTask extends Task<List<Integer>> {

    private final List<Integer> prefix;
    private final List<Integer> partialCityList;
    final private double[][] cities;
    private double[][] distances;

    final static Integer ONE = 1;
    final static Integer TWO = 2;

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

    public List<Integer> call() {
        List<Integer> shortestTour = new ArrayList<Integer>(partialCityList);
        shortestTour.addAll(0, prefix);
        double shortestTourDistance = tourDistance(shortestTour);

        System.out.println("What we start with:");
        for(Integer i : shortestTour) {
            System.out.print(i + ", ");
        }
        System.out.println();

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
        System.out.println("What we end with:");
        for(Integer i : shortestTour) {
            System.out.print(i + ", ");
        }
        System.out.println();
        return shortestTour;
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

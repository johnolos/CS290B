package tasks;

import api.Task;
import java.util.List;

public class EuclideanTSPTask extends Task<Double> {

    private final List<Integer> permutation;
    final private double[][] cities;
    static private double[][] distances;

    public EuclideanTSPTask(String jobId, int id, double[][] cities, List<Integer> permutation) {
        super(jobId, id);
        this.cities = cities;
        this.permutation = permutation;
        initializeDistances();
    }

    public List<Integer> getPermutation() {
        return permutation;
    }


    public Double call() {
        return tourDistance( permutation );
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

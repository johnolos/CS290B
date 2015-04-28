package jobs;

import api.Job;
import api.Task;
import results.TSPResult;
import tasks.EuclideanTSPTask;

import java.util.ArrayList;
import java.util.List;

/**
 * This task computes a solution to a Euclidean TSP problem instance.
 */
public class EuclideanTSPJob implements Job<TSPResult> {

    /**
     * Cities in TSP problem
     */
    final private double[][] cities;

    private TSPResult result;

    public EuclideanTSPJob( double[][] cities )
    {
        this.cities = cities;
        result = null;
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

    @Override
    public Task runJob() {
        List<Integer> prefix = new ArrayList<Integer>();
        prefix.add(0);
        List<Integer> partialCityList = new ArrayList<Integer>();
        for(int i = 1; i < cities.length; i++) {
            partialCityList.add(i);
        }

        EuclideanTSPTask t = new EuclideanTSPTask(null, cities, prefix, partialCityList);
        return t;
    }

    @Override
    public TSPResult value() {
        return result;
    }

    @Override
    public void setValue(TSPResult value) {
        this.result = value;
    }
}
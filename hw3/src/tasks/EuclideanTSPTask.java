package tasks;

import api.Task;
import results.TSPResult;
import system.Computer;
import util.PermutationEnumerator;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class EuclideanTSPTask extends Task<List<Integer>> {

    /**
     * List of prefix cities for this branch of TSP.
     */
    private final List<Integer> pretour;

    private List<Integer> tour;

    private double tourDistance;

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

    private final int LIMIT = 3;

    final static Integer ONE = 1;
    final static Integer TWO = 2;

    /**
     * Class for EuclideanTSPTask
     * @param parentId String id for job.
     * @param cities Cities to be considered and their locations.
     * @param pretour Prefix for this branch.
     * @param partialCityList Cities to be permuted over.
     */
    public EuclideanTSPTask(UUID parentId, double[][] cities, List<Integer> pretour, List<Integer> partialCityList) {
        super(parentId);
        assert pretour != null;
        assert cities != null;
        assert partialCityList != null;
        assert cities.length == (pretour.size() + partialCityList.size());
        this.cities = cities;
        this.pretour = pretour;
        this.partialCityList = partialCityList;
        if(pretour.size() > LIMIT) {
            initializeDistances();
        }
    }

    /*
    public List<Integer> call() {
        List<Integer> shortestTour = new ArrayList<Integer>(partialCityList);
        shortestTour.addAll(0, pretour);
        double shortestTourDistance = tourDistance(shortestTour);


        // Using Permutation Enumerator
        PermutationEnumerator<Integer> permutationEnumerator = new PermutationEnumerator<Integer>( partialCityList );
        for ( List<Integer> subtour = permutationEnumerator.next(); subtour != null; subtour = permutationEnumerator.next() )
        {
            List<Integer> tour = new ArrayList<Integer>( subtour );
            tour.addAll(0, pretour);
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
    }*/

    @Override
    public void execute(Computer computer) throws RemoteException {
        if(pretour.size() > LIMIT) {
            computePermutations();
            TSPResult result = new TSPResult(tour, tourDistance);
            computer.setArg(getParentId(), result);
        } else {
            TSPSum tspSum = new TSPSum(getParentId(), partialCityList.size());
            computer.compute(tspSum);
            listToString(pretour);
            for(int city = 0; city < cities.length; city++) {
                if(!pretour.contains(city)) {
                    List<Integer> prefix = new ArrayList<Integer>();
                    prefix.addAll(pretour);
                    prefix.add(city);
                    List<Integer> newPartialCityList = new ArrayList<Integer>();
                    for(int i = 1; i < cities.length; i++) {
                        if(i != city && !pretour.contains(i)) {
                            newPartialCityList.add(i);
                        }
                    }
                    EuclideanTSPTask t = new EuclideanTSPTask(tspSum.getTaskId(), cities, prefix, newPartialCityList);
                    computer.compute(t);
                }
            }
        }
    }


    public void computePermutations() {
        List<Integer> shortestTour = new ArrayList<Integer>(partialCityList);
        shortestTour.addAll(0, pretour);
        double shortestTourDistance = tourDistance(shortestTour);

        // Using Permutation Enumerator
        PermutationEnumerator<Integer> permutationEnumerator = new PermutationEnumerator<Integer>( partialCityList );
        for ( List<Integer> subtour = permutationEnumerator.next(); subtour != null; subtour = permutationEnumerator.next() )
        {
            List<Integer> tour = new ArrayList<Integer>( subtour );
            tour.addAll(0, pretour);
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
        tour = shortestTour;
        tourDistance = shortestTourDistance;
    }


    public <S> void listToString(List<S> list) {
        for(S ele : list) {
            System.out.print(ele + " , ");
        }
    }

    @Override
    public void addResult(List<Integer> result) {
    }

    @Override
    public boolean isReadyToExecute() {
        return true;
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

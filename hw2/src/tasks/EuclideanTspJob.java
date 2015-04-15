package tasks;

import system.Tuple;
import api.Task;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EuclideanTspJob implements Task<List<Integer>>, Serializable {

    /** Cities and their location. First value is index of city and second array is x and y coordinate **/
    private double[][] cities;
    /** All permutations of city to city **/
    List<List<Tuple>> listOftuples;
    /** List of all permutations **/
    public static List<String> permutationsList;

    /**
     * EuclideanTspTask
     * A task that brute force and calculates euclidean distance for all possible roundabouts given cities presented.
     * The result is the optimal route.
     * @param cities Double[][] cities.
     */
    public EuclideanTspJob(double[][] cities) {
        this.cities = cities;

        listOftuples = new ArrayList<List<Tuple>>();
        for(int i = 0; i < cities.length; i++) {
            ArrayList<Tuple> innerList = new ArrayList<Tuple>();
            for(int j = 0; j < cities.length; j++) {
                if(i == j) {
                    continue;
                } else {
                    Tuple tup = new Tuple(i, j);
                    innerList.add(tup);
                }
            }
            listOftuples.add(innerList);
        }
    }

    /**
     * Calculates the Euclidiean distance given indexes of two cities within a tuple.
     * @param tup Tuple containing two city indexes.
     * @return Double - Euclidean distance between the two cities.
     */
    public double euclideanDistance(Tuple tup) {
        double x1, y1,x2,y2;
        x1 = cities[tup.x][0];
        y1 = cities[tup.x][1];
        x2 = cities[tup.y][0];
        y2 = cities[tup.y][1];
    	return Math.sqrt(Math.pow( (x1-x2),2)+ Math.pow( (y1-y2),2));
    }

    /**
     * Help function to build permutation string.
     * @param numberOfCities
     * @return String
     */
    public String buildStartString(int numberOfCities) {
    	String startString = ""; 
    	for (int i = 1; i < numberOfCities; i ++) { // start i = 1, fordi vi alltid skal starte på 0. 
    		startString += Integer.toString(i); 
    	}
    	return startString; 
    }


    public static void permutation(String str) { 
        permutation("", str); 
    }

    private static void permutation(String prefix, String str) {
        int n = str.length();
        if (n == 0) {
        	String perm = Integer.toString(0) + prefix ;  // legger til 0 på starten. 
        	permutationsList.add(perm); //returnerer 0 + prefix av 1...lengdeAvCities 
        } //System.out.println(prefix);
        else {
            for (int i = 0; i < n; i++)
                permutation(prefix + str.charAt(i), str.substring(0, i) + str.substring(i+1, n));
        }
    }

    /**
     * Execute function inherited from Task interface.
     * Computes and solves euclidean distance for all possible permutations of how to travel from one city to another
     * given the cities. It finds the quickest route and return that as a list of integers.
     * @return List<Integer> Fastest route.
     */
    public List<Integer> execute() {
    	permutationsList = new ArrayList<String>(); 
    	String startString = buildStartString(cities.length-1);
    	permutation(startString); 
    	
    	// Make tuple list
    	List<List<Tuple>> listOfTuples = new ArrayList<List<Tuple>>(); 
    	for (String permutation : permutationsList) {
    		List<Tuple> tuples = new ArrayList<Tuple>(); 
    		for (int i = 0; i < permutation.length() - 1 ; i++) {
    			tuples.add(new Tuple(Character.getNumericValue(permutation.charAt(i)), Character.getNumericValue(permutation.charAt(i+1)))); 	
    		}
    		listOfTuples.add(tuples); 
    	}

    	double shortestPath = 999999999; 
    	int numOfTupleCombination = -1; 

        // Find the permutation with optimal route and save it.
        for(int i = 0; i < listOfTuples.size(); i++) {
            List<Tuple> tuples = listOfTuples.get(i);
            double result = 0;
            for(Tuple tup : tuples) {
                result += euclideanDistance(tup);
            }
            if(result < shortestPath) {
                shortestPath = result;
                numOfTupleCombination = i;
            }
        }
    	
        List<Tuple> resultTuples = listOfTuples.get(numOfTupleCombination);
        List<Integer> resultList = new ArrayList<Integer>();

        resultList.add(resultTuples.get(0).x);
        resultList.add(resultTuples.get(0).y);

        // Compute the optimal route given the permutation data.
        while(resultList.size() < cities.length) {
            for(Tuple tup : resultTuples) {
                if(tup.x == resultList.get(resultList.size() - 1)) {
                    resultList.add(tup.y);
                }
            }
        }

        return resultList;
    }

    @Override
    public List<Integer> call() {
        return null;
    }
}

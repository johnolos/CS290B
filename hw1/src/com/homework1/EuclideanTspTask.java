package com.homework1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EuclideanTspTask implements Task<List<Integer>>, Serializable {

    private double[][] cities;
    List<List<Tuple>> listOftuples;

    public EuclideanTspTask(double[][] cities) {
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

    public double euclideanDistance(Tuple tup) {
        double x1, y1,x2,y2;
        x1 = cities[tup.x][0];
        y1 = cities[tup.x][1];
        x2 = cities[tup.y][0];
        y2 = cities[tup.y][1];
    	return Math.sqrt(Math.pow( (x1-x2),2)+ Math.pow( (y1-y2),2));
    }
    
    public Tuple recursive(int fromCity, int toCity)  {
    	return new Tuple(fromCity, toCity);
    }
   
    
    public static List<String> permutationsList; 
    
    public String buildStartString(int numberOfCities) {
    	String startString = ""; 
    	for (int i = 0; i < numberOfCities; i ++) {
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
        	permutationsList.add(prefix);
        } //System.out.println(prefix);
        else {
            for (int i = 0; i < n; i++)
                permutation(prefix + str.charAt(i), str.substring(0, i) + str.substring(i+1, n));
        }
    }

    
    public List<Integer> execute() {
    	permutationsList = new ArrayList<String>(); 
    	String startString = buildStartString(cities.length);
    	permutation(startString); 
    	
    	// make tuplelist
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

        /*for(Tuple tup: resultTuples) {
            System.out.println(tup);
        }*/

        resultList.add(resultTuples.get(0).x);
        resultList.add(resultTuples.get(0).y);

        //System.out.println("Entering while");
        while(resultList.size() < cities.length) {
            for(Tuple tup : resultTuples) {
                if(tup.x == resultList.get(resultList.size() - 1)) {
                    resultList.add(tup.y);
                }
            }
        }

        return resultList;
    	
    	}



}

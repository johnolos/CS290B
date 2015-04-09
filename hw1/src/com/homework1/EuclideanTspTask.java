package com.homework1;

import java.util.ArrayList;
import java.util.List;

public class EuclideanTspTask implements Task<List<Integer>> {

    private double[][] cities;

    public EuclideanTspTask(double[][] cities) {
        this.cities = cities;

    }

    public double euclideanDistance(double x1, double y1, double cities2, double cities3) {
    	return Math.sqrt(Math.pow( (x1-cities2),2)+ Math.pow( (y1-cities3),2)  ); 
    }
    
    @Override
    public List<Integer> execute() {
        List<Integer> orderedList = new ArrayList<Integer>();
        double leastDistance;
        int numOfCity;
        double atLeastDistance = 100000.0; 
        
        for(int i = 0; i < cities.length; i++) {
            for(int j = 0; j < cities.length; j++) {
                if(i == j)
                    continue;
                else {
                	atLeastDistance = euclideanDistance(cities[i][0],cities[i][1], cities[j][0], cities[j][1]); 
                	

                }
            }
        }



        return new ArrayList<Integer>();
    }
}

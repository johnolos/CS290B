package com.homework1;

import java.util.ArrayList;
import java.util.List;

public class EuclideanTspTask implements Task<List<Integer>> {

    private double[][] cities;

    public EuclideanTspTask(double[][] cities) {
        this.cities = cities;

    }

    @Override
    public List<Integer> execute() {
        List<Integer> orderedList = new ArrayList<Integer>();
        double leastDistance;
        int numOfCity;
        for(int i = 0; i < cities.length; i++) {
            for(int j = 0; j < cities.length; j++) {
                if(i == j)
                    continue;
                else {
                    

                }
            }
        }



        return new ArrayList<Integer>();
    }
}

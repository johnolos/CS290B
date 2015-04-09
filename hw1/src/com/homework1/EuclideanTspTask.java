package com.homework1;

import java.util.ArrayList;
import java.util.List;

public class EuclideanTspTask implements Task<List<Integer>> {

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
    
    @Override
    public List<Integer> execute() {
        List<Integer> orderedList = new ArrayList<Integer>();
        int numOfCity = -1;

        List<Double> resultList = new ArrayList<Double>();

        for(int i = 0; i < listOftuples.size(); i++) {
            List<Tuple> innerList = listOftuples.get(i);
            double result = 0.0;
            for(Tuple tup : innerList) {
                result += euclideanDistance(tup);
            }
            resultList.add(result);
        }

        double compareResult = 1000000000.0;

        for(int i = 0; i < resultList.size(); i++) {
            double tempResult = Double.valueOf(resultList.get(i));
            if(tempResult < compareResult) {
                compareResult = tempResult;
                numOfCity = i;
            }
        }

        List<Tuple> tuples = listOftuples.get(numOfCity);
        List<Integer> intList = new ArrayList<Integer>();
        intList.add(Integer.valueOf(tuples.get(0).x));
        while(intList.size() < tuples.size()) {
            Integer integer = intList.get(-1);
            for(Tuple tup : tuples) {
                if(integer == tup.x) {
                    intList.add(tup.y);
                    break;
                }
            }
        }

        return intList;
    }
}

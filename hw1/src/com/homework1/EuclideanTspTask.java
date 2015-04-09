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
    
    public List<Integer> execute() {

        // Generating all possible tuples of cities
        List<List<Tuple>> listOfTuples = new ArrayList<List<Tuple>>();
        for(int i = 0; i < cities.length; i++) {
            List<Tuple> tuples = new ArrayList<Tuple>();
            for(int j = 0; j < cities.length; j++) {
                if(i==j)
                    continue;
                tuples.add(new Tuple(i, j));
                listOfTuples.add(recursive(tuples,null));
            }
        }

        for(int i = 0; i < cities.length; i++) {



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

        for(Tuple tup: resultTuples) {
            System.out.println(tup);
        }

        resultList.add(resultTuples.get(0).x);
        resultList.add(resultTuples.get(0).y);

        System.out.println("Entering while");
        while(resultList.size() < cities.length) {
            for(Tuple tup : resultTuples) {
                if(tup.x == resultList.get(resultList.size() - 1)) {
                    resultList.add(tup.y);
                }
            }
        }

        return resultList;


        /**
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
                System.out.println(numOfCity);
            }
        }

        List<Tuple> tuples = listOftuples.get(numOfCity);
        List<Integer> intList = new ArrayList<Integer>();
        intList.add(Integer.valueOf(tuples.get(0).x));
        while(intList.size() < cities.length) {
            Integer integer = intList.get(intList.size()-1);
            for(Tuple tup : tuples) {
                if(integer == tup.x) {
                    intList.add(tup.y);
                    System.out.println("foreeever?");
               
                }
            }
        }
        System.out.println(intList.toString());
        System.out.println("halloo!");
        return intList;
         **/
    }


    private List<Tuple> recursive(List<Tuple> prev, List<Integer> avail) {
        List<Integer> copy = null;
    }


}

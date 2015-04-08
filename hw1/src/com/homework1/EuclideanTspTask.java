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
        return new ArrayList<Integer>();
    }
}

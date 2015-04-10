package com.homework1.tasks;

import a.api.Task;
import java.io.Serializable;

public final class MandelbrotSetTask implements Task<Integer[][]>, Serializable {

    /** Doubles representing lower left corner in complex plane **/
    private double x, y;
    /** A double representing edge length of a square in the complex plane **/
    private double length;
    /** Int which specifies how many squares there are which are visualized by 1 pixel. n*n squares **/
    private int n;
    /** Iteration limit. **/
    private int limit;

    /**
     * Constructor for MandelbrotSetTask
     * @param x X-coordinate in lower left corner
     * @param y Y-coordinate in lower left corner
     * @param length Edge length of a square in complex plane
     * @param n Defines how many squares are genereated. n x n
     * @param limit Iteration limit
     */
    public MandelbrotSetTask(double x, double y, double length, int n, int limit) {
        this.x = x;
        this.y = y;
        this.length = length;
        this.n = n;
        this.limit = limit;
    }

    /**
     * execute
     * Computes the data for a MandelbrotSet
     * @return Integer[][] Data result
     */
    public Integer[][] execute() {

        Integer[][] result = new Integer[n][n];
        double x, y;

        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                x = ((i * length) / n) + this.x;
                y = ((j * length) / n) + this.y;
                result[i][j] = computation(x, y);
            }
        }
        return result;
    }

    /**
     * Delegated function to compute the iteration computation for the MandelbrotSet.
     * @param x0 Double x-value
     * @param y0 Double y-value
     * @return
     */
    private int computation(double x0, double y0) {
        double x = 0.0, y = 0.0;
        int itr = 0;
        while((x*x + y*y) < 2*2 && itr < limit) {
            double x_temp = x*x - y*y + x0;
            y = 2*x*y + y0;
            x = x_temp;
            itr++;
        }
        return itr;
    }
}

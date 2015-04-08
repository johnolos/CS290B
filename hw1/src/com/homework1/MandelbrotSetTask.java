package com.homework1;


import java.io.Serializable;

public final class MandelbrotSetTask implements Task<Integer[][]>, Serializable {

    private double x, y;
    private double length;
    private int n;
    private int limit;

    public MandelbrotSetTask(double x, double y, double length, int n, int limit) {
        this.x = x;
        this.y = y;
        this.length = length;
        this.n = n;
        this.limit = limit;
    }

    @Override
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

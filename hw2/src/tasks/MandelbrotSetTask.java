package tasks;

import api.Task;

/**
 * MandelbrotSetTask
 * A task class implementation to solve a pixel row part of the MandelbrotSet.
 */
public class MandelbrotSetTask extends Task<Integer[]> {
    private int row, n, limit;
    private double x, y;
    private double length;

    /**
     * Constructor for MandelbrotSetTask
     * @param jobId String jobId of which job this task is part of.
     * @param row Row to calculate the MandelbrotSet pixel.
     * @param x Coordinate in lower left corner.
     * @param y Coordinate in lower left corner.
     * @param n
     * @param limit Limit for number of iterations.
     * @param length
     */
    public MandelbrotSetTask(String jobId, int row, double x, double y, int n, int limit, double length) {
        super(jobId, row);
        this.row = row;
        this.n = n;
        this.x = x;
        this.y = y;
        this.limit = limit;
        this.length = length;
    }

    /**
     * Call function to run the computation for this task of the MandelbrotSet.
     * @return Integer[] pixel row with 0 or 1.
     */
    public Integer[] call() {
        double x, y;
        Integer[] pixels = new Integer[n];
        for(int col = 0; col < n; col++) {
            x = ((row * length) / n) + this.x;
            y = ((col * length) / n) + this.y;
            pixels[col] = computation(x, y);
        }
        return pixels;
    }

    /**
     * Delegated function to compute the iteration computation for the MandelbrotSet.
     * @param x0 Double x-value
     * @param y0 Double y-value
     * @return Int value as result of iteration computation
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

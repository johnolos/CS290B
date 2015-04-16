package jobs;

import api.Job;
import api.NotEnoughResultsException;
import api.Result;
import tasks.MandelbrotSetTask;
import java.io.Serializable;

public final class MandelbrotSetJob extends Job<Integer[],Integer[][]> implements Serializable {

    /** Doubles representing lower left corner in complex plane **/
    private double x, y;
    /** A double representing edge length of a square in the complex plane **/
    private double length;
    /** Int which specifies how many squares there are which are visualized by 1 pixel. n*n squares **/
    private int n;
    /** Iteration limit. **/
    private int limit;
    /** JobId of current job */
    private final String jobId;
    /** Set number of rows in each task */
    private final int NUM_OF_ROWS = 10;

    /**
     * Constructor for MandelbrotSetTask
     * @param x X-coordinate in lower left corner
     * @param y Y-coordinate in lower left corner
     * @param length Edge length of a square in complex plane
     * @param n Defines how many squares are genereated. n x n
     * @param limit Iteration limit
     */
    public MandelbrotSetJob(double x, double y, double length, int n, int limit) {
        this.x = x;
        this.y = y;
        this.length = length;
        this.n = n;
        this.limit = limit;
        jobId = "MandelbrotSet";
    }

    /**
     * Divides current Job into smaller task which is able to be computed in simultaneously.
     */
    public void createTasks() {
        for(int row = 0; row < n; row++) {
            MandelbrotSetTask t = new MandelbrotSetTask(jobId, row, x, y, n, limit, length);
            addTask(t);
        }
    }

    public Integer[][] calculateSolution() throws NotEnoughResultsException {
        if(getResults().size() < getTasks().size()) throw new NotEnoughResultsException("calculateSolution()");

        Integer[][] pixels = new Integer[n][n];
        for(Result<Integer[]> r : getResults()) {
            final int row = r.getId();
            final Integer[] pixelRow = r.getTaskReturnValue();
            for(int col = 0; col < pixelRow.length; col++) {
                pixels[row][col] = pixelRow[col];
            }
        }
        return pixels;
    }

}

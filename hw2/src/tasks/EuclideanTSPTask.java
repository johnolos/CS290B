package tasks;

import api.Task;

import java.util.ArrayList;
import java.util.List;

public class EuclideanTSPTask extends Task<List<Integer>> {

    public EuclideanTSPTask(String jobId, int id) {
        super(jobId, id);
    }

    public List<Integer> call() {
        return new ArrayList<Integer>();
    }
}

package tasks;

import api.Task;

import java.util.ArrayList;
import java.util.List;

public class EuclideanTSPTask extends Task<List<Integer>> {

    List<List<Integer>> permutations;

    public EuclideanTSPTask(String jobId, int id) {
        super(jobId, id);
        permutations = new ArrayList<List<Integer>>();
    }

    public void addPermutation(List<Integer> permutation) {
        permutations.add(permutation);
    }

    public List<Integer> call() {
        return new ArrayList<Integer>();
    }
}

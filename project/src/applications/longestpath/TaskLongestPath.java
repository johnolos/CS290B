package applications.longestpath;

import api.ReturnDecomposition;
import api.ReturnValue;
import api.TaskRecursive;
import system.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskLongestPath extends TaskRecursive<Path> {

    // Configure job


    final private Path path;

    public TaskLongestPath(Path path) {
        this.path = path;
    }

    @Override
    public boolean isAtomic() {
        return false;
    }

    @Override
    public ReturnValue<Path> solve() {
        return new ReturnValuePath(this, path);
    }

    @Override
    public ReturnDecomposition divideAndConquer() {



        // This is just a example of how it looks like and not an actually solution.

        List<Task> subtasks = new ArrayList<>();
        subtasks.add(new TaskLongestPath(path));
        subtasks.add(new TaskLongestPath(path));
        return new ReturnDecomposition(new LongestPath(), subtasks);
    }
}

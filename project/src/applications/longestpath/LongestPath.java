package applications.longestpath;

import api.ReturnValue;
import api.TaskCompose;

import java.util.List;

public class LongestPath extends TaskCompose<Path> {

    @Override
    public ReturnValue call() {

        // Sort our paths on distance and return the longest one.
        List<Path> paths = args();
        paths.sort((path1, path2) -> path1.compareTo(path2));
        return new ReturnValuePath(this, paths.get(0));

    }
}

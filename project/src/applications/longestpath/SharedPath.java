package applications.longestpath;

import api.Shared;

public class SharedPath extends Shared<SharedPath> {

    private final Path path;

    /**
     * SharedPath
     * @param path
     */
    public SharedPath(final Path path) {
        this.path = path;
    }

    @Override
    public boolean isOlderThan(SharedPath that) {
        return this.path.getCost() < that.path().getCost();
    }

    public Path path() {
        return path;
    }
}

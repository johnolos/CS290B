package results;

import java.io.Serializable;
import java.util.UUID;

public class SetArg<T> implements Serializable {
    private final UUID id;
    private final T arg;

    public SetArg(UUID id, T arg) {
        this.id = id;
        this.arg = arg;
    }

    public UUID getUUID() {
        return id;
    }

    public T getArg() {
        return arg;
    }

}

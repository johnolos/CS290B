package api.events;

import java.io.Serializable;

public interface EventView<T> extends Serializable {

    public void view(final T data);

}

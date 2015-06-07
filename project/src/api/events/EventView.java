package api.events;

public interface EventView<T> {

    void view(final T data);

    void viewIfCapable(Object data);

}

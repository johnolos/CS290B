package api;

public interface EventHandler {

    public void handle(Event event);

    public void register(JobRunner jobRunner);

    public void unregister(JobRunner jobRunner);

}

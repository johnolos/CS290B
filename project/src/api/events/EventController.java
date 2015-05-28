package api.events;


public interface EventController {

    public void handle(Event event);

    public void register(EventView jobRunner);

    public void unregister(EventView jobRunner);

}

package api.events;

import java.io.Serializable;

public class Event implements Serializable {

    private final EventType event;
    private final Object o;

    public Event(EventType event, Object o) {
        this.event = event;
        this.o = o;
    }

    public EventType getEvent() { return event; }

    public Object getObject() { return o; }

}

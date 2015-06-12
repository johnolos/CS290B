package api.events;

import java.io.Serializable;

public class Event implements Serializable {

    public static enum Type {
        SHARED_UPDATED,
        TEMPORARY_SOLUTION,
        STATUS
    }

    private final Event.Type event;
    private final Object o;

    public Event(Type event, Object o) {
        this.event = event;
        this.o = o;
    }

    public Event.Type getEventType() { return event; }

    public Object getObject() { return o; }

}

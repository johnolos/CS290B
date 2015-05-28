package api;

import java.io.Serializable;

public class Event implements Serializable {

    private final EventEnum event;
    private final Object o;

    public Event(EventEnum event, Object o) {
        this.event = event;
        this.o = o;
    }

    public EventEnum getEvent() { return event; }

    public Object getObject() { return o; }

}

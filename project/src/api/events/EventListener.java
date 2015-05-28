package api.events;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class EventListener implements Remote {

    List<EventController> eventHandlers = new ArrayList<>();

    public void fireEvent(Event event) throws RemoteException {
        for(EventController handler : eventHandlers) {
            handler.handle(event);
        }
    }

    public void register(EventController handler) {
        eventHandlers.add(handler);
    }

    public void unregister(EventController handler) {
        eventHandlers.remove(handler);
    }

}

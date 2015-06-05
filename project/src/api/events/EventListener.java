package api.events;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class EventListener implements Remote {

    List<EventController> eventControllers = new ArrayList<>();

    public void fireEvent(Event event) throws RemoteException {
        for(EventController handler : eventControllers) {
            handler.handle(event);
        }
    }

    public void register(EventController handler) {
        eventControllers.add(handler);
    }

    public void unregister(EventController handler) {
        eventControllers.remove(handler);
    }

}

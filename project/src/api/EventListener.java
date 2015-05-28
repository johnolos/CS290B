package api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class EventListener implements Remote {

    List<EventHandler> eventHandlers = new ArrayList<>();

    public void fireEvent(Event event) throws RemoteException {
        for(EventHandler handler : eventHandlers) {
            handler.handle(event);
        }
    }

    public void register(EventHandler handler) {
        eventHandlers.add(handler);
    }

    public void unregister(EventHandler handler) {
        eventHandlers.remove(handler);
    }

}

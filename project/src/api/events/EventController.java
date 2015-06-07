package api.events;


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public abstract class EventController extends UnicastRemoteObject implements EventListener {

    List<EventView> eventViews;

    public EventController() throws RemoteException{
        eventViews = new ArrayList<>();
    }

    abstract public void handle(Event event);

    public void fireViewUpdate(Object data) {
        for(EventView eventView : eventViews) {
            eventView.viewIfCapable(data);
        }
    }

    public void register(EventView eventView) {
        eventViews.add(eventView);
    }

    public void unregister(EventView eventView) {
        eventViews.remove(eventView);
    }

    @Override
    public void notify(Event event) throws RemoteException {
        handle(event);
    }

}

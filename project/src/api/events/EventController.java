package api.events;


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public abstract class EventController extends UnicastRemoteObject implements EventListener {

    List<EventView> eventViews;

    final private EventControllerUrl url;

    public EventController(final String domain, final int port, final String service)
            throws RemoteException {
        eventViews = new ArrayList<>();
        url = new EventControllerUrl(domain, port, service);
    }

    abstract public void handle(Event event);

    public void fireViewUpdate(Object data) {
        for(EventView eventView : eventViews) {
            eventView.viewIfCapable(data);
        }
    }

    @Override
    public void notify(Event event) throws RemoteException {
        handle(event);
    }

    public EventControllerUrl getUrl() { return url; }

    public void register(EventView eventView) {
        eventViews.add(eventView);
    }

    public void unregister(EventView eventView) {
        eventViews.remove(eventView);
    }


}

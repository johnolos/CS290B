package api.events;


import java.rmi.Remote;
import java.rmi.RemoteException;

public abstract class EventController implements Remote {

    public String DOMAIN;
    public String SERVICE;
    public int PORT;

    public EventController(String domain, String service, int port) {
        this.DOMAIN = domain;
        this.SERVICE = service;
        this.PORT = port;

    }

    abstract public void handle(Event event) throws RemoteException;

    abstract public void register(EventView jobRunner);

    abstract public void unregister(EventView jobRunner);

}

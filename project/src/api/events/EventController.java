package api.events;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface EventController extends Remote {

    public String url() throws RemoteException;

    public void handle(Event event) throws RemoteException;

    void register(EventView eventView) throws RemoteException;

    void unregister(EventView eventView) throws RemoteException;

}

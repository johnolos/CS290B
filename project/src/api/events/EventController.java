package api.events;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface EventController extends Remote {

    public void handle(Event event) throws RemoteException;

}

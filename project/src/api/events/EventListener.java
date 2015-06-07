package api.events;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface EventListener extends Remote {

    public void notify(Event event) throws RemoteException;

}

package api;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Computer extends Remote {
	public final String SERVICE_NAME = "Marianne";
	public final int PORT = 1099;
	public <T> T execute(Task<T> task) throws RemoteException; 

}

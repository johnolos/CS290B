package computer;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import api.Computer;
import api.Task;

public class ComputerImpl implements Computer, Serializable{

	
	@Override
	public <T> T execute(Task<T> task) {
		return task.execute();
	}
	
	public static void main(String[] args) throws Exception {
		
		System.setSecurityManager(new SecurityManager());
		
		Computer computer = new ComputerImpl(); 
		Computer stub = (Computer) UnicastRemoteObject.exportObject(computer,0);
		
		Registry registry = LocateRegistry.createRegistry(1099);
		
		registry.rebind(Computer.SERVICE_NAME, stub);
	
		System.out.println("ComputerImpl.main Ready.");
	}

}

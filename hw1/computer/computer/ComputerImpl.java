package com.homework1.computer;

import a.api.Computer;
import a.api.Task;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * ComputerImplementation of the interface given by Computer
 */
public class ComputerImpl implements Computer {


    /**
     * Implementation of execute
     * @param t Task to be executed
     * @param <T> Result object of the task executed.
     * @return T object as result specified by the task.
     * @throws java.rmi.RemoteException
     */
    public <T> T execute(Task<T> t) throws RemoteException {
        return t.execute();
    }

    /**
     * Main function to start register and the name service and bind a stub of this computer to SERVICE_NAME given in
     * the computer interface.
     * @param args unused
     * @throws java.rmi.RemoteException
     */
    public static void main(String[] args) throws Exception {

        System.setSecurityManager(new SecurityManager());

        Computer computer = new ComputerImpl();
        Computer stub = (Computer) UnicastRemoteObject.exportObject(computer, 0);

        Registry registry = LocateRegistry.createRegistry(1099);

        registry.rebind(Computer.SERVICE_NAME, stub);

        System.out.println("ComputerImpl.main Ready.");
    }

}

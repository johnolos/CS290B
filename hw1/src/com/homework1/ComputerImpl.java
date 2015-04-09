package com.homework1;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ComputerImpl implements Computer {


    @Override
    public <T> T execute(Task<T> t) throws RemoteException {
        return t.execute();
    }


    public static void main(String[] args) throws Exception {

        System.setSecurityManager(new SecurityManager());

        Computer computer = new ComputerImpl();
        Computer stub = (Computer) UnicastRemoteObject.exportObject(computer, 0);

        Registry registry = LocateRegistry.createRegistry(1099);

        registry.rebind(Computer.SERVICE_NAME, stub);

        System.out.println("ComputerImpl.main Ready.");
    }

}

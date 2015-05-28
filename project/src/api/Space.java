/*
 * The MIT License
 *
 * Copyright 2015 peter.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package api;

import api.events.EventListener;
import system.Task;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import system.Computer;

/**
 *
 * @author Peter Cappello
 */
public interface Space extends Remote 
{
    /**
     * The port used by the RMI registry.
     */
    public static int PORT = 8001;

    /**
     * The service name associated with this Remote interface.
     */
    public static String SERVICE_NAME = "Space";

    /**
     *
     * @param task to be computed.
     * @return the task's execute method return value.
     * @throws RemoteException
     */
    ReturnValue compute( final Task task ) throws RemoteException;
    
    /**
     *
     * @param task to be computed.
     * @param shared the shared object.
     * @return the task's execute method return value.
     * @throws RemoteException
     */
    ReturnValue compute( Task task, Shared shared ) throws RemoteException;

    /**
     *
     * @param task to be computed.
     * @param shared the shared object.
     * @param eventListener to be called when an event happens.
     * @return the task's execute method return value.
     * @throws RemoteException
     */
    ReturnValue compute( Task task, Shared shared, EventListener eventListener) throws RemoteException;
    
    /**
     *
     * @param taskList
     * @throws RemoteException
     */
    void putAll ( final List<Task> taskList ) throws RemoteException;
    
    /**
     *
     * @param computer
     * @param numWorkerProxies
     * @throws RemoteException
     */
    void register( final Computer computer, int numWorkerProxies ) throws RemoteException;

    /**
     *
     * @return
     * @throws RemoteException
     */
    ReturnValue take() throws RemoteException;  
}

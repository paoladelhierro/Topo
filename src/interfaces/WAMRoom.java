package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * WAMRoom
 */
public interface WAMRoom extends Remote{

    public void addUser(String user) throws RemoteException;
    public int addPoint(String user) throws RemoteException;
    public String getScore() throws RemoteException;
    public boolean done() throws RemoteException;
    public void reset() throws RemoteException;
    public int playerCount() throws RemoteException;

}
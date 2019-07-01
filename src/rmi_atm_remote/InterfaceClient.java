package rmi_atm_remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceClient extends Remote {
    void show(String message) throws RemoteException;
}

package rmi_atm_remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface InterfaceATM extends Remote {

    double verifyMoney (int code) throws RemoteException;
    void deposit (int code, double value) throws RemoteException;
    void withdow (int code, double value) throws RemoteException;
    void transfer (int origin, int destination, double value) throws RemoteException;
    void createAccount (int code, String name, double money) throws RemoteException;
    List<Account> findAll () throws RemoteException;
    Account findOne (int code) throws RemoteException;
    boolean hasAccount (int code) throws RemoteException;
}

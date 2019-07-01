
package rmi_atm_remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class SystemATM extends UnicastRemoteObject implements InterfaceATM {

    List<Account> accounts;
    public String password = "admin";
    
    public SystemATM() throws RemoteException {
        super();
        accounts = new ArrayList<>();
    }

    @Override
    public double verifyMoney(int code) throws RemoteException {
        return findOne(code).getMoney();
    }

    @Override
    public void deposit(int code, double value) throws RemoteException {
        findOne(code).setDeposit(value);
    }

    @Override
    public void withdow(int code, double value) throws RemoteException {
        findOne(code).setWithdraw(value);
    }

    @Override
    public void transfer(int origin, int destination, double value) throws RemoteException {
        findOne(origin).setWithdraw(value);
        findOne(destination).setDeposit(value);
    }

    @Override
    public void createAccount(int code, String name, double money) throws RemoteException {
        accounts.add(new Account(code, name, money));
    }

    @Override
    public List<Account> findAll () throws RemoteException {
        return accounts;
    }

    @Override
    public Account findOne(int code) throws RemoteException {
        for(Account acc : accounts)
            if(acc.getMoney() == code)
                return acc;
        return null;
    }

    @Override
    public boolean hasAccount(int code) throws RemoteException {
        for(Account acc : accounts )
            if(acc.getCode() == code)
                return true;
        return false;
    }
    
}

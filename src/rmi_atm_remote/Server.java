package rmi_atm_remote;

import java.rmi.Naming;

public class Server {
    public static void main(String[] args) {
        try {
            SystemATM atm = new SystemATM();
            Naming.rebind("rmi://localhost:3001/atm", atm);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}

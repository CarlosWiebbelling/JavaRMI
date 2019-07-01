package rmi_atm_remote;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {

    private static SystemATM sysATM;
    private static Scanner scan;
    
    public static void main(String[] args) {
        try {
            sysATM = new SystemATM();
            sysATM = (SystemATM) Naming.lookup("rmi://localhost:3001/atm");

            options();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void options(){
        try{
            scan = new Scanner(System.in);  
            int code;

            String menu = 
                    "\n\nAutomated Teller Machine\n\n" + 
                    "1) Verificar Saldo\n2) Realizar Depósito\n" + 
                    "3) Realizar Saque\n" + 
                    "4) Realizar Transferência entre contas\n" + 
                    "5) Criar conta (admin)\n" + 
                    "6) Listar todos os clientes (admin)\n" + 
                    "7) Finalizar";

            do {
                System.out.println(menu);
                code = scan.nextInt();
                scan.nextLine();
                
                switch(code) {
                    case 1:
                        System.out.println("\n1) - Verificar saldo");
                        verifyMoney();
                        break;
                    case 2:
                        System.out.println("\n2) - Realizar depósito");
                        deposit();
                        break;
                    case 3:
                        System.out.println("\n3) - Realizar Saque");
                        withdow();
                        break;
                    case 4:
                        System.out.println("\n4) - Realizar Transferência entre contas");
                        transfer();
                        break;
                    case 5:
                        System.out.println("\n5) Criar conta (admin)");
                        createAccount();
                        break;
                    case 6:
                        System.out.println("\n6) - Listar todos os clientes (admin)");
                        findAll();
                        break; 
                    case 7:
                        System.out.println("\n7) - Finalizar");
//                        outputData.println("7");
                        break; 
                    default: 
                        System.out.println("\nInsira uma opção válida!");
                        break;
                }

            } while (code != 7);
        } catch(Exception e) {
            System.out.println("err: " + e);
        }
    }
    
    public static void verifyMoney() {
        System.out.println("Que conta desejas verificar o saldo? ");
        int codeAccount = scan.nextInt();
        
        if(verifyNegative(codeAccount))
            return;
        
        try {
            if(sysATM.hasAccount(codeAccount)) {
                Account acc = sysATM.findOne(codeAccount);
                System.out.println("Conta " + acc.getCode() + " Saldo: " + acc.getMoney());;
            } else
                System.out.println("Conta não encontrada!");
            
        } catch (RemoteException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void deposit() {
        System.out.println("Para que conta desejas depositar? ");
        int code = scan.nextInt();
        
        if(verifyNegative(code)) 
            return;
        
        try {
            if(!sysATM.hasAccount(code)) {
                System.out.println("Conta não encontrada");
                return;
            }
            System.out.println("Qual o valor do depósito? ");
            float value = scan.nextFloat();

            if(verifyNegative(value))
                return;
            
            sysATM.deposit(code, value);
            
            System.out.println("Depósito realizado com sucesso!");
        } catch (RemoteException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void withdow() {
        System.out.println("De que conta desejas realizar saque? ");
        int code = scan.nextInt();
        
        if(verifyNegative(code))
            return;
        
        try {
            if(!sysATM.hasAccount(code)) {
                System.out.println("Conta não encontrada");
                return;
            }
            System.out.println("Qual o valor do saque? ");
            double value = scan.nextDouble();

            if(verifyNegative(value))
                return;
            
            if(sysATM.findOne(code).getMoney() < value) {
                System.out.println("Saldo insuficiente");
                return;
            }
            
            sysATM.withdow(code, value);
            
            System.out.println("Saque realizado com sucesso!");
        } catch (RemoteException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void transfer() {
        System.out.println("Conta de origem: ");
        int origin = scan.nextInt();
        scan.nextLine();
        
        if(verifyNegative(origin))
            return;
        
        try {
            if(!sysATM.hasAccount(origin)) {
                System.out.println("Conta não encontrada");
                return;
            }
        
            System.out.println("Conta de destino: ");
            int destination = scan.nextInt();
            scan.nextLine();
            
            if(verifyNegative(destination))
                return;
            
            if(origin == destination) {
                System.out.println("Transação inválida");
                return;
            }
            
            if(!sysATM.hasAccount(destination)) {
                System.out.println("Conta não encontrada");
                return;
            }
            
            System.out.println("Valor da transação: ");
            double value = scan.nextDouble();

            if(verifyNegative(value))
                return;
            
            if(sysATM.findOne(origin).getMoney() < value) {
                System.out.println("Saldo insuficiente!");
                return;
            }
            
            sysATM.transfer(origin, destination, value);
            
            System.out.println("Transferência realizada com sucesso!");
        } catch (RemoteException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void createAccount() {
        try {
            System.out.println("Senha: ");
            String password = scan.nextLine();
            
            if(!password.equals(sysATM.password)) {
                System.out.println("Senha incorreta!");
                return;
            }
            
            System.out.println("Digite o código da conta: ");
            int code = scan.nextInt();
            scan.nextLine();

            if(verifyNegative(code))
                return;
        
            if(sysATM.hasAccount(code)) {
                System.out.println("Código em uso!");
                return;
            }
            
            String name = "";
            do {
                System.out.println("Nome da conta: ");
                name = scan.nextLine();
            } while(inputEmpty(name));

            System.out.println("Saldo inicial: ");
            
            double value = scan.nextDouble();
            scan.nextLine();

            if(verifyNegative(value))
                return;
            
            sysATM.createAccount(code, name, value);
            
            System.out.println("Conta criada com sucesso!");
            
        } catch (RemoteException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void findAll() {
        List<Account> accounts;
        
        try {
            System.out.println("Senha: ");
            String password = scan.nextLine();

            if(!password.equals(sysATM.password)) {
                System.out.println("Senha incorreta!");
                return;
            }

            accounts = sysATM.findAll();
            
            for(Account acc : accounts)
                System.out.println(
                        "Conta: " + acc.getCode() +
                        " Name: " + acc.getName() +
                        " Saldo: R$" + acc.getMoney()
                );
                
        } catch (RemoteException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static boolean verifyNegative(int number) {
        if(number < 0) {
            System.out.println("Valor negativo inválido!");
            return true;
        }
        return false;
    }

    public static boolean verifyNegative(double number) {
        if(number < 0) {
            System.out.println("Valor negativo inválido!");
            return true;
        }
        return false;
    }

    public static boolean inputEmpty(String string) {
        if(string.length() == 0) {
            System.out.println("Por favor, preencha todos os campos!");
            return true;
        }
        return false;
    }
}

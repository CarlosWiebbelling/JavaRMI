package rmi_atm_remote;

public class Account {
    
    public int code;
    public String name;
    public double money;

    public Account(int code, String name, double money) {
        this.code = code;
        this.name = name;
        this.money = money;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }
    
    public void setDeposit(double money) {
        this.money += money;
    }
    
    public void setWithdraw(double money) {
        this.money -= money;
    }
    
}

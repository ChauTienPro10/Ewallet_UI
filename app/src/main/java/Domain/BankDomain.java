package Domain;

public class BankDomain {
    private String name;
    private Double balance;
    private String picPath;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public BankDomain(String name, Double balance, String picPath) {
        this.name = name;
        this.balance = balance;
        this.picPath = picPath;
    }
}

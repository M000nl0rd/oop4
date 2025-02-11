public class ATM {
    private int atmNumber;
    private String address;
    private double balance;

    public ATM(int atmNumber, String address, double balance) {
        this.atmNumber = atmNumber;
        this.address = address;
        this.balance = balance;
    }

    public int getAtmNumber() {
        return atmNumber;
    }

    public String getAddress() {
        return address;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}

public class Account {
    private int number;
    private String pinCode;
    private double balance;
    private String firstName;
    private String lastName;

    public Account(int number, String pinCode, double balance, String firstName, String lastName) {
        this.number = number;
        this.pinCode = pinCode;
        this.balance = balance;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getNumber() {
        return number;
    }

    public String getPinCode() {
        return pinCode;
    }

    public double getBalance() {
        return balance;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}

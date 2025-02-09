public class Account {
    private int number;
    private String pinCode;
    private double remainder;

    public Account(int number, String pinCode, double remainder) {
        this.number = number;
        this.pinCode = pinCode;
        this.remainder = remainder;
    }

    public int getNumber() {
        return number;
    }

    public String getPinCode() {
        return pinCode;
    }

    public double getRemainder() {
        return remainder;
    }

    public void setRemainder(double remainder) {
        this.remainder = remainder;
    }
}

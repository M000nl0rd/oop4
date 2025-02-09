public class ATM {
    private int atmNumber;
    private String address;

    public ATM(int atmNumber, String address) {
        this.atmNumber = atmNumber;
        this.address = address;
    }

    public int getAtmNumber() {
        return atmNumber;
    }

    public String getAddress() {
        return address;
    }
}

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDAO {

    // Add a new account for a specific bank
    public static void addAccount(int number, String pinCode, double balance, String firstName, String lastName, String bankName) {
        String sql = "INSERT INTO accounts (number, pin_code, balance, first_name, last_name, bank_name) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, number);
            stmt.setString(2, pinCode);
            stmt.setDouble(3, balance);
            stmt.setString(4, firstName);
            stmt.setString(5, lastName);
            stmt.setString(6, bankName);
            stmt.executeUpdate();
            System.out.println("Account added successfully!");
        } catch (SQLException e) {
            System.out.println("Error while adding account: " + e.getMessage());
        }
    }

    // Remove an account by number for a specific bank
    public static void removeAccount(int number, String bankName) {
        String sql = "DELETE FROM accounts WHERE number = ? AND bank_name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, number);
            stmt.setString(2, bankName);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Account removed successfully!");
            } else {
                System.out.println("Account not found!");
            }
        } catch (SQLException e) {
            System.out.println("Error while removing account: " + e.getMessage());
        }
    }

    // Retrieve an account by number and PIN for a specific bank
    public static Account getAccount(int number, String pinCode, String bankName) {
        String sql = "SELECT * FROM accounts WHERE number = ? AND pin_code = ? AND bank_name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, number);
            stmt.setString(2, pinCode);
            stmt.setString(3, bankName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int accNumber = rs.getInt("number");
                String dbPin = rs.getString("pin_code");
                double balance = rs.getDouble("balance");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                return new Account(accNumber, dbPin, balance, firstName, lastName);
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error while retrieving account: " + e.getMessage());
            return null;
        }
    }

    // Retrieve an account by number (without PIN check) for a specific bank
    public static Account getAccountByNumber(int number, String bankName) {
        String sql = "SELECT * FROM accounts WHERE number = ? AND bank_name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, number);
            stmt.setString(2, bankName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int accNumber = rs.getInt("number");
                String dbPin = rs.getString("pin_code");
                double balance = rs.getDouble("balance");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                return new Account(accNumber, dbPin, balance, firstName, lastName);
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error while retrieving account: " + e.getMessage());
            return null;
        }
    }

    // Deposit money into an account for a specific bank
    public static void replenishAccount(int number, double amount, String bankName) {
        String sql = "UPDATE accounts SET balance = balance + ? WHERE number = ? AND bank_name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, amount);
            stmt.setInt(2, number);
            stmt.setString(3, bankName);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Account replenished!");
            } else {
                System.out.println("Account not found!");
            }
        } catch (SQLException e) {
            System.out.println("Error while replenishing account: " + e.getMessage());
        }
    }

    // Withdraw money from an account (with balance check) for a specific bank
    // Returns true if the withdrawal is successful, otherwise false
    public static boolean withdrawAccount(int number, double amount, String bankName) {
        Account account = getAccountByNumber(number, bankName);
        if (account == null) {
            System.out.println("Account not found!");
            return false;
        }
        if (account.getBalance() < amount) {
            System.out.println("Insufficient funds!");
            return false;
        }
        String sql = "UPDATE accounts SET balance = balance - ? WHERE number = ? AND bank_name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, amount);
            stmt.setInt(2, number);
            stmt.setString(3, bankName);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Withdrawal successful!");
                return true;
            } else {
                System.out.println("Error during withdrawal!");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error during withdrawal: " + e.getMessage());
            return false;
        }
    }
}

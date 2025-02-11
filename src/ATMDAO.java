import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ATMDAO {

    // Add an ATM with a balance for a specific bank
    public static void addATM(int atmNumber, String address, double balance, String bankName) {
        String sql = "INSERT INTO atms (atm_number, address, balance, bank_name) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, atmNumber);
            stmt.setString(2, address);
            stmt.setDouble(3, balance);
            stmt.setString(4, bankName);
            stmt.executeUpdate();
            System.out.println("ATM added successfully");
        } catch (SQLException e) {
            System.out.println("Error while adding ATM: " + e.getMessage());
        }
    }

    // Remove an ATM by number for a specific bank
    public static void removeATM(int atmNumber, String bankName) {
        String sql = "DELETE FROM atms WHERE atm_number = ? AND bank_name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, atmNumber);
            stmt.setString(2, bankName);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("ATM removed successfully!");
            } else {
                System.out.println("ATM not found!");
            }
        } catch (SQLException e) {
            System.out.println("Error while removing ATM: " + e.getMessage());
        }
    }

    // Retrieve an ATM by number for a specific bank
    public static ATM getATM(int atmNumber, String bankName) {
        String sql = "SELECT * FROM atms WHERE atm_number = ? AND bank_name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, atmNumber);
            stmt.setString(2, bankName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String address = rs.getString("address");
                double balance = rs.getDouble("balance");
                return new ATM(atmNumber, address, balance);
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error while retrieving ATM: " + e.getMessage());
            return null;
        }
    }

    // Update the ATM balance during withdrawal for a specific bank
    public static boolean withdrawFromATM(int atmNumber, double amount, String bankName) {
        String sql = "UPDATE atms SET balance = balance - ? WHERE atm_number = ? AND bank_name = ? AND balance >= ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, amount);
            stmt.setInt(2, atmNumber);
            stmt.setString(3, bankName);
            stmt.setDouble(4, amount);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("ATM balance updated successfully.");
                return true;
            } else {
                System.out.println("Not enough cash in ATM.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error while updating ATM balance: " + e.getMessage());
            return false;
        }
    }
    public static boolean depositToATM(int atmNumber, double amount, String bankName) {
        String sql = "UPDATE atms SET balance = balance + ? WHERE atm_number = ? AND bank_name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, amount);
            stmt.setInt(2, atmNumber);
            stmt.setString(3, bankName);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("ATM balance increased successfully.");
                return true;
            } else {
                System.out.println("Error updating ATM balance.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error updating ATM balance: " + e.getMessage());
            return false;
        }
    }

}

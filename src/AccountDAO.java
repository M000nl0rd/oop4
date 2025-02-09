import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDAO {

    // Добавление нового счета
    public static void addAccount(int number, String pinCode, double balance) {
        String sql = "INSERT INTO accounts (number, pin_code, balance) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, number);
            stmt.setString(2, pinCode);
            stmt.setDouble(3, balance);
            stmt.executeUpdate();
            System.out.println("Account added successfully!");
        } catch (SQLException e) {
            System.out.println("Error while adding account: " + e.getMessage());
        }
    }

    // Удаление счета по номеру
    public static void removeAccount(int number) {
        String sql = "DELETE FROM accounts WHERE number = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, number);
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

    // Получение аккаунта по номеру и PIN (для аутентификации)
    public static Account getAccount(int number, String pinCode) {
        String sql = "SELECT * FROM accounts WHERE number = ? AND pin_code = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, number);
            stmt.setString(2, pinCode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int accNumber = rs.getInt("number");
                String dbPin = rs.getString("pin_code");
                double balance = rs.getDouble("balance");
                return new Account(accNumber, dbPin, balance);
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error while cathing account " + e.getMessage());
            return null;
        }
    }

    // Получение аккаунта только по номеру (без проверки PIN)
    public static Account getAccountByNumber(int number) {
        String sql = "SELECT * FROM accounts WHERE number = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, number);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int accNumber = rs.getInt("number");
                String dbPin = rs.getString("pin_code");
                double balance = rs.getDouble("balance");
                return new Account(accNumber, dbPin, balance);
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error while getting account: " + e.getMessage());
            return null;
        }
    }

    // Пополнение счета
    public static void replenishAccount(int number, double amount) {
        String sql = "UPDATE accounts SET balance = balance + ? WHERE number = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, amount);
            stmt.setInt(2, number);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Account has been replenished!");
            } else {
                System.out.println("Account not found!");
            }
        } catch (SQLException e) {
            System.out.println("Error when adding funds to the account: " + e.getMessage());
        }
    }

    // Снятие средств со счета (проверяется наличие достаточного баланса)
    public static void withdrawAccount(int number, double amount) {
        Account account = getAccountByNumber(number);
        if (account == null) {
            System.out.println("Account not found!");
            return;
        }
        if (account.getRemainder() < amount) {
            System.out.println("Insufficient funds!");
            return;
        }
        String sql = "UPDATE accounts SET balance = balance - ? WHERE number = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, amount);
            stmt.setInt(2, number);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("The withdrawal operation was successful!");
            } else {
                System.out.println("Error when removing funds!");
            }
        } catch (SQLException e) {
            System.out.println("Error when removing funds: " + e.getMessage());
        }
    }
}

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ATMDAO {

    // Добавление банкомата
    public static void addATM(int atmNumber, String address) {
        String sql = "INSERT INTO atms (atm_number, address) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, atmNumber);
            stmt.setString(2, address);
            stmt.executeUpdate();
            System.out.println("ATM added successfully");
        } catch (SQLException e) {
            System.out.println("Error while adding ATM " + e.getMessage());
        }
    }

    // Удаление банкомата по идентификационному номеру
    public static void removeATM(int atmNumber) {
        String sql = "DELETE FROM atms WHERE atm_number = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, atmNumber);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("ATM removed successfully!");
            } else {
                System.out.println("ATM not found!");
            }
        } catch (SQLException e) {
            System.out.println("Error while removing ATM " + e.getMessage());
        }
    }
}

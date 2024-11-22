import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegistrationService {
    private static final Logger LOGGER = Logger.getLogger(RegistrationService.class.getName());

    public boolean registerForBanquet(String email, int bin, int mealChoice, String remarks) {
        String checkQuotaQuery = "SELECT Quota FROM Banquet WHERE BIN = ? AND Available = 'Y'";
        String registerQuery = "INSERT INTO Registration (Email, BIN, MealChoice, Remarks, RegistrationTime) VALUES (?, ?, ?, ?, NOW())";
        String updateQuotaQuery = "UPDATE Banquet SET Quota = Quota - 1 WHERE BIN = ?";

        try (Connection conn = DB_query.getConnection()) {
            conn.setAutoCommit(false);

            // Check if there are available seats
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuotaQuery)) {
                checkStmt.setInt(1, bin);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    int availableSeats = rs.getInt("Quota");
                    if (availableSeats <= 0) {
                        conn.rollback();
                        return false;
                    }
                } else {
                    conn.rollback();
                    return false;
                }
            }

            // Register the attendee
            try (PreparedStatement registerStmt = conn.prepareStatement(registerQuery)) {
                registerStmt.setString(1, email);
                registerStmt.setInt(2, bin);
                registerStmt.setInt(3, mealChoice);
                registerStmt.setString(4, remarks);
                registerStmt.executeUpdate();
            }

            // Update the quota
            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuotaQuery)) {
                updateStmt.setInt(1, bin);
                updateStmt.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An error occurred while registering for banquet.", e);
            return false;
        }
    }

    public List<Registration> searchRegisteredBanquets(String email, String date, String banquetName) {
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT b.BIN, b.BanquetName, b.DateTime, b.Address, b.Location, m.Type AS MealType, m.DishName " +
                        "FROM Registration r " +
                        "JOIN Banquet b ON r.BIN = b.BIN " +
                        "JOIN Meal m ON r.MealChoice = m.MealID " +
                        "WHERE r.Email = ?");

        if (date != null && !date.trim().isEmpty()) {
            queryBuilder.append(" AND DATE(b.DateTime) = ?");
        }
        if (banquetName != null && !banquetName.trim().isEmpty()) {
            queryBuilder.append(" AND b.BanquetName LIKE ?");
        }

        List<Registration> registrations = new ArrayList<>();

        try (Connection conn = DB_query.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(queryBuilder.toString())) {

            pstmt.setString(1, email);
            int paramIndex = 2;

            if (date != null && !date.trim().isEmpty()) {
                pstmt.setString(paramIndex++, date);
            }
            if (banquetName != null && !banquetName.trim().isEmpty()) {
                pstmt.setString(paramIndex, "%" + banquetName + "%");
            }

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Registration registration = new Registration(
                        rs.getInt("BIN"),
                        rs.getString("BanquetName"),
                        rs.getString("DateTime"),
                        rs.getString("Address"),
                        rs.getString("Location"),
                        rs.getString("MealType"),
                        rs.getString("DishName")
                );
                registrations.add(registration);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An error occurred while searching for registered banquets.", e);
        }

        return registrations;
    }
}
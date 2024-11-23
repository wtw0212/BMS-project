package Registration;

import Database.DB_query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegistrationService {
    private static final Logger LOGGER = Logger.getLogger(RegistrationService.class.getName());

    public boolean registerForBanquet(String email, int bin, int mealChoice, String remarks, String seatNumber) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DB_query.getConnection();
            conn.setAutoCommit(false);

            // Check if the user is already registered for this banquet
            String checkQuery = "SELECT COUNT(*) FROM Registration WHERE Email = ? AND BIN = ?";
            pstmt = conn.prepareStatement(checkQuery);
            pstmt.setString(1, email);
            pstmt.setInt(2, bin);
            rs = pstmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("You are already registered for this banquet.");
                return false;
            }

            // Register for the banquet
            String registerQuery = "INSERT INTO Registration (Email, BIN, MealChoice, Remarks) VALUES (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(registerQuery);
            pstmt.setString(1, email);
            pstmt.setInt(2, bin);
            pstmt.setInt(3, mealChoice);
            pstmt.setString(4, remarks);
            pstmt.executeUpdate();

            // Reserve a seat if seatNumber is provided
            if (seatNumber != null) {
                String reserveSeatQuery = "UPDATE SeatReservation SET Email = ? WHERE BIN = ? AND SeatNumber = ? AND Email IS NULL";
                pstmt = conn.prepareStatement(reserveSeatQuery);
                pstmt.setString(1, email);
                pstmt.setInt(2, bin);
                pstmt.setString(3, seatNumber);
                int updatedRows = pstmt.executeUpdate();
                if (updatedRows == 0) {
                    throw new SQLException("Failed to reserve seat: " + seatNumber);
                }
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error registering for banquet", e);
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Error rolling back transaction", ex);
                }
            }
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error closing database resources", e);
            }
        }
    }

    public List<Registration> searchRegisteredBanquets(String email, String date, String banquetName) {
        List<Registration> registrations = new ArrayList<>();
        String query = "SELECT r.RegistrationID, r.Email, r.BIN, r.MealChoice, r.Remarks, r.RegistrationTime, " +
                "b.BanquetName, b.DateTime, b.Address, b.Location, " +
                "m.Type, m.DishName, sr.SeatNumber " +
                "FROM Registration r " +
                "JOIN Banquet b ON r.BIN = b.BIN " +
                "JOIN Meal m ON r.MealChoice = m.MealID " +
                "LEFT JOIN SeatReservation sr ON r.Email = sr.Email AND r.BIN = sr.BIN " +
                "WHERE r.Email = ? ";

        if (date != null && !date.isEmpty()) {
            query += "AND DATE(b.DateTime) = ? ";
        }
        if (banquetName != null && !banquetName.isEmpty()) {
            query += "AND b.BanquetName LIKE ? ";
        }

        try (Connection conn = DB_query.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
            int paramIndex = 2;
            if (date != null && !date.isEmpty()) {
                pstmt.setString(paramIndex++, date);
            }
            if (banquetName != null && !banquetName.isEmpty()) {
                pstmt.setString(paramIndex, "%" + banquetName + "%");
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Registration registration = new Registration(
                            rs.getInt("RegistrationID"),
                            rs.getString("Email"),
                            rs.getInt("BIN"),
                            rs.getInt("MealChoice"),
                            rs.getString("Remarks"),
                            rs.getString("RegistrationTime")
                    );
                    registration.setSeatNumber(rs.getString("SeatNumber"));
                    registrations.add(registration);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error searching registered banquets", e);
        }
        return registrations;
    }

    public boolean isUserRegisteredForBanquet(String email, int bin) {
        String query = "SELECT COUNT(*) FROM Registration WHERE Email = ? AND BIN = ?";
        try (Connection conn = DB_query.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
            pstmt.setInt(2, bin);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking user registration", e);
        }
        return false;
    }

    public List<String> getAvailableSeats(int bin) {
        List<String> availableSeats = new ArrayList<>();
        String query = "SELECT SeatNumber FROM SeatReservation WHERE BIN = ? AND Email IS NULL";
        try (Connection conn = DB_query.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, bin);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    availableSeats.add(rs.getString("SeatNumber"));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching available seats", e);
        }
        return availableSeats;
    }

    public boolean reserveSeat(String email, int bin, String seatNumber) {
        String query = "UPDATE SeatReservation SET Email = ? WHERE BIN = ? AND SeatNumber = ? AND Email IS NULL";
        try (Connection conn = DB_query.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
            pstmt.setInt(2, bin);
            pstmt.setString(3, seatNumber);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error reserving seat", e);
        }
        return false;
    }

    public String getUserSeatForBanquet(String email, int bin) {
        String query = "SELECT SeatNumber FROM SeatReservation WHERE Email = ? AND BIN = ?";
        try (Connection conn = DB_query.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
            pstmt.setInt(2, bin);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("SeatNumber");
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting user's seat for banquet", e);
        }
        return null;
    }

    public String getMealInfo(int mealChoice) {
        String query = "SELECT Type, DishName FROM Meal WHERE MealID = ?";
        try (Connection conn = DB_query.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, mealChoice);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("Type") + " - " + rs.getString("DishName");
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting meal info", e);
        }
        return "Unknown";
    }
}


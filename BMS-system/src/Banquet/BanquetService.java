package Banquet;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import Database.DB_query;

public class BanquetService {
    private static final Logger LOGGER = Logger.getLogger(BanquetService.class.getName());

    public static List<Banquet> viewBanquets() {
        List<Banquet> banquets = new ArrayList<>();
        String query = "SELECT * FROM Banquet";

        try (Connection conn = DB_query.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Banquet banquet = new Banquet(
                        rs.getInt("BIN"),
                        rs.getString("BanquetName"),
                        rs.getString("DateTime"),
                        rs.getString("Address"),
                        rs.getString("Location"),
                        rs.getString("ContactFirstName"),
                        rs.getString("ContactLastName"),
                        rs.getString("Available"),
                        rs.getInt("Quota")
                );
                banquets.add(banquet);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error viewing banquets", e);
        }

        return banquets;
    }

    public static List<Banquet> viewAvailableBanquets() {
        List<Banquet> availableBanquets = new ArrayList<>();
        String query = "SELECT * FROM Banquet WHERE Available = 'Y'";

        try (Connection conn = DB_query.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Banquet banquet = new Banquet(
                        rs.getInt("BIN"),
                        rs.getString("BanquetName"),
                        rs.getString("DateTime"),
                        rs.getString("Address"),
                        rs.getString("Location"),
                        rs.getString("ContactFirstName"),
                        rs.getString("ContactLastName"),
                        rs.getString("Available"),
                        rs.getInt("Quota")
                );
                availableBanquets.add(banquet);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error viewing available banquets", e);
        }

        return availableBanquets;
    }

    public boolean createBanquet(Banquet banquet) {
        String query = "INSERT INTO Banquet (BanquetName, DateTime, Address, Location, F_NameOfTheContactStaff, L_NameOfTheContactStaff, Available, Quota) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DB_query.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, banquet.getBanquetName());
                pstmt.setString(2, banquet.getDateTime());
                pstmt.setString(3, banquet.getAddress());
                pstmt.setString(4, banquet.getLocation());
                pstmt.setString(5, banquet.getContactFirstName());
                pstmt.setString(6, banquet.getContactLastName());
                pstmt.setString(7, banquet.getAvailable());
                pstmt.setInt(8, banquet.getQuota());

                int affectedRows = pstmt.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Creating banquet failed, no rows affected.");
                }

                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int banquetId = generatedKeys.getInt(1);
                        createSeatsForBanquet(conn, banquetId, banquet.getQuota());
                    } else {
                        throw new SQLException("Creating banquet failed, no ID obtained.");
                    }
                }
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An error occurred while creating banquet.", e);
            return false;
        }
    }

    private void createSeatsForBanquet(Connection conn, int banquetId, int quota) throws SQLException {
        String query = "INSERT INTO SeatReservation (BIN, SeatNumber) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            for (int i = 1; i <= quota; i++) {
                pstmt.setInt(1, banquetId);
                pstmt.setString(2, String.format("S%03d", i));
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }

    public boolean updateBanquet(Banquet banquet) {
        String query = "UPDATE Banquet SET BanquetName = ?, DateTime = ?, Address = ?, Location = ?, ContactFirstName = ?, ContactLastName = ?, Available = ?, Quota = ? WHERE BIN = ?";

        try (Connection conn = DB_query.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, banquet.getBanquetName());
            pstmt.setString(2, banquet.getDateTime());
            pstmt.setString(3, banquet.getAddress());
            pstmt.setString(4, banquet.getLocation());
            pstmt.setString(5, banquet.getContactFirstName());
            pstmt.setString(6, banquet.getContactLastName());
            pstmt.setString(7, banquet.getAvailable());
            pstmt.setInt(8, banquet.getQuota());
            pstmt.setInt(9, banquet.getBIN());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating banquet", e);
            return false;
        }
    }

    public static boolean addMealToBanquet(int bin, String type, String dishName, double price, String specialCuisine) {
        String query = "INSERT INTO Meal (BIN, Type, DishName, Price, SpecialCuisine) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DB_query.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, bin);
            pstmt.setString(2, type);
            pstmt.setString(3, dishName);
            pstmt.setDouble(4, price);
            pstmt.setString(5, specialCuisine);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding meal to banquet", e);
            return false;
        }
    }

    public Banquet getBanquetByBIN(int bin) {
        String query = "SELECT * FROM Banquet WHERE BIN = ?";

        try (Connection conn = DB_query.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, bin);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Banquet(
                            rs.getInt("BIN"),
                            rs.getString("BanquetName"),
                            rs.getString("DateTime"),
                            rs.getString("Address"),
                            rs.getString("Location"),
                            rs.getString("ContactFirstName"),
                            rs.getString("ContactLastName"),
                            rs.getString("Available"),
                            rs.getInt("Quota")
                    );
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching banquet by BIN", e);
        }

        return null;
    }
}


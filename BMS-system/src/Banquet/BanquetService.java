package Banquet;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import Database.DB_query;

public class BanquetService {
    private static final Logger LOGGER = Logger.getLogger(BanquetService.class.getName());

    public boolean createBanquet(Banquet banquet) {
        String query = "INSERT INTO Banquet (BanquetName, DateTime, Address, Location, F_NameOfTheContactStaff, L_NameOfTheContactStaff, Available, Quota) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
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

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An error occurred while creating banquet.", e);
            return false;
        }
    }

    public boolean updateBanquet(Banquet banquet) {
        String query = "UPDATE Banquet SET BanquetName = ?, DateTime = ?, Address = ?, Location = ?, F_NameOfTheContactStaff = ?, L_NameOfTheContactStaff = ?, Available = ?, Quota = ? WHERE BIN = ?";

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
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An error occurred while adding meal to banquet.", e);
            return false;
        }
    }

    public static void viewAvailableBanquets() {
        String query = "SELECT b.*, " +
                "GROUP_CONCAT(CONCAT(m.Type, ': ', m.DishName, ' (', m.SpecialCuisine, ') - $', m.Price) SEPARATOR '; ') AS Meals " +
                "FROM Banquet b " +
                "LEFT JOIN Meal m ON b.BIN = m.BIN " +
                "WHERE b.Available = 'Y' AND b.Quota > 0 " +
                "GROUP BY b.BIN";

        try (Connection conn = DB_query.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("------------------------------------------------------------------------------------------------------");
            System.out.println("Banquet ID | Banquet Name | Date & Time | Address | Location | Contact Staff       | Available Seats |");
            System.out.println("------------------------------------------------------------------------------------------------------");
            System.out.println("Meals|");
            System.out.println("------");

            while (rs.next()) {
                int banquetId = rs.getInt("BIN");
                String banquetName = rs.getString("BanquetName");
                String banquetDate = rs.getString("DateTime");
                String address = rs.getString("Address");
                String location = rs.getString("Location"); // Retrieve Location
                String contactFirstName = rs.getString("F_NameOfTheContactStaff"); // Retrieve first name
                String contactLastName = rs.getString("L_NameOfTheContactStaff");   // Retrieve last name
                int availableSeats = rs.getInt("Quota");
                String meals = rs.getString("Meals");

                // Combine first and last name for display
                String contactStaffFullName = contactFirstName + " " + contactLastName;

                // Print banquet details
                System.out.printf("%-10d | %-12s | %-19s | %-22s | %-10s | %-20s | %-15d %n",
                        banquetId, banquetName, banquetDate, address, location, contactStaffFullName, availableSeats);

                // Print meals on a new line with proper formatting
                System.out.println("Meals: " + meals);
                System.out.println("------"); // Separator for meals
            }


            System.out.println("------------------------------------------------------------------------------------------------------------");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An error occurred while retrieving available banquets.", e);
            System.out.println("An error occurred while retrieving available banquets.");
        }
    }

    public void viewBanquets() {
        String banquetQuery = "SELECT b.*, b.F_NameOfTheContactStaff, b.L_NameOfTheContactStaff, " +
                "b.Available, " +
                "GROUP_CONCAT(CONCAT(m.Type, ': ', m.DishName, ' (', m.SpecialCuisine, ') - $', m.Price) SEPARATOR '; ') AS Meals " +
                "FROM Banquet b " +
                "LEFT JOIN Meal m ON b.BIN = m.BIN " +
                "GROUP BY b.BIN";

        try (Connection conn = DB_query.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(banquetQuery)) {

            System.out.println("-------------------------------------------------------------------------------------------------------------------");
            System.out.println("Banquet ID | Banquet Name | Date & Time | Address | Location | Contact Staff       | Available Seats | Availability |");
            System.out.println("-------------------------------------------------------------------------------------------------------------------");

            while (rs.next()) {
                int banquetId = rs.getInt("BIN");
                String banquetNameResult = rs.getString("BanquetName");
                String banquetDate = rs.getString("DateTime");
                String address = rs.getString("Address");
                String location = rs.getString("Location");
                String contactFirstName = rs.getString("F_NameOfTheContactStaff");
                String contactLastName = rs.getString("L_NameOfTheContactStaff");
                int availableSeats = rs.getInt("Quota");
                String meals = rs.getString("Meals");
                String availability = rs.getString("Available");

                String contactStaffFullName = contactFirstName + " " + contactLastName;

                System.out.printf("%-10d | %-12s | %-19s | %-22s | %-10s | %-20s | %-15d | %-12s%n",
                        banquetId, banquetNameResult, banquetDate, address, location, contactStaffFullName, availableSeats, availability);

                System.out.println("Meals: " + meals);
                System.out.println("------");
            }

            System.out.println("------------------------------------------------------------------------------------------------------------");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An error occurred while retrieving available banquets.", e);
            System.out.println("An error occurred while retrieving available banquets.");
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
                            rs.getString("F_NameOfTheContactStaff"),
                            rs.getString("L_NameOfTheContactStaff"),
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

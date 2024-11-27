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

    private static void printBanquetInfo(ResultSet rs) throws SQLException {
        System.out.printf("Banquet ID: %d%n", rs.getInt("BIN"));
        System.out.printf("Name: %s%n", rs.getString("BanquetName"));
        System.out.printf("Date & Time: %s%n", rs.getString("DateTime"));
        System.out.printf("Address: %s%n", rs.getString("Address"));
        System.out.printf("Location: %s%n", rs.getString("Location"));
        System.out.printf("Contact Staff: %s %s%n", rs.getString("F_NameOfTheContactStaff"), rs.getString("L_NameOfTheContactStaff"));
        System.out.printf("Available Seats: %d%n", rs.getInt("Quota"));
        System.out.printf("Availability: %s%n", rs.getString("Available"));
        System.out.println("Meals:");
    }

    private static void printMealTable(String mainCourses, String appetizers, String desserts) {
        String[] mainCourseArray = mainCourses != null ? mainCourses.split("\\|") : new String[0];
        String[] appetizerArray = appetizers != null ? appetizers.split("\\|") : new String[0];
        String[] dessertArray = desserts != null ? desserts.split("\\|") : new String[0];

        int maxRows = Math.max(Math.max(mainCourseArray.length, appetizerArray.length), dessertArray.length);

        System.out.println("+-------------------------------------------------------+------------------------------------+------------------------------------+");
        System.out.println("|            Main Courses (Special Cuisine)             |             Appetizers             |              Desserts              |");
        System.out.println("+-------------------------------------------------------+------------------------------------+------------------------------------+");

        for (int i = 0; i < maxRows; i++) {
            System.out.printf("| %-40s | %-40s | %-40s |%n",
                    i < mainCourseArray.length ? truncateString(mainCourseArray[i], 60) : "",
                    i < appetizerArray.length ? truncateString(appetizerArray[i], 40) : "",
                    i < dessertArray.length ? truncateString(dessertArray[i], 40) : "");
        }

        System.out.println("+-------------------------------------------------------+------------------------------------+------------------------------------+");
    }

    private static String truncateString(String str, int maxLength) {
        if (str == null || str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength - 3) + "...";
    }

    public static List<Banquet> viewBanquetsList() {
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



    public static void viewAvailableBanquets() {
        String query = "SELECT b.*, " +
                "GROUP_CONCAT(CASE WHEN m.Type = 'Main Course' THEN CONCAT(m.DishName, ' (', IFNULL(m.SpecialCuisine, 'N/A'), ') - $', m.Price) END SEPARATOR '|') AS MainCourses, " +
                "GROUP_CONCAT(CASE WHEN m.Type = 'Appetizer' THEN CONCAT(m.DishName, ' (', IFNULL(m.SpecialCuisine, 'N/A'), ') - $', m.Price) END SEPARATOR '|') AS Appetizers, " +
                "GROUP_CONCAT(CASE WHEN m.Type = 'Dessert' THEN CONCAT(m.DishName, ' (', IFNULL(m.SpecialCuisine, 'N/A'), ') - $', m.Price) END SEPARATOR '|') AS Desserts " +
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

            while (rs.next()) {
                int banquetId = rs.getInt("BIN");
                String banquetName = rs.getString("BanquetName");
                String banquetDate = rs.getString("DateTime");
                String address = rs.getString("Address");
                String location = rs.getString("Location");
                String contactFirstName = rs.getString("F_NameOfTheContactStaff");
                String contactLastName = rs.getString("L_NameOfTheContactStaff");
                int availableSeats = rs.getInt("Quota");

                String contactStaffFullName = contactFirstName + " " + contactLastName;

                System.out.printf("%-10d | %-12s | %-19s | %-22s | %-10s | %-20s | %-15d %n",
                        banquetId, banquetName, banquetDate, address, location, contactStaffFullName, availableSeats);

                printMealTable(rs.getString("MainCourses"), rs.getString("Appetizers"), rs.getString("Desserts"));
                System.out.println("------------------------------------------------------------------------------------------------------");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An error occurred while retrieving available banquets.", e);
            System.out.println("An error occurred while retrieving available banquets.");
        }
    }

    public static void viewBanquets() {
        String banquetQuery = "SELECT b.*, " +
                "GROUP_CONCAT(CASE WHEN m.Type = 'Main Course' THEN CONCAT(m.DishName, ' (', IFNULL(m.SpecialCuisine, 'N/A'), ') - $', m.Price) END SEPARATOR '|') AS MainCourses, " +
                "GROUP_CONCAT(CASE WHEN m.Type = 'Appetizer' THEN CONCAT(m.DishName, ' (', IFNULL(m.SpecialCuisine, 'N/A'), ') - $', m.Price) END SEPARATOR '|') AS Appetizers, " +
                "GROUP_CONCAT(CASE WHEN m.Type = 'Dessert' THEN CONCAT(m.DishName, ' (', IFNULL(m.SpecialCuisine, 'N/A'), ') - $', m.Price) END SEPARATOR '|') AS Desserts " +
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
                String availability = rs.getString("Available");

                String contactStaffFullName = contactFirstName + " " + contactLastName;

                System.out.printf("%-10d | %-12s | %-19s | %-22s | %-10s | %-20s | %-15d | %-12s%n",
                        banquetId, banquetNameResult, banquetDate, address, location, contactStaffFullName, availableSeats, availability);

                printMealTable(rs.getString("MainCourses"), rs.getString("Appetizers"), rs.getString("Desserts"));
                System.out.println("-------------------------------------------------------------------------------------------------------------------");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An error occurred while retrieving banquets.", e);
            System.out.println("An error occurred while retrieving banquets.");
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

import java.sql.*;

public class DB_query {
    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/BMS";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "1qewasdrt";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
    }

    public static boolean isAdmin(String email, String password) {
        String adminQuery = "SELECT * FROM admins WHERE adminEmail = ? AND adminPassword = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(adminQuery)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isAttendee(String email, String password) {
        String attendeeQuery = "SELECT * FROM Attendees WHERE email = ? AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(attendeeQuery)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean registerUser(String firstName, String lastName, String address, String attendeeType, String email, String password, String mobileNumber, String affiliatedOrganization) {
        String query = "INSERT INTO Attendees (first_name, last_name, address, attendee_type, email, password, mobile_number, affiliated_organization) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, address);
            pstmt.setString(4, attendeeType);
            pstmt.setString(5, email);
            pstmt.setString(6, password);
            pstmt.setString(7, mobileNumber);
            pstmt.setString(8, affiliatedOrganization);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
// Method to retrieve banquets
public void viewBanquets() {
    String banquetQuery = "SELECT * FROM Banquets"; // Query to get all banquets
    String mealQuery = "SELECT * FROM Meals WHERE banquet_id = ?"; // Query to get meals for a specific banquet

    try (Connection conn = getConnection();
         Statement banquetStmt = conn.createStatement();
         ResultSet banquetRs = banquetStmt.executeQuery(banquetQuery)) {

        while (banquetRs.next()) {
            // Retrieve banquet details
            int banquetId = banquetRs.getInt("banquet_id");
            String banquetName = banquetRs.getString("banquet_name");
            String banquetDate = banquetRs.getString("banquet_date");
            String address = banquetRs.getString("address");
            String location = banquetRs.getString("location");
            String contactFirstName = banquetRs.getString("contact_first_name");
            String contactLastName = banquetRs.getString("contact_last_name");
            String available = banquetRs.getString("available");
            int quota = banquetRs.getInt("quota");

            // Display banquet details
            System.out.println("------------------------------------------------------------------------------------------------------------");
            System.out.println("Banquet ID | Banquet Name | Date & Time | Address | Location | Contact Staff | Available | Quota ");
            System.out.println("------------------------------------------------------------------------------------------------------------");
            System.out.printf("%-2d | %-18s | %-19s | %-22s | %-14s | %-10s | %-2s | %-5d ",
                    banquetId, banquetName, banquetDate, address, location, contactFirstName + " " + contactLastName, available, quota);

            // Now retrieve and display meals for this banquet
            try (PreparedStatement mealStmt = conn.prepareStatement(mealQuery)) {
                mealStmt.setInt(1, banquetId);
                ResultSet mealRs = mealStmt.executeQuery();

                System.out.println("\n"+"Meals:");
                System.out.println("------------------------------------------------------------------------------------------------------------");
                System.out.println("Meal Type | Dish Name | Special Cuisine | Price ");
                System.out.println("------------------------------------------------------------------------------------------------------------");
                while (mealRs.next()) {
                    String mealType = mealRs.getString("meal_type");
                    String dishName = mealRs.getString("dish_name");
                    double price = mealRs.getDouble("price");
                    String specialCuisine = mealRs.getString("special_cuisine");

                    // Display meal details
                    System.out.printf("%-5s | %-12s | %-6s | %.2f%n",
                            mealType, dishName, specialCuisine, price);
                }
            }

            System.out.println("--------------------------------------------------");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("An error occurred while retrieving banquets.");
    }
}

    // Method to retrieve attendees
    public void viewAttendees() {
        String query = "SELECT * FROM attendees"; // Adjust table name as necessary
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                // Assuming there are columns like id and name in the attendees table
                System.out.println("Attendee Email: " + rs.getString("email") + ", Name: " + rs.getString("first_name")+" "+rs.getString("last_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to generate a report (placeholder)
    public void generateReport() {
        System.out.println("Generating report...");
        // Implement report generation logic here}
    }
}

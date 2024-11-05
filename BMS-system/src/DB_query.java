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
        String query = "SELECT * FROM banquets"; // Adjust table name as necessary
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                // Assuming there are columns like id and name in the banquets table
                System.out.println("Banquet ID: " + rs.getInt("banquet_id") + ", Name: " + rs.getString("banquet_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
                System.out.println("Attendee ID: " + rs.getInt("attendee_id") + ", Name: " + rs.getString("first_name")+" "+rs.getString("last_name"));
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

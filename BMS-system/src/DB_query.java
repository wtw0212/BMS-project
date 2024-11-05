import java.sql.*;

public class DB_query {
    private final String dbUsername = "root";
    private final String dbPassword = "1qewasdrt";
    private final String url = "jdbc:mysql://127.0.0.1:3306/BMS"; // Update with your database name

    // Method to establish a connection to the database
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, dbUsername, dbPassword);
    }

    // Method to register a new user
    public boolean registerUser(String firstName, String lastName, String address, String attendeeType, String email, String password, String mobileNumber, String affiliatedOrganization) {
        String query = "INSERT INTO Attendees (first_name, last_name, address, attendee_type, email, password, mobile_number, affiliated_organization) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, address);
            pstmt.setString(4, attendeeType);
            pstmt.setString(5, email);
            pstmt.setString(6, password); // Consider hashing the password for security
            pstmt.setString(7, mobileNumber);
            pstmt.setString(8, affiliatedOrganization);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // Return true if registration was successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false if there was an error
        }
    }

    // Method to check admin login credentials
    public boolean managerLogin(String username, String password) {
        String query = "SELECT * FROM admins WHERE username = ? AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // Return true if login is successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false if there was an error
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
                System.out.println("Banquet ID: " + rs.getInt("id") + ", Name: " + rs.getString("name"));
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
                System.out.println("Attendee ID: " + rs.getInt("id") + ", Name: " + rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to generate a report (placeholder)
    public void generateReport() {
        System.out.println("Generating report...");
        // Implement report generation logic here
    }
}
import java.sql.*;

public class DB_query {
    private final String dbUsername = "root";
    private final String dbPassword = "1qewasdrt";
    private final String url = "jdbc:mysql://127.0.0.1:3306/BMS"; // Update with your database name

    // Method to establish a connection to the database
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, dbUsername, dbPassword);
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
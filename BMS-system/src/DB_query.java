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
        String adminQuery = "SELECT * FROM admin WHERE Email = ? AND Password = ?";
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
        String attendeeQuery = "SELECT * FROM Attendee WHERE email = ? AND password = ?";
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

    public static boolean registerUser(String name, String address, String attendeeType, String email, String password, String mobileNumber, String affiliatedOrganization) {
        String query = "INSERT INTO Attendee (Name, address, AttendeeType, email, password, MobileNumber, AffiliatedOrganization) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, name);
            pstmt.setString(2, address);
            pstmt.setString(3, attendeeType);
            pstmt.setString(4, email);
            pstmt.setString(5, password);
            pstmt.setString(6, mobileNumber);
            pstmt.setString(7, affiliatedOrganization);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Updated method to retrieve banquets with meals combined
    public void viewBanquets() {
        String banquetQuery = "SELECT b.*, " +
                "GROUP_CONCAT(CONCAT(m.Type, ': ', m.DishName, ' (', m.SpecialCuisine, ') - $', m.Price) SEPARATOR '; ') AS Meals " +
                "FROM Banquet b " +
                "LEFT JOIN Meal m ON b.BIN = m.BIN " +
                "GROUP BY b.BIN";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(banquetQuery)) {

            System.out.println("------------------------------------------------------------------------------------------------------------");
            System.out.println("Banquet ID | Banquet Name | Date & Time | Address | Contact Staff | Quota | Meals");
            System.out.println("------------------------------------------------------------------------------------------------------------");

            while (rs.next()) {
                int banquetId = rs.getInt("BIN");
                String banquetName = rs.getString("BanquetName");
                String banquetDate = rs.getString("DateTime");
                String address = rs.getString("Address");
                String contactStaff = rs.getString("NameOfTheContactStaff");
                int quota = rs.getInt("Quota");
                String meals = rs.getString("Meals");

                System.out.printf("%-10d | %-12s | %-19s | %-22s | %-13s | %-5d | %s%n",
                        banquetId, banquetName, banquetDate, address, contactStaff, quota, meals);
            }

            System.out.println("------------------------------------------------------------------------------------------------------------");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("An error occurred while retrieving banquets.");
        }
    }

    // Updated method to retrieve attendees with their registered banquets
    public void viewAttendees() {
        String query = "SELECT a.*, GROUP_CONCAT(b.BanquetName SEPARATOR ', ') AS RegisteredBanquets " +
                "FROM Attendee a " +
                "LEFT JOIN Registration r ON a.Email = r.Email " +
                "LEFT JOIN Banquet b ON r.BIN = b.BIN " +
                "GROUP BY a.Email";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("------------------------------------------------------------------------------------------------------------");
            System.out.println("Email | Name | Attendee Type | Mobile Number | Affiliated Organization | Registered Banquets");
            System.out.println("------------------------------------------------------------------------------------------------------------");

            while (rs.next()) {
                String email = rs.getString("Email");
                String name = rs.getString("Name");
                String attendeeType = rs.getString("AttendeeType");
                String mobileNumber = rs.getString("MobileNumber");
                String affiliatedOrg = rs.getString("AffiliatedOrganization");
                String registeredBanquets = rs.getString("RegisteredBanquets");

                System.out.printf("%-25s | %-20s | %-13s | %-13s | %-25s | %s%n",
                        email, name, attendeeType, mobileNumber, affiliatedOrg,
                        (registeredBanquets != null ? registeredBanquets : "None"));
            }

            System.out.println("------------------------------------------------------------------------------------------------------------");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("An error occurred while retrieving attendees.");
        }
    }

    public void generateReport() {
        System.out.println("Generating report...");

        try (Connection conn = getConnection()) {
            // 1. Registration Status Report
            String registrationQuery = "SELECT b.BanquetName, COUNT(r.RegistrationID) as TotalRegistrations, b.Quota, " +
                    "(b.Quota - COUNT(r.RegistrationID)) as RemainingSeats " +
                    "FROM Banquet b LEFT JOIN Registration r ON b.BIN = r.BIN " +
                    "GROUP BY b.BIN";

            // 2. Popular Meals Report
            String popularMealsQuery = "SELECT m.Type, m.DishName, COUNT(r.RegistrationID) as OrderCount " +
                    "FROM Meal m LEFT JOIN Registration r ON m.MealID = r.MealChoice " +
                    "GROUP BY m.MealID ORDER BY OrderCount DESC LIMIT 5";

            // 3. Attendance Behavior Report
            String attendanceBehaviorQuery = "SELECT a.AttendeeType, COUNT(DISTINCT r.Email) as UniqueAttendee, " +
                    "COUNT(r.RegistrationID) as TotalRegistrations " +
                    "FROM Attendee a LEFT JOIN Registration r ON a.Email = r.Email " +
                    "GROUP BY a.AttendeeType";

            // Execute queries and print reports
            System.out.println("\n1. Registration Status Report:");
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(registrationQuery)) {
                System.out.printf("%-30s %-20s %-10s %-15s%n", "Banquet Name", "Total Registrations", "Quota", "Remaining Seats");
                System.out.println("-------------------------------------------------------------------------------------");
                while (rs.next()) {
                    System.out.printf("%-30s %-20d %-10d %-15d%n",
                            rs.getString("BanquetName"),
                            rs.getInt("TotalRegistrations"),
                            rs.getInt("Quota"),
                            rs.getInt("RemainingSeats"));
                }
            }

            System.out.println("\n2. Popular Meals Report:");
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(popularMealsQuery)) {
                System.out.printf("%-15s %-30s %-15s%n", "Meal Type", "Dish Name", "Order Count");
                System.out.println("------------------------------------------------------------");
                while (rs.next()) {
                    System.out.printf("%-15s %-30s %-15d%n",
                            rs.getString("Type"),
                            rs.getString("DishName"),
                            rs.getInt("OrderCount"));
                }
            }

            System.out.println("\n3. Attendance Behavior Report:");
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(attendanceBehaviorQuery)) {
                System.out.printf("%-20s %-20s %-20s%n", "Attendee Type", "Unique Attendee", "Total Registrations");
                System.out.println("--------------------------------------------------------------------");
                while (rs.next()) {
                    System.out.printf("%-20s %-20d %-20d%n",
                            rs.getString("AttendeeType"),
                            rs.getInt("UniqueAttendee"),
                            rs.getInt("TotalRegistrations"));
                }
            }

        } catch (SQLException e) {
            System.out.println("An error occurred while generating the report.");
            e.printStackTrace();
        }
    }
}
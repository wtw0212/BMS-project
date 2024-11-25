package Database;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DB_query {
    private static final String DB_URL = "jdbc:mysql://sql12.freemysqlhosting.net:3306/sql12746990";
    private static final String DB_USERNAME = "sql12746990";
    private static final String DB_PASSWORD = "2sC3mUmzAn";
    private static final Logger LOGGER = Logger.getLogger(DB_query.class.getName());

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
            LOGGER.log(Level.SEVERE, "MySQL JDBC Driver not found.", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
    }

    public static void generateReport() {
        System.out.println("Generating report...");

        try (Connection conn = getConnection()) {
            // 1. Registration Status Report
            String registrationQuery = "SELECT b.BanquetName, COUNT(r.RegistrationID) as TotalRegistrations, b.Quota, " +
                    "(b.Quota - COUNT(r.RegistrationID)) as RemainingSeats " +
                    "FROM Banquet b LEFT JOIN Registration r ON b.BIN = r.BIN " +
                    "GROUP BY b.BIN";

            // 2. Popular Meals Report (updated to include all meal types)
            String popularMealsQuery = "SELECT m.Type, m.DishName, " +
                    "COUNT(CASE WHEN r.MainCourseChoice = m.MealID THEN 1 ELSE NULL END) + " +
                    "COUNT(CASE WHEN r.AppetizerChoice = m.MealID THEN 1 ELSE NULL END) + " +
                    "COUNT(CASE WHEN r.DessertChoice = m.MealID THEN 1 ELSE NULL END) as OrderCount " +
                    "FROM Meal m LEFT JOIN Registration r ON m.MealID IN (r.MainCourseChoice, r.AppetizerChoice, r.DessertChoice) " +
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
            LOGGER.log(Level.SEVERE, "An error occurred while generating the report.", e);
            System.out.println("An error occurred while generating the report.");
        }
    }
}


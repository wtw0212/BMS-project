import java.sql.*;
import oracle.jdbc.driver.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("----------------------------------------------------------------");
            System.out.println("             Welcome to the Banquet Manage System!");
            System.out.println("             COMP2411 Database System (Fall 2024)");
            System.out.println("                       Project Group 43");
            System.out.println();
            System.out.println("- [1] Manager Login");
            System.out.println("- [-1] Exit System");
            System.out.print(">>> Please select the above options x in [x]: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    managerLogin(scanner);
                    break;
                case -1:
                    System.out.println("Exiting the system. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        scanner.close();
    }

    private static void managerLogin(Scanner scanner) {
        // Connect to the database
        String dbUsername = "root";
        String dbPassword = "1qewasdrt";
        String url = "jdbc:mysql://127.0.0.1:3306/BMS"; // Update with your database name

        // Prompt for username and password
        System.out.print("Enter username: ");
        String username = scanner.next();
        System.out.print("Enter password: ");
        String password = scanner.next();

        // SQL query to check credentials
        String query = "SELECT * FROM admins WHERE username = ? AND password = ?";

        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the connection
            Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("Login successful! Welcome, " + username + "!");
                // Call the method to show options after successful login
                showManagerOptions(scanner, conn);
            } else {
                System.out.println("Invalid username or password. Please try again.");
            }

            // Clean up
            rs.close();
            pstmt.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("An error occurred while connecting to the database.");
        }
    }
    private static void showManagerOptions(Scanner scanner, Connection conn) {
        boolean loggedIn = true;

        while (loggedIn) {
            System.out.println("----------------------------------------------------------------");
            System.out.println("Please select an option:");
            System.out.println("- [1] View Banquets");
            System.out.println("- [2] View Attendees");
            System.out.println("- [3] Generate Report");
            System.out.println("- [-1] Exit");
            System.out.print(">>> Please select the above options x in [x]: ");

            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    viewBanquets(conn);
                    break;
                case 2:
                    viewAttendees(conn);
                    break;
                case 3:
                    generateReport(conn);
                    break;
                case -1:
                    System.out.println("Exiting the manager options.");
                    loggedIn = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void viewBanquets(Connection conn) {
        // Implement the logic to view banquets
        System.out.println("Viewing banquets...");
        // Example SQL query to fetch banquets
        String query = "SELECT * FROM banquets"; // Adjust table name as necessary
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                // Assuming there are columns like id and name in the banquets table
                System.out.println("Banquet ID: " + rs.getInt("id") + ", Name: " + rs.getString("name"));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void viewAttendees(Connection conn) {
        // Implement the logic to view attendees
        System.out.println("Viewing attendees...");
        // Example SQL query to fetch attendees
        String query = "SELECT * FROM attendees"; // Adjust table name as necessary
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                // Assuming there are columns like id and name in the attendees table
                System.out.println("Attendee ID: " + rs.getInt("id") + ", Name: " + rs.getString("name"));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void generateReport(Connection conn) {
        // Implement the logic to generate a report
        System.out.println("Generating report...");
        // Example logic for generating a report
        // You can customize this based on your requirements
    }
}

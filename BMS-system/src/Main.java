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
                showManagerOptions(scanner, new DB_query());
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

    private static void showManagerOptions(Scanner scanner, DB_query dbQuery) {
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
                    dbQuery.viewBanquets();
                    break;
                case 2:
                    dbQuery.viewAttendees();
                    break;
                case 3:
                    dbQuery.generateReport();
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
}
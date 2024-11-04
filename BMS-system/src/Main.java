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
        // Connect to the database (for project only, and password will be deleted after grading)
        String dbUsername = "\"23118761d\"";
        String dbPassword = "btracoql";
        String url = "jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms";

        // Prompt for username and password
        System.out.print("Enter username: ");
        String username = scanner.next();
        System.out.print("Enter password: ");
        String password = scanner.next();

        // SQL query to check credentials
        String query = "SELECT * FROM managers WHERE username = ? AND password = ?";

        try {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            OracleConnection conn = (OracleConnection)DriverManager.getConnection(url,dbUsername,dbPassword);
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("Login successful! Welcome, " + username + "!");
                // Proceed with manager functionalities
            } else {
                System.out.println("Invalid username or password. Please try again.");
            }

            // Clean up
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("An error occurred while connecting to the database.");
        }
    }
}
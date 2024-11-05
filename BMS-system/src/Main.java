import java.sql.*;
import oracle.jdbc.driver.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        DB_query dbQuery = new DB_query(); // Create an instance of DB_query

        while (running) {
            System.out.println("----------------------------------------------------------------");
            System.out.println("             Welcome to the Banquet Manage System!");
            System.out.println("             COMP2411 Database System (Fall 2024)");
            System.out.println("                       Project Group 43");
            System.out.println();
            System.out.println("- [1] Manager Login");
            System.out.println("- [2] User Registration");
            System.out.println("- [-1] Exit System");
            System.out.print(">>> Please select the above options x in [x]: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    managerLogin(scanner, dbQuery);
                    break;
                case 2:
                    registerUser(scanner, dbQuery);
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
    private static void registerUser(Scanner scanner, DB_query dbQuery) {
        // Prompt for user details
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter address: ");
        String address = scanner.nextLine();
        System.out.print("Enter attendee type (staff, student, alumni, guest): ");
        String attendeeType = scanner.nextLine();
        System.out.print("Enter email address: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter mobile number (8 digits): ");
        String mobileNumber = scanner.nextLine();
        System.out.print("Enter affiliated organization: ");
        String affiliatedOrganization = scanner.nextLine();

        // Validate input
        if (!email.contains("@")) {
            System.out.println("Invalid email address. Please include '@'.");
            return;
        }
        if (mobileNumber.length() != 8 || !mobileNumber.matches("\\d{8}")) {
            System.out.println("Mobile number must be an 8-digit number.");
            return;
        }
        if (!firstName.matches("[a-zA-Z]+") || !lastName.matches("[a-zA-Z]+")) {
            System.out.println("First name and last name must contain only English characters.");
            return;
        }
        // Call the registerUser method from DB_query
        boolean success = dbQuery.registerUser(firstName, lastName, address, attendeeType, email, password, mobileNumber, affiliatedOrganization);
        if (success) {
            System.out.println("Registration successful! Welcome, " + firstName + "!");
        } else {
            System.out.println("Registration failed. Please try again.");
        }
    }

    private static void managerLogin(Scanner scanner, DB_query dbQuery) {
        // Prompt for username and password
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        // Call the managerLogin method from DB_query
        boolean success = dbQuery.managerLogin(username, password);
        if (success) {
            System.out.println("Login successful! Welcome, " + username + "!");
            // Call the method to show options after successful login
            showManagerOptions(scanner, dbQuery);
        } else {
            System.out.println("Invalid username or password. Please try again.");
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
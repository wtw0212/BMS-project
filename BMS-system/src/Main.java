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
            System.out.println("- [1] Login");
            System.out.println("- [2] User Registration");
            System.out.println("- [-1] Exit System");
            System.out.print(">>> Please select the above options x in [x]: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    login(scanner);
                    break;
                case 2:
                    registerUser(scanner);
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

    private static void login(Scanner scanner) {
        // Prompt for username and password
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (DB_query.isAdmin(email, password)) {
            System.out.println("Admin login successful! Welcome, " + email + "!");
            new AdminOptions(scanner).showAdminOptions();
        } else if (DB_query.isAttendee(email, password)) {
            System.out.println("User login successful! Welcome, " + email + "!");
            new UserOptions(scanner).showUserOptions();
        } else {
            System.out.println("Invalid email or password. Please try again.");
        }
    }

    private static void registerUser(Scanner scanner) {
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

        boolean registered = DB_query.registerUser(firstName, lastName, address, attendeeType, email, password, mobileNumber, affiliatedOrganization);
        if (registered) {
            System.out.println("Registration successful! Welcome, " + firstName + "!");
        } else {
            System.out.println("Registration failed. Please try again.");
        }
    }
}
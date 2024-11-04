import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("----------------------------------------------------------------");
            System.out.println("             Welcome to the Banquet Manage System!");
            System.out.println("             COMP2411 Database System (2024 Fall)");
            System.out.println("                       Project Group 43");
            System.out.println();
            System.out.println("- [1] Manager Login");
            System.out.println("- [-1] Exit System");
            System.out.print(">>> Please select the above options x in [x]: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    managerLogin();
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

    private static void managerLogin() {
        // Placeholder for manager login functionality
        System.out.println("Manager login functionality is not yet implemented.");
        // Here you would typically prompt for username and password,
        // then verify these credentials against your SQL database.
    }
}
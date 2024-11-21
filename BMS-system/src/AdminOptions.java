import java.util.Scanner;

public class AdminOptions {
    private Scanner scanner;

    public AdminOptions(Scanner scanner) {
        this.scanner = scanner;
    }

    public void showAdminOptions() {
        boolean loggedIn = true;

        while (loggedIn) {
            System.out.println("----------------------------------------------------------------");
            System.out.println("Admin Options:");
            System.out.println("- [1] View Banquets");
            System.out.println("- [2] View Attendees");
            System.out.println("- [3] Create Banquet");
            System.out.println("- [4] Update Banquet");
            System.out.println("- [5] Add Meal to Banquet");
            System.out.println("- [6] Generate Report");
            System.out.println("- [-1] Logout");
            System.out.print(">>> Please select the above options x in [x]: ");

            int option = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (option) {
                case 1:
                    DB_query.viewBanquets();
                    break;
                case 2:
                    DB_query.viewAttendees();
                    break;
                case 3:
                    createBanquet();
                    break;
                case 4:
                    updateBanquet();
                    break;
                case 5:
                    addMealToBanquet();
                    break;
                case 6:
                    DB_query.generateReport();
                    break;
                case -1:
                    System.out.println("Logging out...");
                    loggedIn = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void createBanquet() {
        System.out.println("Creating a new banquet:");
        System.out.print("Enter Banquet Name: ");
        String banquetName = scanner.nextLine();
        System.out.print("Enter Date and Time (YYYY-MM-DD HH:MM:SS): ");
        String dateTime = scanner.nextLine();
        System.out.print("Enter Address: ");
        String address = scanner.nextLine();
        System.out.print("Enter Location: ");
        String location = scanner.nextLine();
        System.out.print("Enter Contact Staff First Name: ");
        String contactFirstName = scanner.nextLine();
        System.out.print("Enter Contact Staff Last Name: ");
        String contactLastName = scanner.nextLine();
        System.out.print("Is the banquet available? (Y/N): ");
        String available = scanner.nextLine();
        System.out.print("Enter Quota: ");
        int quota = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        boolean created = DB_query.createBanquet(banquetName, dateTime, address, location, contactFirstName, contactLastName, available, quota);
        if (created) {
            System.out.println("Banquet created successfully!");
        } else {
            System.out.println("Failed to create banquet. Please try again.");
        }
    }

    private void updateBanquet() {
        System.out.print("Enter BIN of the banquet to update: ");
        int bin = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.println("Enter new details (press Enter to skip):");
        System.out.print("Banquet Name: ");
        String banquetName = scanner.nextLine();
        System.out.print("Date and Time (YYYY-MM-DD HH:MM:SS): ");
        String dateTime = scanner.nextLine();
        System.out.print("Address: ");
        String address = scanner.nextLine();
        System.out.print("Location: ");
        String location = scanner.nextLine();
        System.out.print("Contact Staff First Name: ");
        String contactFirstName = scanner.nextLine();
        System.out.print("Contact Staff Last Name: ");
        String contactLastName = scanner.nextLine();
        System.out.print("Available (Y/N): ");
        String available = scanner.nextLine();
        System.out.print("Quota: ");
        String quotaStr = scanner.nextLine();
        int quota = quotaStr.isEmpty() ? -1 : Integer.parseInt(quotaStr);

        boolean updated = DB_query.updateBanquet(bin, banquetName, dateTime, address, location, contactFirstName, contactLastName, available, quota);
        if (updated) {
            System.out.println("Banquet updated successfully!");
        } else {
            System.out.println("Failed to update banquet. Please try again.");
        }
    }

    private void addMealToBanquet() {
        System.out.print("Enter BIN of the banquet to add a meal: ");
        int bin = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.println("Enter meal details:");
        System.out.print("Meal Type: ");
        String type = scanner.nextLine();
        System.out.print("Dish Name: ");
        String dishName = scanner.nextLine();
        System.out.print("Price: ");
        double price = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        System.out.print("Special Cuisine: ");
        String specialCuisine = scanner.nextLine();

        boolean added = DB_query.addMealToBanquet(bin, type, dishName, price, specialCuisine);
        if (added) {
            System.out.println("Meal added to banquet successfully!");
        } else {
            System.out.println("Failed to add meal to banquet. Please try again.");
        }
    }
}


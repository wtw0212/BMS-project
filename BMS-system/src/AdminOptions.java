import Database.DB_query;

import java.util.List;
import java.util.Scanner;

public class AdminOptions {
    private Scanner scanner;
    private String adminEmail;
    private Banquet.BanquetService banquetService;
    private Attendee.AttendeeService attendeeService;

    public AdminOptions(Scanner scanner, String adminEmail) {
        this.scanner = scanner;
        this.adminEmail = adminEmail;
        this.banquetService = new Banquet.BanquetService();
        this.attendeeService = new Attendee.AttendeeService();
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
            System.out.println("- [7] Get Attendee by Email");
            System.out.println("- [8] Update Attendee");
            System.out.println("- [9] Manage Admin Accounts");
            System.out.println("- [-1] Logout");
            System.out.print(">>> Please select the above options x in [x]: ");

            int option = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (option) {
                case 1:
                    banquetService.viewBanquets();
                    break;
                case 2:
                    Attendee.AttendeeService.viewAttendees();
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
                case 7:
                    getAttendeeByEmail();
                    break;
                case 8:
                    updateAttendee();
                    break;
                case 9:
                    manageAdminAccounts();
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

        Banquet.Banquet newBanquet = new Banquet.Banquet(0, banquetName, dateTime, address, location, contactFirstName, contactLastName, available, quota);
        boolean created = banquetService.createBanquet(newBanquet);
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

        // Fetch the existing banquet data
        Banquet.Banquet existingBanquet = banquetService.getBanquetByBIN(bin);
        if (existingBanquet == null) {
            System.out.println("Banquet not found. Please try again.");
            return;
        }

        // Update only the fields that have been changed
        Banquet.Banquet updatedBanquet = new Banquet.Banquet(
                bin,
                banquetName.isEmpty() ? existingBanquet.getBanquetName() : banquetName,
                dateTime.isEmpty() ? existingBanquet.getDateTime() : dateTime,
                address.isEmpty() ? existingBanquet.getAddress() : address,
                location.isEmpty() ? existingBanquet.getLocation() : location,
                contactFirstName.isEmpty() ? existingBanquet.getContactFirstName() : contactFirstName,
                contactLastName.isEmpty() ? existingBanquet.getContactLastName() : contactLastName,
                available.isEmpty() ? existingBanquet.getAvailable() : available,
                quota == -1 ? existingBanquet.getQuota() : quota
        );

        boolean updated = banquetService.updateBanquet(updatedBanquet);
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
        System.out.print("Meal Type (Main Course, Appetizer, or Dessert): ");
        String type = scanner.nextLine();
        System.out.print("Dish Name: ");
        String dishName = scanner.nextLine();
        System.out.print("Price: ");
        double price = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        System.out.print("Special Food Item (if any, or press Enter to skip): ");
        String specialFood = scanner.nextLine();

        boolean added = Banquet.BanquetService.addMealToBanquet(bin, type, dishName, price, specialFood);
        if (added) {
            System.out.println("Meal added to banquet successfully!");
        } else {
            System.out.println("Failed to add meal to banquet. Please try again.");
        }
    }

    private void getAttendeeByEmail() {
        System.out.print("Enter the email of the attendee: ");
        String email = scanner.nextLine();
        Attendee.Attendee attendee = attendeeService.getAttendeeByEmail(email);
        if (attendee != null) {
            System.out.println("Attendee Details:");
            System.out.println("Email: " + attendee.getEmail());
            System.out.println("First Name: " + attendee.getFirstName());
            System.out.println("Last Name: " + attendee.getLastName());
            System.out.println("Address: " + attendee.getAddress());
            System.out.println("Attendee Type: " + attendee.getAttendeeType());
            System.out.println("Mobile Number: " + attendee.getMobileNumber());
            System.out.println("Affiliated Organization: " + attendee.getAffiliatedOrganization());
        }
    }

    private void updateAttendee() {
        System.out.print("Enter the email of the attendee to update: ");
        String email = scanner.nextLine();

        System.out.println("Enter new details (press Enter to skip):");
        System.out.print("First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("Address: ");
        String address = scanner.nextLine();
        System.out.print("Attendee Type: ");
        String attendeeType = scanner.nextLine();
        System.out.print("Mobile Number: ");
        String mobileNumber = scanner.nextLine();
        System.out.print("Affiliated Organization: ");
        String affiliatedOrganization = scanner.nextLine();

        Attendee.Attendee updatedAttendee = new Attendee.Attendee(email, firstName, lastName, address, attendeeType, null, mobileNumber, affiliatedOrganization);
        boolean updated = attendeeService.updateAttendeeProfile(updatedAttendee);
        if (updated) {
            System.out.println("Attendee information updated successfully!");
        } else {
            System.out.println("Failed to update attendee information. Please try again.");
        }
    }

    private void manageAdminAccounts() {
        System.out.println("Admin Account Management");
        System.out.println("1. Add Admin Account");
        System.out.println("2. Delete Admin Account");
        System.out.print("Enter your choice (1 or 2): ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                addAdminAccount();
                break;
            case 2:
                deleteAdminAccount();
                break;
            default:
                System.out.println("Invalid choice. Returning to main menu.");
        }
    }

    private void addAdminAccount() {
        System.out.println("Adding a new Admin Account");
        System.out.print("Enter admin email: ");
        String email = scanner.nextLine();
        System.out.print("Enter admin password: ");
        String password = scanner.nextLine();
        System.out.print("Enter admin name: ");
        String name = scanner.nextLine();

        boolean added = attendeeService.addAdminAccount(email, password, name);
        if (added) {
            System.out.println("Admin account added successfully.");
        } else {
            System.out.println("Failed to add admin account. The email might already be in use.");
        }
    }

    private void deleteAdminAccount() {
        System.out.println("Deleting an Admin Account");
        System.out.print("Enter admin email to delete: ");
        String email = scanner.nextLine();

        if (email.equals(adminEmail)) {
            System.out.println("You cannot delete your own account.");
            return;
        }

        boolean deleted = attendeeService.deleteAdminAccount(email);
        if (deleted) {
            System.out.println("Admin account deleted successfully.");
        } else {
            System.out.println("Failed to delete admin account. The email might not exist.");
        }
    }
}
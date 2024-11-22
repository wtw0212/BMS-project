import java.util.Scanner;

public class AdminOptions {
    private Scanner scanner;
    private BanquetService banquetService;
    private AttendeeService attendeeService;

    public AdminOptions(Scanner scanner) {
        this.scanner = scanner;
        this.banquetService = new BanquetService();
        this.attendeeService = new AttendeeService();
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
            System.out.println("- [-1] Logout");
            System.out.print(">>> Please select the above options x in [x]: ");

            int option = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (option) {
                case 1:
                    banquetService.viewBanquets();
                    break;
                case 2:
                    AttendeeService.viewAttendees();
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

        Banquet newBanquet = new Banquet(0, banquetName, dateTime, address, location, contactFirstName, contactLastName, available, quota);
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

        Banquet updatedBanquet = new Banquet(bin, banquetName, dateTime, address, location, contactFirstName, contactLastName, available, quota);
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
        System.out.print("Meal Type: ");
        String type = scanner.nextLine();
        System.out.print("Dish Name: ");
        String dishName = scanner.nextLine();
        System.out.print("Price: ");
        double price = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        System.out.print("Special Cuisine: ");
        String specialCuisine = scanner.nextLine();

        boolean added = BanquetService.addMealToBanquet(bin, type, dishName, price, specialCuisine);
        if (added) {
            System.out.println("Meal added to banquet successfully!");
        } else {
            System.out.println("Failed to add meal to banquet. Please try again.");
        }
    }

    private void getAttendeeByEmail() {
        System.out.print("Enter the email of the attendee: ");
        String email = scanner.nextLine();
        Attendee attendee = attendeeService.getAttendeeByEmail(email);
        if (attendee != null) {
            System.out.println("Attendee Details:");
            System.out.println("Email: " + attendee.getEmail());
            System.out.println("First Name: " + attendee.getFirstName());
            System.out.println("Last Name: " + attendee.getLastName());
            System.out.println("Address: " + attendee.getAddress());
            System.out.println("Attendee Type: " + attendee.getAttendeeType());
            System.out.println("Mobile Number: " + attendee.getMobileNumber());
            System.out.println("Affiliated Organization: " + attendee.getAffiliatedOrganization());
        } else {
            System.out.println("No attendee found with the email: " + email);
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

        Attendee updatedAttendee = new Attendee(email, firstName, lastName, address, attendeeType, null, mobileNumber, affiliatedOrganization);
        boolean updated = attendeeService.updateAttendeeProfile(updatedAttendee);
        if (updated) {
            System.out.println("Attendee information updated successfully!");
        } else {
            System.out.println("Failed to update attendee information. Please try again.");
        }
    }
}
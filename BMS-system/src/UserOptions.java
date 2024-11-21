import java.util.Scanner;

public class UserOptions {
    private Scanner scanner;
    private String userEmail;

    public UserOptions(Scanner scanner, String userEmail) {
        this.scanner = scanner;
        this.userEmail = userEmail;
    }

    public void showUserOptions() {
        boolean loggedIn = true;

        while (loggedIn) {
            System.out.println("----------------------------------------------------------------");
            System.out.println("User Options:");
            System.out.println("- [1] View Available Banquets");
            System.out.println("- [2] Register for a Banquet");
            System.out.println("- [3] Search Registered Banquets");
            System.out.println("- [4] Update Profile");
            System.out.println("- [-1] Logout");
            System.out.print(">>> Please select the above options x in [x]: ");

            int option = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (option) {
                case 1:
                    DB_query.viewAvailableBanquets();
                    break;
                case 2:
                    registerForBanquet();
                    break;
                case 3:
                    searchRegisteredBanquets();
                    break;
                case 4:
                    updateProfile();
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

    private void registerForBanquet() {
        System.out.print("Enter BIN of the banquet you want to register for: ");
        int bin = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter your drink choice (tea, coffee, lemon tea): ");
        String drinkChoice = scanner.nextLine();
        System.out.print("Enter your meal choice (MealID): ");
        int mealChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter any remarks (e.g., seating preference): ");
        String remarks = scanner.nextLine();

        boolean registered = DB_query.registerForBanquet(userEmail, bin, drinkChoice, mealChoice, remarks);
        if (registered) {
            System.out.println("Successfully registered for the banquet!");
        } else {
            System.out.println("Failed to register for the banquet. It might be full or you're already registered.");
        }
    }

    private void searchRegisteredBanquets() {
        System.out.println("Search criteria:");
        System.out.print("Enter date (YYYY-MM-DD) or leave blank: ");
        String date = scanner.nextLine();
        System.out.print("Enter part of banquet name or leave blank: ");
        String banquetName = scanner.nextLine();

        DB_query.searchRegisteredBanquets(userEmail, date, banquetName);
    }

    private void updateProfile() {
        System.out.println("Enter new details (press Enter to skip):");

        System.out.print("First Name: ");
        String firstName = scanner.nextLine();

        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();

        System.out.print("Address: ");
        String address = scanner.nextLine();

        System.out.print("Attendee Type: ");
        String attendeeType = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        System.out.print("Mobile Number: ");
        String mobileNumber = scanner.nextLine();

        System.out.print("Affiliated Organization: ");
        String affiliatedOrganization = scanner.nextLine();

        // Call the updated method with first and last names
        boolean updated = DB_query.updateAttendeeProfile(userEmail, firstName, lastName, address, attendeeType, password, mobileNumber, affiliatedOrganization);

        if (updated) {
            System.out.println("Profile updated successfully!");
        } else {
            System.out.println("Failed to update profile. Please try again.");
        }
    }
}


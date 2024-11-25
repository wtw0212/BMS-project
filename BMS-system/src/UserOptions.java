import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class UserOptions {
    private Scanner scanner;
    private String userEmail;
    private Banquet.BanquetService banquetService;
    private Registration.RegistrationService registrationService;
    private Attendee.AttendeeService attendeeService;

    public UserOptions(Scanner scanner, String userEmail) {
        this.scanner = scanner;
        this.userEmail = userEmail;
        this.banquetService = new Banquet.BanquetService();
        this.registrationService = new Registration.RegistrationService();
        this.attendeeService = new Attendee.AttendeeService();
    }

    public void showUserOptions() {
        boolean loggedIn = true;

        while (loggedIn) {
            System.out.println("----------------------------------------------------------------");
            System.out.println("User Options:");
            System.out.println("- [1] View Available Banquets");
            System.out.println("- [2] Register for a Banquet");
            System.out.println("- [3] Search Registered Banquets");
            System.out.println("- [4] View Profile");
            System.out.println("- [5] Update Profile");
            System.out.println("- [-1] Logout");
            System.out.print(">>> Please select the above options x in [x]: ");

            int option = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (option) {
                case 1:
                    viewAvailableBanquets();
                    break;
                case 2:
                    registerForBanquet();
                    break;
                case 3:
                    searchRegisteredBanquets();
                    break;
                case 4:
                    viewProfile();
                    break;
                case 5:
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

    private void viewAvailableBanquets() {
        Banquet.BanquetService.viewAvailableBanquets();
    }

    private void registerForBanquet() {
        System.out.print("Enter BIN of the banquet you want to register for: ");
        int bin = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // Display and select main course
        int mainCourseChoice = selectMealOption(bin, "Main Course");
        if (mainCourseChoice == -1) return;

        // Display and select appetizer
        int appetizerChoice = selectMealOption(bin, "Appetizer");
        if (appetizerChoice == -1) return;

        // Display and select dessert
        int dessertChoice = selectMealOption(bin, "Dessert");
        if (dessertChoice == -1) return;


        System.out.print("Enter any remarks: ");
        String remarks = scanner.nextLine();

        // Combine all choices and remarks
        String combinedRemarks = String.format("Main Course: %d | Appetizer: %d | Dessert: %d | %s",
                mainCourseChoice, appetizerChoice, dessertChoice, remarks);

        boolean registered = registrationService.registerForBanquet(userEmail, bin, mainCourseChoice, appetizerChoice, dessertChoice, combinedRemarks);
        if (registered) {
            System.out.println("Successfully registered for the banquet!");
            System.out.println("Your meal choices:");
            System.out.println("Main Course: " + registrationService.getMealInfo(mainCourseChoice));
            System.out.println("Appetizer: " + registrationService.getMealInfo(appetizerChoice));
            System.out.println("Dessert: " + registrationService.getMealInfo(dessertChoice));
        } else {
            System.out.println("Failed to register for the banquet. It might be full or you're already registered.");
        }
    }


    private int selectMealOption(int bin, String courseType) {
        Map<Integer, String> mealOptions = registrationService.getMealOptionsForBanquet(bin, courseType);
        if (mealOptions.isEmpty()) {
            System.out.println("No " + courseType + " options available for this banquet. Registration canceled.");
            return -1;
        }

        System.out.println("Available " + courseType + " options:");
        for (Map.Entry<Integer, String> entry : mealOptions.entrySet()) {
            System.out.printf("%-5d: %s%n", entry.getKey(), entry.getValue());
        }

        System.out.print("Enter your " + courseType + " choice (MealID): ");
        int mealChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (!mealOptions.containsKey(mealChoice)) {
            System.out.println("Invalid meal choice. Registration canceled.");
            return -1;
        }

        return mealChoice;
    }

    private void searchRegisteredBanquets() {
        System.out.println("Search criteria:");
        System.out.print("Enter date (YYYY-MM-DD) or leave blank: ");
        String date = scanner.nextLine();
        System.out.print("Enter part of banquet name or leave blank: ");
        String banquetName = scanner.nextLine();

        List<Registration.Registration> registrations = registrationService.searchRegisteredBanquets(userEmail, date, banquetName);
        if (registrations.isEmpty()) {
            System.out.println("No registered banquets found for the given criteria.");
        } else {
            System.out.println("\nRegistered Banquets:");
            System.out.println("----------------------------------------------------------------");
            System.out.printf("%-5s | %-30s | %-19s | %-20s | %-15s | %-20s | %-20s | %-20s | %-15s%n",
                    "BIN", "Banquet Name", "Date & Time", "Address", "Location", "Main Course", "Appetizer", "Dessert", "Registration Time");
            System.out.println("----------------------------------------------------------------");
            for (Registration.Registration registration : registrations) {
                System.out.printf("%-5d | %-30s | %-19s | %-20s | %-15s | %-20s | %-20s | %-20s | %-15s%n",
                        registration.getBIN(),
                        registration.getBanquetName(),
                        registration.getDateTime(),
                        registration.getAddress(),
                        registration.getLocation(),
                        registration.getMainCourseName(),
                        registration.getAppetizerName(),
                        registration.getDessertName(),
                        registration.getRegistrationTime());
            }
            System.out.println("----------------------------------------------------------------");
        }
    }

    private void viewProfile() {
        Attendee.Attendee profile = attendeeService.getAttendeeByEmail(userEmail);
        if (profile != null) {
            System.out.println("Your Profile:");
            System.out.println("Email: " + profile.getEmail());
            System.out.println("First Name: " + profile.getFirstName());
            System.out.println("Last Name: " + profile.getLastName());
            System.out.println("Address: " + profile.getAddress());
            System.out.println("Attendee Type: " + profile.getAttendeeType());
            System.out.println("Attendee Type: " + profile.getPassword());
            System.out.println("Mobile Number: " + profile.getMobileNumber());
            System.out.println("Affiliated Organization: " + profile.getAffiliatedOrganization());
        } else {
            System.out.println("Unable to retrieve your profile. Please try again later.");
        }
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

        Attendee.Attendee updatedAttendee = new Attendee.Attendee(userEmail, firstName, lastName, address, attendeeType, password, mobileNumber, affiliatedOrganization);
        boolean updated = attendeeService.updateAttendeeProfile(updatedAttendee);

        if (updated) {
            System.out.println("Profile updated successfully!");
        } else {
            System.out.println("Failed to update profile. Please try again.");
        }
    }

}
import java.util.List;
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
            System.out.println("- [4] Update Profile");
            System.out.println("- [5] Reserve a Seat");
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
                    updateProfile();
                    break;
                case 5:
                    reserveSeat();
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

        System.out.print("Enter your meal choice (MealID): ");
        int mealChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter any remarks (e.g., seating preference): ");
        String remarks = scanner.nextLine();

        System.out.print("Do you want to reserve a seat now? (Y/N): ");
        String reserveSeatChoice = scanner.nextLine().trim().toUpperCase();

        String seatNumber = null;
        if (reserveSeatChoice.equals("Y")) {
            List<String> availableSeats = registrationService.getAvailableSeats(bin);
            if (availableSeats.isEmpty()) {
                System.out.println("No seats available for this banquet. You can reserve a seat later.");
            } else {
                System.out.println("Available seats: " + String.join(", ", availableSeats));
                System.out.print("Enter your preferred seat number (or press Enter to skip): ");
                seatNumber = scanner.nextLine().trim();
                if (seatNumber.isEmpty()) {
                    seatNumber = null;
                    System.out.println("Seat reservation skipped. You can reserve a seat later.");
                } else if (!availableSeats.contains(seatNumber)) {
                    System.out.println("Invalid seat number. Seat reservation skipped. You can reserve a seat later.");
                    seatNumber = null;
                }
            }
        } else {
            System.out.println("Seat reservation skipped. You can reserve a seat later.");
        }

        boolean registered = registrationService.registerForBanquet(userEmail, bin, mealChoice, remarks, seatNumber);
        if (registered) {
            System.out.println("Successfully registered for the banquet!");
            if (seatNumber != null) {
                System.out.println("Seat " + seatNumber + " has been reserved for you.");
            }
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

        List<Registration.Registration> registrations = registrationService.searchRegisteredBanquets(userEmail, date, banquetName);
        if (registrations.isEmpty()) {
            System.out.println("No registered banquets found for the given criteria.");
        } else {
            System.out.println("\nRegistered Banquets:");
            System.out.println("----------------------------------------------------------------");
            System.out.printf("%-5s | %-30s | %-19s | %-20s | %-15s | %-20s | %-15s | %-10s%n",
                    "BIN", "Banquet Name", "Date & Time", "Address", "Location", "Meal", "Registration Time", "Seat");
            System.out.println("----------------------------------------------------------------");
            for (Registration.Registration registration : registrations) {
                Banquet.Banquet banquet = banquetService.getBanquetByBIN(registration.getBIN());
                String mealInfo = registrationService.getMealInfo(registration.getMealChoice());
                System.out.printf("%-5d | %-30s | %-19s | %-20s | %-15s | %-20s | %-15s | %-10s%n",
                        registration.getBIN(),
                        banquet.getBanquetName(),
                        banquet.getDateTime(),
                        banquet.getAddress(),
                        banquet.getLocation(),
                        mealInfo,
                        registration.getRegistrationTime(),
                        registration.getSeatNumber() != null ? registration.getSeatNumber() : "Not reserved");
            }
            System.out.println("----------------------------------------------------------------");
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

    private void reserveSeat() {
        System.out.println("Seat Reservation");
        System.out.print("Enter BIN of the banquet: ");
        int bin = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // Check if the user is registered for this banquet
        if (!registrationService.isUserRegisteredForBanquet(userEmail, bin)) {
            System.out.println("You are not registered for this banquet. Please register first.");
            return;
        }

        // Check if the user already has a seat reserved
        String currentSeat = registrationService.getUserSeatForBanquet(userEmail, bin);
        if (currentSeat != null) {
            System.out.println("You already have seat " + currentSeat + " reserved for this banquet.");
            System.out.print("Do you want to change your seat? (Y/N): ");
            String changeSeat = scanner.nextLine().trim().toUpperCase();
            if (!changeSeat.equals("Y")) {
                return;
            }
        }

        // Get available seats
        List<String> availableSeats = registrationService.getAvailableSeats(bin);
        if (availableSeats.isEmpty()) {
            System.out.println("No seats available for this banquet.");
            return;
        }

        System.out.println("Available seats: " + String.join(", ", availableSeats));
        System.out.print("Enter your preferred seat number: ");
        String seatNumber = scanner.nextLine();

        if (!availableSeats.contains(seatNumber)) {
            System.out.println("Invalid seat number. Please choose from the available seats.");
            return;
        }

        boolean reserved = registrationService.reserveSeat(userEmail, bin, seatNumber);
        if (reserved) {
            System.out.println("Seat " + seatNumber + " has been successfully reserved for you.");
        } else {
            System.out.println("Failed to reserve the seat. Please try again.");
        }
    }
}


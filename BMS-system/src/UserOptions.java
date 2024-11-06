import java.util.Scanner;

public class UserOptions {
    private Scanner scanner;

    public UserOptions(Scanner scanner) {
        this.scanner = scanner;
    }

    public void showUserOptions() {
        boolean loggedIn = true;

        while (loggedIn) {
            System.out.println("----------------------------------------------------------------");
            System.out.println("User Options:");
            System.out.println("- [1] View Available Banquets");
            System.out.println("- [2] Register for a Banquet");
            System.out.println("- [-1] Logout");
            System.out.print(">>> Please select the above options x in [x]: ");

            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    new DB_query().viewBanquets(); // Assuming this method shows available banquets
                    break;
                case 2:
                    // Implement banquet registration logic here
                    System.out.println("Registering for a banquet...");
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
}
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
            System.out.println("- [3] Generate Report");
            System.out.println("- [-1] Logout");
            System.out.print(">>> Please select the above options x in [x]: ");

            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    new DB_query().viewBanquets();
                    break;
                case 2:
                    new DB_query().viewAttendees();
                    break;
                case 3:
                    new DB_query().generateReport();
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
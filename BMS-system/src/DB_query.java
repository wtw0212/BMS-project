import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DB_query {
    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/BMS";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "1qewasdrt";
    private static final Logger LOGGER = Logger.getLogger(DB_query.class.getName());

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
            LOGGER.log(Level.SEVERE, "MySQL JDBC Driver not found.", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
    }

    public static boolean isAdmin(String email, String password) {
        String adminQuery = "SELECT * FROM Admin WHERE Email = ? AND Password = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(adminQuery)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking Admin credentials", e);
            return false;
        }
    }

    public static boolean isAttendee(String email, String password) {
        String attendeeQuery = "SELECT * FROM Attendee WHERE Email = ? AND Password = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(attendeeQuery)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking User credentials", e);
            return false;
        }
    }

    public static boolean registerUser(String firstName, String lastName, String address, String attendeeType,
                                       String email, String password, String mobileNumber,
                                       String affiliatedOrganization) {
        // Updated SQL query to include first and last name
        String query = "INSERT INTO Attendee (F_Name, L_Name, Address, AttendeeType, Email, Password, MobileNumber, AffiliatedOrganization) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, address);
            pstmt.setString(4, attendeeType);
            pstmt.setString(5, email);
            pstmt.setString(6, password);
            pstmt.setString(7, mobileNumber);
            pstmt.setString(8, affiliatedOrganization);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // Return true if registration was successful

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error registering user", e);
            return false; // Return false if there was an error
        }
    }

    public static void viewBanquets() {
        String banquetQuery = "SELECT b.*, b.F_NameOfTheContactStaff, b.L_NameOfTheContactStaff, " +
                "b.Available, " + // Select availability
                "GROUP_CONCAT(CONCAT(m.Type, ': ', m.DishName, ' (', m.SpecialCuisine, ') - $', m.Price) SEPARATOR '; ') AS Meals " +
                "FROM Banquet b " +
                "LEFT JOIN Meal m ON b.BIN = m.BIN " +
                "GROUP BY b.BIN";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(banquetQuery)) {

            System.out.println("-------------------------------------------------------------------------------------------------------------------");
            System.out.println("Banquet ID | Banquet Name | Date & Time | Address | Location | Contact Staff       | Available Seats | Availability |");
            System.out.println("-------------------------------------------------------------------------------------------------------------------");

            while (rs.next()) {
                int banquetId = rs.getInt("BIN");
                String banquetNameResult = rs.getString("BanquetName");
                String banquetDate = rs.getString("DateTime");
                String address = rs.getString("Address");
                String location = rs.getString("Location"); // Retrieve Location
                String contactFirstName = rs.getString("F_NameOfTheContactStaff"); // Retrieve first name
                String contactLastName = rs.getString("L_NameOfTheContactStaff");   // Retrieve last name
                int availableSeats = rs.getInt("Quota");
                String meals = rs.getString("Meals");
                String availability = rs.getString("Available"); // Retrieve availability

                // Combine first and last name for display
                String contactStaffFullName = contactFirstName + " " + contactLastName;

                // Print banquet details
                System.out.printf("%-10d | %-12s | %-19s | %-22s | %-10s | %-20s | %-15d | %-12s%n",
                        banquetId, banquetNameResult, banquetDate, address, location, contactStaffFullName, availableSeats, availability);

                // Print meals on a new line with proper formatting
                System.out.println("Meals: " + meals);
                System.out.println("------"); // Separator for meals
            }

            System.out.println("------------------------------------------------------------------------------------------------------------");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An error occurred while retrieving available banquets.", e);
            System.out.println("An error occurred while retrieving available banquets.");
        }
    }

    public static void viewAttendees() {
        String query = "SELECT a.*, GROUP_CONCAT(b.BanquetName SEPARATOR ', ') AS RegisteredBanquets " +
                "FROM Attendee a " +
                "LEFT JOIN Registration r ON a.Email = r.Email " +
                "LEFT JOIN Banquet b ON r.BIN = b.BIN " +
                "GROUP BY a.Email";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("------------------------------------------------------------------------------------------------------------");
            System.out.println("Email | Name | Attendee Type | Mobile Number | Affiliated Organization | Registered Banquets");
            System.out.println("------------------------------------------------------------------------------------------------------------");

            while (rs.next()) {
                String email = rs.getString("Email");
                String firstName = rs.getString("F_Name"); // Fetch first name
                String lastName = rs.getString("L_Name");   // Fetch last name
                String attendeeType = rs.getString("AttendeeType");
                String mobileNumber = rs.getString("MobileNumber");
                String affiliatedOrg = rs.getString("AffiliatedOrganization");
                String registeredBanquets = rs.getString("RegisteredBanquets");

                // Combine first and last name for display
                String fullName = firstName + " " + lastName;

                System.out.printf("%-25s | %-20s | %-13s | %-13s | %-25s | %s%n",
                        email, fullName, attendeeType, mobileNumber,
                        affiliatedOrg,
                        (registeredBanquets != null ? registeredBanquets : "None"));
            }

            System.out.println("------------------------------------------------------------------------------------------------------------");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An error occurred while retrieving attendees.", e);
            System.out.println("An error occurred while retrieving attendees.");
        }
    }

    public static void generateReport() {
        System.out.println("Generating report...");

        try (Connection conn = getConnection()) {
            // 1. Registration Status Report
            String registrationQuery = "SELECT b.BanquetName, COUNT(r.RegistrationID) as TotalRegistrations, b.Quota, " +
                    "(b.Quota - COUNT(r.RegistrationID)) as RemainingSeats " +
                    "FROM Banquet b LEFT JOIN Registration r ON b.BIN = r.BIN " +
                    "GROUP BY b.BIN";

            // 2. Popular Meals Report
            String popularMealsQuery = "SELECT m.Type, m.DishName, COUNT(r.RegistrationID) as OrderCount " +
                    "FROM Meal m LEFT JOIN Registration r ON m.MealID = r.MealChoice " +
                    "GROUP BY m.MealID ORDER BY OrderCount DESC LIMIT 5";

            // 3. Attendance Behavior Report
            String attendanceBehaviorQuery = "SELECT a.AttendeeType, COUNT(DISTINCT r.Email) as UniqueAttendee, " +
                    "COUNT(r.RegistrationID) as TotalRegistrations " +
                    "FROM Attendee a LEFT JOIN Registration r ON a.Email = r.Email " +
                    "GROUP BY a.AttendeeType";

            // Execute queries and print reports
            System.out.println("\n1. Registration Status Report:");
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(registrationQuery)) {
                System.out.printf("%-30s %-20s %-10s %-15s%n", "Banquet Name", "Total Registrations", "Quota", "Remaining Seats");
                System.out.println("-------------------------------------------------------------------------------------");
                while (rs.next()) {
                    System.out.printf("%-30s %-20d %-10d %-15d%n",
                            rs.getString("BanquetName"),
                            rs.getInt("TotalRegistrations"),
                            rs.getInt("Quota"),
                            rs.getInt("RemainingSeats"));
                }
            }

            System.out.println("\n2. Popular Meals Report:");
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(popularMealsQuery)) {
                System.out.printf("%-15s %-30s %-15s%n", "Meal Type", "Dish Name", "Order Count");
                System.out.println("------------------------------------------------------------");
                while (rs.next()) {
                    System.out.printf("%-15s %-30s %-15d%n",
                            rs.getString("Type"),
                            rs.getString("DishName"),
                            rs.getInt("OrderCount"));
                }
            }

            System.out.println("\n3. Attendance Behavior Report:");
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(attendanceBehaviorQuery)) {
                System.out.printf("%-20s %-20s %-20s%n", "Attendee Type", "Unique Attendee", "Total Registrations");
                System.out.println("--------------------------------------------------------------------");
                while (rs.next()) {
                    System.out.printf("%-20s %-20d %-20d%n",
                            rs.getString("AttendeeType"),
                            rs.getInt("UniqueAttendee"),
                            rs.getInt("TotalRegistrations"));
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An error occurred while generating the report.", e);
            System.out.println("An error occurred while generating the report.");
        }
    }

    public static boolean createBanquet(String banquetName, String dateTime, String address, String location, String contactFirstName, String contactLastName, String available, int quota) {
        String query = "INSERT INTO Banquet (BanquetName, DateTime, Address, Location, F_NameOfTheContactStaff, L_NameOfTheContactStaff, Available, Quota) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, banquetName);
            pstmt.setString(2, dateTime);
            pstmt.setString(3, address);
            pstmt.setString(4, location); // Set location
            pstmt.setString(5, contactFirstName); // Set first name
            pstmt.setString(6, contactLastName);  // Set last name
            pstmt.setString(7, available);
            pstmt.setInt(8, quota);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An error occurred while creating banquet.", e);
            return false;
        }
    }

    public static boolean updateBanquet(int bin, String banquetName, String dateTime, String address, String location,
                                        String contactFirstName, String contactLastName, String available, int quota) {
        StringBuilder queryBuilder = new StringBuilder("UPDATE Banquet SET ");
        boolean needComma = false;

        if (!banquetName.isEmpty()) {
            queryBuilder.append("BanquetName = ?");
            needComma = true;
        }

        if (!dateTime.isEmpty()) {
            if (needComma) queryBuilder.append(", ");
            queryBuilder.append("DateTime = ?");
            needComma = true;
        }

        if (!address.isEmpty()) {
            if (needComma) queryBuilder.append(", ");
            queryBuilder.append("Address = ?");
            needComma = true;
        }

        if (!location.isEmpty()) {
            if (needComma) queryBuilder.append(", ");
            queryBuilder.append("Location = ?");
            needComma = true;
        }

        if (!contactFirstName.isEmpty() || !contactLastName.isEmpty()) {
            if (needComma) queryBuilder.append(", ");
            queryBuilder.append("F_NameOfTheContactStaff = ?, L_NameOfTheContactStaff = ?");
            needComma = true;
        }

        if (!available.isEmpty()) {
            if (needComma) queryBuilder.append(", ");
            queryBuilder.append("Available = ?");
            needComma = true;
        }

        if (quota != -1) {
            if (needComma) queryBuilder.append(", ");
            queryBuilder.append("Quota = ?");
        }

        queryBuilder.append(" WHERE BIN = ?");

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(queryBuilder.toString())) {

            int parameterIndex = 1;

            // Set parameters for banquet details
            if (!banquetName.isEmpty()) pstmt.setString(parameterIndex++, banquetName);
            if (!dateTime.isEmpty()) pstmt.setString(parameterIndex++, dateTime);
            if (!address.isEmpty()) pstmt.setString(parameterIndex++, address);
            if (!location.isEmpty()) pstmt.setString(parameterIndex++, location);

            // Set first and last name for contact staff
            if (!contactFirstName.isEmpty() || !contactLastName.isEmpty()) {
                pstmt.setString(parameterIndex++, contactFirstName);
                pstmt.setString(parameterIndex++, contactLastName);
            }

            if (!available.isEmpty()) pstmt.setString(parameterIndex++, available);
            if (quota != -1) pstmt.setInt(parameterIndex++, quota);

            // Set the BIN parameter for the WHERE clause
            pstmt.setInt(parameterIndex, bin);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // Return true if update was successful
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An error occurred while updating banquet.", e);
            return false; // Return false in case of an error
        }
    }

    public static boolean addMealToBanquet(int bin, String type, String dishName, double price, String specialCuisine) {
        String query = "INSERT INTO Meal (BIN, Type, DishName, Price, SpecialCuisine) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, bin);
            pstmt.setString(2, type);
            pstmt.setString(3, dishName);
            pstmt.setDouble(4, price);
            pstmt.setString(5, specialCuisine);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An error occurred while adding meal to banquet.", e);
            return false;
        }
    }

    public static void viewAvailableBanquets() {
        String query = "SELECT b.*, " +
                "GROUP_CONCAT(CONCAT(m.Type, ': ', m.DishName, ' (', m.SpecialCuisine, ') - $', m.Price) SEPARATOR '; ') AS Meals " +
                "FROM Banquet b " +
                "LEFT JOIN Meal m ON b.BIN = m.BIN " +
                "WHERE b.Available = 'Y' AND b.Quota > 0 " +
                "GROUP BY b.BIN";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("------------------------------------------------------------------------------------------------------");
            System.out.println("Banquet ID | Banquet Name | Date & Time | Address | Location | Contact Staff       | Available Seats |");
            System.out.println("------------------------------------------------------------------------------------------------------");
            System.out.println("Meals|");
            System.out.println("------");

            while (rs.next()) {
                int banquetId = rs.getInt("BIN");
                String banquetName = rs.getString("BanquetName");
                String banquetDate = rs.getString("DateTime");
                String address = rs.getString("Address");
                String location = rs.getString("Location"); // Retrieve Location
                String contactFirstName = rs.getString("F_NameOfTheContactStaff"); // Retrieve first name
                String contactLastName = rs.getString("L_NameOfTheContactStaff");   // Retrieve last name
                int availableSeats = rs.getInt("Quota");
                String meals = rs.getString("Meals");

                // Combine first and last name for display
                String contactStaffFullName = contactFirstName + " " + contactLastName;

                // Print banquet details
                System.out.printf("%-10d | %-12s | %-19s | %-22s | %-10s | %-20s | %-15d %n",
                        banquetId, banquetName, banquetDate, address, location, contactStaffFullName, availableSeats);

                // Print meals on a new line with proper formatting
                System.out.println("Meals: " + meals);
                System.out.println("------"); // Separator for meals
            }


            System.out.println("------------------------------------------------------------------------------------------------------------");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An error occurred while retrieving available banquets.", e);
            System.out.println("An error occurred while retrieving available banquets.");
        }
    }


    public static boolean registerForBanquet(String email, int bin, String drinkChoice, int mealChoice, String remarks) {
        String checkQuotaQuery = "SELECT Quota FROM Banquet WHERE BIN = ? AND Available = 'Y'";
        String registerQuery = "INSERT INTO Registration (Email, BIN, DrinkChoice, MealChoice, Remarks, RegistrationTime) VALUES (?, ?, ?, ?, ?, NOW())";
        String updateQuotaQuery = "UPDATE Banquet SET Quota = Quota - 1 WHERE BIN = ?";

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            // Check if there are available seats
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuotaQuery)) {
                checkStmt.setInt(1, bin);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    int availableSeats = rs.getInt("Quota");
                    if (availableSeats <= 0) {
                        conn.rollback();
                        return false;
                    }
                } else {
                    conn.rollback();
                    return false;
                }
            }

            // Register the attendee
            try (PreparedStatement registerStmt = conn.prepareStatement(registerQuery)) {
                registerStmt.setString(1, email);
                registerStmt.setInt(2, bin);
                registerStmt.setString(3, drinkChoice);
                registerStmt.setInt(4, mealChoice);
                registerStmt.setString(5, remarks);
                registerStmt.executeUpdate();
            }

            // Update the quota
            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuotaQuery)) {
                updateStmt.setInt(1, bin);
                updateStmt.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An error occurred while registering available banquets.", e);
            return false;
        }
    }

    public static void searchRegisteredBanquets(String email, String date, String banquetName) {
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT b.BIN, b.BanquetName, b.DateTime, b.Address, b.Location, r.DrinkChoice, m.Type AS MealType, m.DishName " + // Include Location
                        "FROM Registration r " +
                        "JOIN Banquet b ON r.BIN = b.BIN " +
                        "JOIN Meal m ON r.MealChoice = m.MealID " +
                        "WHERE r.Email = ?");

        if (!date.trim().isEmpty()) { // Use trim to avoid issues with whitespace
            queryBuilder.append(" AND DATE(b.DateTime) = ?");
        }
        if (!banquetName.trim().isEmpty()) { // Use trim to avoid issues with whitespace
            queryBuilder.append(" AND b.BanquetName LIKE ?");
        }

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(queryBuilder.toString())) {

            pstmt.setString(1, email);
            int paramIndex = 2;

            if (!date.trim().isEmpty()) {
                pstmt.setString(paramIndex++, date);
            }
            if (!banquetName.trim().isEmpty()) {
                pstmt.setString(paramIndex, "%" + banquetName + "%");
            }

            ResultSet rs = pstmt.executeQuery();

            System.out.println("------------------------------------------------------------------------------------------------------------");
            System.out.println("Banquet ID | Banquet Name | Date & Time | Address | Location | Drink Choice | Meal Type | Dish Name");
            System.out.println("------------------------------------------------------------------------------------------------------------");

            boolean hasResults = false; // Flag to check if any results were returned
            while (rs.next()) {
                hasResults = true; // Set flag to true if at least one result is found
                System.out.printf("%-10d | %-12s | %-19s | %-22s | %-10s | %-12s | %-9s | %s%n",
                        rs.getInt("BIN"),
                        rs.getString("BanquetName"),
                        rs.getString("DateTime"),
                        rs.getString("Address"),
                        rs.getString("Location"), // Display location
                        rs.getString("DrinkChoice"),
                        rs.getString("MealType"),
                        rs.getString("DishName"));
            }

            if (!hasResults) {
                System.out.println("No registered banquets found for the given criteria.");
            }

            System.out.println("------------------------------------------------------------------------------------------------------------");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An error occurred while searching for registered banquets.", e);
            System.out.println("An error occurred while searching for registered banquets.");
        }
    }

    public static boolean updateAttendeeProfile(String email, String firstName, String lastName,
                                                String address, String attendeeType,
                                                String password, String mobileNumber,
                                                String affiliatedOrganization) {

        StringBuilder queryBuilder = new StringBuilder("UPDATE Attendee SET ");
        boolean needComma = false;

        // Update first name and last name
        if (!firstName.isEmpty() || !lastName.isEmpty()) {
            queryBuilder.append("F_Name = ?, L_Name = ?");
            needComma = true;
        }

        // Other fields
        if (!address.isEmpty()) {
            if (needComma) queryBuilder.append(", ");
            queryBuilder.append("Address = ?");
            needComma = true;
        }

        if (!attendeeType.isEmpty()) {
            if (needComma) queryBuilder.append(", ");
            queryBuilder.append("AttendeeType = ?");
            needComma = true;
        }

        if (!password.isEmpty()) {
            if (needComma) queryBuilder.append(", ");
            queryBuilder.append("Password = ?");
            needComma = true;
        }

        if (!mobileNumber.isEmpty()) {
            if (needComma) queryBuilder.append(", ");
            queryBuilder.append("MobileNumber = ?");
            needComma = true;
        }

        if (!affiliatedOrganization.isEmpty()) {
            if (needComma) queryBuilder.append(", ");
            queryBuilder.append("AffiliatedOrganization = ?");
        }

        // Add WHERE clause
        queryBuilder.append(" WHERE Email = ?");

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(queryBuilder.toString())) {

            int parameterIndex = 1;

            // Set first and last name parameters
            if (!firstName.isEmpty() || !lastName.isEmpty()) {
                pstmt.setString(parameterIndex++, firstName);
                pstmt.setString(parameterIndex++, lastName);
            }

            // Set other parameters
            if (!address.isEmpty()) pstmt.setString(parameterIndex++, address);
            if (!attendeeType.isEmpty()) pstmt.setString(parameterIndex++, attendeeType);
            if (!password.isEmpty()) pstmt.setString(parameterIndex++, password);
            if (!mobileNumber.isEmpty()) pstmt.setString(parameterIndex++, mobileNumber);
            if (!affiliatedOrganization.isEmpty()) pstmt.setString(parameterIndex++, affiliatedOrganization);

            // Set email for WHERE clause
            pstmt.setString(parameterIndex, email);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // Return true if update was successful

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An error occurred while updating profile.", e);
            return false; // Return false in case of an error
        }
    }
}


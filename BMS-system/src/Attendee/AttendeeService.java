package Attendee;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import Database.DB_query;

public class AttendeeService {
    private static final Logger LOGGER = Logger.getLogger(AttendeeService.class.getName());

    public boolean registerUser(Attendee attendee) {
        String query = "INSERT INTO Attendee (F_Name, L_Name, Address, AttendeeType, Email, Password, MobileNumber, AffiliatedOrganization) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DB_query.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, attendee.getFirstName());
            pstmt.setString(2, attendee.getLastName());
            pstmt.setString(3, attendee.getAddress());
            pstmt.setString(4, attendee.getAttendeeType());
            pstmt.setString(5, attendee.getEmail());
            pstmt.setString(6, attendee.getPassword());
            pstmt.setString(7, attendee.getMobileNumber());
            pstmt.setString(8, attendee.getAffiliatedOrganization());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error registering user", e);
            return false;
        }
    }

    public static void viewAttendees() {
        String query = "SELECT a.*, GROUP_CONCAT(b.BanquetName SEPARATOR ', ') AS RegisteredBanquets " +
                "FROM Attendee a " +
                "LEFT JOIN Registration r ON a.Email = r.Email " +
                "LEFT JOIN Banquet b ON r.BIN = b.BIN " +
                "GROUP BY a.Email";

        try (Connection conn = DB_query.getConnection();
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

    public boolean updateAttendeeProfile(Attendee attendee) {
        StringBuilder queryBuilder = new StringBuilder("UPDATE Attendee SET ");
        boolean needComma = false;

        if (attendee.getFirstName() != null && !attendee.getFirstName().isEmpty()) {
            queryBuilder.append("F_Name = ?");
            needComma = true;
        }
        if (attendee.getLastName() != null && !attendee.getLastName().isEmpty()) {
            if (needComma) queryBuilder.append(", ");
            queryBuilder.append("L_Name = ?");
            needComma = true;
        }
        if (attendee.getAddress() != null && !attendee.getAddress().isEmpty()) {
            if (needComma) queryBuilder.append(", ");
            queryBuilder.append("Address = ?");
            needComma = true;
        }
        if (attendee.getAttendeeType() != null && !attendee.getAttendeeType().isEmpty()) {
            if (needComma) queryBuilder.append(", ");
            queryBuilder.append("AttendeeType = ?");
            needComma = true;
        }
        if (attendee.getPassword() != null && !attendee.getPassword().isEmpty()) {
            if (needComma) queryBuilder.append(", ");
            queryBuilder.append("Password = ?");
            needComma = true;
        }
        if (attendee.getMobileNumber() != null && !attendee.getMobileNumber().isEmpty()) {
            if (needComma) queryBuilder.append(", ");
            queryBuilder.append("MobileNumber = ?");
            needComma = true;
        }
        if (attendee.getAffiliatedOrganization() != null && !attendee.getAffiliatedOrganization().isEmpty()) {
            if (needComma) queryBuilder.append(", ");
            queryBuilder.append("AffiliatedOrganization = ?");
        }

        queryBuilder.append(" WHERE Email = ?");

        try (Connection conn = DB_query.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(queryBuilder.toString())) {

            int parameterIndex = 1;

            if (attendee.getFirstName() != null && !attendee.getFirstName().isEmpty()) {
                pstmt.setString(parameterIndex++, attendee.getFirstName());
            }
            if (attendee.getLastName() != null && !attendee.getLastName().isEmpty()) {
                pstmt.setString(parameterIndex++, attendee.getLastName());
            }
            if (attendee.getAddress() != null && !attendee.getAddress().isEmpty()) {
                pstmt.setString(parameterIndex++, attendee.getAddress());
            }
            if (attendee.getAttendeeType() != null && !attendee.getAttendeeType().isEmpty()) {
                pstmt.setString(parameterIndex++, attendee.getAttendeeType());
            }
            if (attendee.getPassword() != null && !attendee.getPassword().isEmpty()) {
                pstmt.setString(parameterIndex++, attendee.getPassword());
            }
            if (attendee.getMobileNumber() != null && !attendee.getMobileNumber().isEmpty()) {
                pstmt.setString(parameterIndex++, attendee.getMobileNumber());
            }
            if (attendee.getAffiliatedOrganization() != null && !attendee.getAffiliatedOrganization().isEmpty()) {
                pstmt.setString(parameterIndex++, attendee.getAffiliatedOrganization());
            }

            pstmt.setString(parameterIndex, attendee.getEmail());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An error occurred while updating profile.", e);
            return false;
        }
    }

    public Attendee getAttendeeByEmail(String email) {
        String query = "SELECT * FROM Attendee WHERE Email = ?";
        try (Connection conn = DB_query.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Attendee(
                        rs.getString("Email"),
                        rs.getString("F_Name"),
                        rs.getString("L_Name"),
                        rs.getString("Address"),
                        rs.getString("AttendeeType"),
                        rs.getString("Password"),
                        rs.getString("MobileNumber"),
                        rs.getString("AffiliatedOrganization")
                );
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An error occurred while retrieving attendee information.", e);
        }
        return null;
    }

    public boolean isAdmin(String email, String password) {
        String adminQuery = "SELECT * FROM Admin WHERE Email = ? AND Password = ?";
        try (Connection conn = DB_query.getConnection();
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

    public boolean isAttendee(String email, String password) {
        String attendeeQuery = "SELECT * FROM Attendee WHERE Email = ? AND Password = ?";
        try (Connection conn = DB_query.getConnection();
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

    public boolean addAdminAccount(String email, String password, String name) {
        String query = "INSERT INTO Admin (Email, Password, Name) VALUES (?, ?, ?)";

        try (Connection conn = DB_query.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
            pstmt.setString(2, password);
            pstmt.setString(3, name);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding admin account", e);
            return false;
        }
    }

    public boolean deleteAdminAccount(String email) {
        String query = "DELETE FROM Admin WHERE Email = ?";

        try (Connection conn = DB_query.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);

            // Check if the admin account exists before deleting
            if (!isAdminAccountExists(email)) {
                return false;
            }

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting admin account", e);
            return false;
        }
    }

    private boolean isAdminAccountExists(String email) {
        String query = "SELECT COUNT(*) FROM Admin WHERE Email = ?";

        try (Connection conn = DB_query.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking admin account existence", e);
        }
        return false;
    }

}
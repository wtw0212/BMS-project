package Registration;

import Database.DB_query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegistrationService {
    private static final Logger LOGGER = Logger.getLogger(RegistrationService.class.getName());

        public boolean registerForBanquet(String email, int bin, int mainCourseChoice, int appetizerChoice, int dessertChoice, String remarks) {
            String query = "INSERT INTO Registration (Email, BIN, MainCourseChoice, AppetizerChoice, DessertChoice, Remarks) VALUES (?, ?, ?, ?, ?, ?)";

            try (Connection conn = DB_query.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setString(1, email);
                pstmt.setInt(2, bin);
                pstmt.setInt(3, mainCourseChoice);
                pstmt.setInt(4, appetizerChoice);
                pstmt.setInt(5, dessertChoice);
                pstmt.setString(6, remarks);

                int affectedRows = pstmt.executeUpdate();
                return affectedRows > 0;
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error registering for banquet", e);
                return false;
            }
        }

        public List<Registration> searchRegisteredBanquets(String email, String date, String banquetName) {
            List<Registration> registrations = new ArrayList<>();
            String query = "SELECT r.RegistrationID, r.Email, r.BIN, r.MainCourseChoice, r.AppetizerChoice, r.DessertChoice, r.Remarks, r.RegistrationTime, " +
                    "b.BanquetName, b.DateTime, b.Address, b.Location, " +
                    "m1.Type AS MainCourseType, m1.DishName AS MainCourseDishName, " +
                    "m2.Type AS AppetizerType, m2.DishName AS AppetizerDishName, " +
                    "m3.Type AS DessertType, m3.DishName AS DessertDishName " +
                    "FROM Registration r " +
                    "JOIN Banquet b ON r.BIN = b.BIN " +
                    "JOIN Meal m1 ON r.MainCourseChoice = m1.MealID " +
                    "JOIN Meal m2 ON r.AppetizerChoice = m2.MealID " +
                    "JOIN Meal m3 ON r.DessertChoice = m3.MealID " +
                    "WHERE r.Email = ? ";

            if (date != null && !date.isEmpty()) {
                query += "AND DATE(b.DateTime) = ? ";
            }
            if (banquetName != null && !banquetName.isEmpty()) {
                query += "AND b.BanquetName LIKE ? ";
            }

            try (Connection conn = DB_query.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setString(1, email);
                int paramIndex = 2;
                if (date != null && !date.isEmpty()) {
                    pstmt.setString(paramIndex++, date);
                }
                if (banquetName != null && !banquetName.isEmpty()) {
                    pstmt.setString(paramIndex, "%" + banquetName + "%");
                }

                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Registration registration = new Registration(
                                rs.getInt("RegistrationID"),
                                rs.getString("Email"),
                                rs.getInt("BIN"),
                                rs.getInt("MainCourseChoice"),
                                rs.getInt("AppetizerChoice"),
                                rs.getInt("DessertChoice"),
                                rs.getString("Remarks"),
                                rs.getString("RegistrationTime")
                        );
                        registration.setMainCourseName(rs.getString("MainCourseType") + " - " + rs.getString("MainCourseDishName"));
                        registration.setAppetizerName(rs.getString("AppetizerType") + " - " + rs.getString("AppetizerDishName"));
                        registration.setDessertName(rs.getString("DessertType") + " - " + rs.getString("DessertDishName"));
                        registration.setBanquetName(rs.getString("BanquetName"));
                        registration.setDateTime(rs.getString("DateTime"));
                        registration.setAddress(rs.getString("Address"));
                        registration.setLocation(rs.getString("Location"));
                        registrations.add(registration);
                    }
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error searching registered banquets", e);
            }
            return registrations;
        }


        public boolean isUserRegisteredForBanquet(String email, int bin) {
            String query = "SELECT COUNT(*) FROM Registration WHERE Email = ? AND BIN = ?";
            try (Connection conn = DB_query.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setString(1, email);
                pstmt.setInt(2, bin);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1) > 0;
                    }
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error checking user registration", e);
            }
            return false;
        }

        public String getMealInfo(int mealChoice) {
            String query = "SELECT Type, DishName FROM Meal WHERE MealID = ?";
            try (Connection conn = DB_query.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setInt(1, mealChoice);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("Type") + " - " + rs.getString("DishName");
                    }
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error getting meal info", e);
            }
            return "Unknown";
        }

        public Map<Integer, String> getMealOptionsForBanquet(int bin, String courseType) {
            Map<Integer, String> mealOptions = new HashMap<>();
            String query = "SELECT MealID, Type, DishName, Price, SpecialCuisine FROM Meal WHERE BIN = ? AND Type = ?";

            try (Connection conn = DB_query.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setInt(1, bin);
                pstmt.setString(2, courseType);

                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        int mealId = rs.getInt("MealID");
                        String type = rs.getString("Type");
                        String dishName = rs.getString("DishName");
                        double price = rs.getDouble("Price");
                        String specialCuisine = rs.getString("SpecialCuisine");

                        String mealInfo = String.format("%s - %s (Price: $%.2f)", type, dishName, price);
                        if (specialCuisine != null && !specialCuisine.isEmpty()) {
                            mealInfo += " [" + specialCuisine + "]";
                        }

                        mealOptions.put(mealId, mealInfo);
                    }
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error getting meal options for banquet", e);
            }
            return mealOptions;
        }
    }

package sms;

import java.sql.*;
import java.util.Vector;

public class DBHandler {

    private static String login = "root";
    private static String password = "";
    private static String databaseUrl = "jdbc:mysql://localhost:3306/studentsdb";

    private static final String facultiesTable = "faculties";

    /**
     * Load DB settings (supports Docker/Kubernetes env)
     */
    static {
        String envUrl = System.getenv("DB_URL");
        String envUser = System.getenv("DB_USER");
        String envPass = System.getenv("DB_PASSWORD");

        if (envUrl != null && envUser != null && envPass != null) {
            databaseUrl = envUrl;
            login = envUser;
            password = envPass;
            System.out.println("DBHandler: Using ENV database settings");
        } else {
            System.out.println("DBHandler: Using LOCAL database settings");
        }
    }

    // ----------------------------
    // Setters for Login/Password/URL
    // ----------------------------

    public static void setLogin(String loginValue) {
        login = loginValue;
    }

    public static void setPassword(String passValue) {
        password = passValue;
    }

    public static void setDatabaseUrl(String url) {
        databaseUrl = url;
    }

    public static String getFacultiesTable() {
        return facultiesTable;
    }


    // ------------------------------------------------
    //  CREATE TABLES (only faculties)
    // ------------------------------------------------
    public static boolean createTables() {
        try (Connection connection = DriverManager.getConnection(databaseUrl, login, password);
             Statement statement = connection.createStatement()) {

            // Create faculties table if not exists
            statement.executeUpdate(
                "CREATE TABLE IF NOT EXISTS " + facultiesTable +
                " (ID INTEGER NOT NULL AUTO_INCREMENT, " +
                " Name VARCHAR(100), " +
                " PRIMARY KEY(ID))"
            );

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // ------------------------------------------------
    //  ADD FACULTY
    // ------------------------------------------------
    public static boolean addFaculty(String facultyName) {
        try (Connection connection = DriverManager.getConnection(databaseUrl, login, password);
             PreparedStatement ps = connection.prepareStatement(
                     "INSERT INTO " + facultiesTable + " (Name) VALUES (?)")) {

            ps.setString(1, facultyName);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error adding faculty: " + e.getMessage());
            return false;
        }
    }


    // ------------------------------------------------
    //  DELETE FACULTY
    // ------------------------------------------------
    public static boolean deleteFaculty(String facultyName) {
        try (Connection connection = DriverManager.getConnection(databaseUrl, login, password);
             PreparedStatement ps = connection.prepareStatement(
                     "DELETE FROM " + facultiesTable + " WHERE Name = ?")) {

            ps.setString(1, facultyName);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error deleting faculty: " + e.getMessage());
            return false;
        }
    }


    // ------------------------------------------------
    //  CHECK IF FACULTY EXISTS
    // ------------------------------------------------
    public static boolean checkIfElementExists(String tableName, String name) {
        try (Connection connection = DriverManager.getConnection(databaseUrl, login, password);
             PreparedStatement ps = connection.prepareStatement(
                     "SELECT Name FROM " + tableName + " WHERE Name = ?")) {

            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // true if found

        } catch (SQLException e) {
            System.out.println("Error checking element: " + e.getMessage());
            return false;
        }
    }


    // ------------------------------------------------
    //  GET ALL FACULTIES
    // ------------------------------------------------
    public static String[] getFaculties() {

        Vector<String> faculties = new Vector<>();

        try (Connection connection = DriverManager.getConnection(databaseUrl, login, password);
             PreparedStatement ps = connection.prepareStatement(
                     "SELECT Name FROM " + facultiesTable);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                faculties.add(rs.getString("Name"));
            }

        } catch (SQLException e) {
            System.out.println("Error loading faculties: " + e.getMessage());
        }

        return faculties.toArray(new String[0]);
    }
}

package org.example.api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseHelper {
    private static final String DB_URL = "jdbc:postgresql://dpg-d3qvhjndiees73am0fig-a.oregon-postgres.render.com/dbinnopolis";
    private static final String DB_USER = "dbinnopolis_user";
    private static final String DB_PASSWORD = "qsN9l1XpkfpSpDqeRJCFcIsBu95b2y6Y";

    private Connection connection;

    public DatabaseHelper() {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to database", e);
        }
    }

    public void cleanupEmployeeData() {
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM employee")) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to cleanup employee data", e);
        }
    }

    public Integer createEmployeeInDb(String name, String surname, String position, String city) {
        String sql = "INSERT INTO employee (name, surname, position, city) VALUES (John,Brown,Admin,NY) RETURNING id";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, surname);
            stmt.setString(3, position);
            stmt.setString(4, city);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create employee in DB", e);
        }
    }

    public boolean employeeExistsInDb(Integer id) {
        String sql = "SELECT COUNT(*) FROM employee WHERE id = 2";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check employee existence", e);
        }
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            // Ignore
        }
    }
}

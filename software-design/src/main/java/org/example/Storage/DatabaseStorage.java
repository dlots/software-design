package org.example.Storage;

import java.sql.*;

public class DatabaseStorage implements Storage, AutoCloseable {
    private final Connection connection;

    public DatabaseStorage() throws SQLException {
        String jdbcUrl = "jdbc:sqlite:example.db";
        this.connection = DriverManager.getConnection(jdbcUrl);
        setUpDatabase();
    }

    private void setUpDatabase() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS storage (" + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "value TEXT NOT NULL)");
        }
    }

    @Override
    public void save(String value) {
        String query = "INSERT INTO storage (value) VALUES (?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, value);
            preparedStatement.executeUpdate();

            // Get the generated ID
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    System.out.printf("Saved with index %d%n", generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Failed to retrieve generated ID.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving value to database", e);
        }
    }

    @Override
    public String retrieve(int id) {
        String query = "SELECT value FROM storage WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("value");
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving value from database", e);
        }
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error closing database connection", e);
        }
    }
}

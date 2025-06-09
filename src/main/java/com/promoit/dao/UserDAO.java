
package com.promoit.dao;

import com.promoit.model.User;
import com.promoit.util.DBConnection;

import java.sql.*;

public class UserDAO {
    public boolean existsAdmin() throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE role = 'ADMIN'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            rs.next();
            return rs.getInt(1) > 0;
        }
    }

    public void insert(User user) throws SQLException {
        String sql = "INSERT INTO users (username, password_hash, role) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPasswordHash());
            stmt.setString(3, user.getRole());
            stmt.executeUpdate();
        }
    }

    public User findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password_hash"),
                            rs.getString("role")
                    );
                }
            }
        }
        return null;
    }

    // Additional methods: listUsers, deleteUser, etc.
}


package com.promoit.dao;

import com.promoit.model.OTPCode;
import com.promoit.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OTPCodeDAO {
    public void insert(OTPCode otpCode) throws SQLException {
        String sql = "INSERT INTO otp_codes (operation_id, code, status, created_at, expires_at, user_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, otpCode.getOperationId());
            stmt.setString(2, otpCode.getCode());
            stmt.setString(3, otpCode.getStatus());
            stmt.setTimestamp(4, Timestamp.valueOf(otpCode.getCreatedAt()));
            stmt.setTimestamp(5, Timestamp.valueOf(otpCode.getExpiresAt()));
            stmt.setInt(6, otpCode.getUserId());
            stmt.executeUpdate();
        }
    }

    public OTPCode findActiveByOperation(String operationId) throws SQLException {
        String sql = "SELECT * FROM otp_codes WHERE operation_id = ? AND status = 'ACTIVE'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, operationId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new OTPCode(
                            rs.getInt("id"),
                            rs.getString("operation_id"),
                            rs.getString("code"),
                            rs.getString("status"),
                            rs.getTimestamp("created_at").toLocalDateTime(),
                            rs.getTimestamp("expires_at").toLocalDateTime(),
                            rs.getInt("user_id")
                    );
                }
            }
        }
        return null;
    }

    public void updateStatus(int id, String status) throws SQLException {
        String sql = "UPDATE otp_codes SET status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }

    // Additional methods: expire old codes, list by user, etc.
}

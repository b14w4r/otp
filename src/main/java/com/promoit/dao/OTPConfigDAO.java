
package com.promoit.dao;

import com.promoit.model.OTPConfig;
import com.promoit.util.DBConnection;

import java.sql.*;

public class OTPConfigDAO {
    public OTPConfig getConfig() throws SQLException {
        String sql = "SELECT * FROM otp_config LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return new OTPConfig(
                        rs.getInt("id"),
                        rs.getInt("code_length"),
                        rs.getInt("ttl_seconds")
                );
            }
        }
        return null;
    }

    public void updateConfig(OTPConfig config) throws SQLException {
        String sql = "UPDATE otp_config SET code_length = ?, ttl_seconds = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, config.getCodeLength());
            stmt.setInt(2, config.getTtlSeconds());
            stmt.setInt(3, config.getId());
            stmt.executeUpdate();
        }
    }
}

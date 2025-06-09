
package com.promoit.model;

import java.time.LocalDateTime;

public class OTPCode {
    private int id;
    private String operationId;
    private String code;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private int userId;

    public OTPCode() {}

    public OTPCode(int id, String operationId, String code, String status, LocalDateTime createdAt, LocalDateTime expiresAt, int userId) {
        this.id = id;
        this.operationId = operationId;
        this.code = code;
        this.status = status;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.userId = userId;
    }

    // getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getOperationId() { return operationId; }
    public void setOperationId(String operationId) { this.operationId = operationId; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
}

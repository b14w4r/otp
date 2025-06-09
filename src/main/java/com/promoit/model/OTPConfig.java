
package com.promoit.model;

public class OTPConfig {
    private int id;
    private int codeLength;
    private int ttlSeconds;

    public OTPConfig() {}

    public OTPConfig(int id, int codeLength, int ttlSeconds) {
        this.id = id;
        this.codeLength = codeLength;
        this.ttlSeconds = ttlSeconds;
    }

    // getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getCodeLength() { return codeLength; }
    public void setCodeLength(int codeLength) { this.codeLength = codeLength; }
    public int getTtlSeconds() { return ttlSeconds; }
    public void setTtlSeconds(int ttlSeconds) { this.ttlSeconds = ttlSeconds; }
}

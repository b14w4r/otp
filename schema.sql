
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(100) NOT NULL,
    role VARCHAR(10) NOT NULL
);

CREATE TABLE otp_config (
    id SERIAL PRIMARY KEY,
    code_length INT NOT NULL,
    ttl_seconds INT NOT NULL
);

-- Insert default config
INSERT INTO otp_config (code_length, ttl_seconds) VALUES (6, 300);

CREATE TABLE otp_codes (
    id SERIAL PRIMARY KEY,
    operation_id VARCHAR(100) NOT NULL,
    code VARCHAR(20) NOT NULL,
    status VARCHAR(10) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    user_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

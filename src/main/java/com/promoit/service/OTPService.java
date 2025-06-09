
package com.promoit.service;

import com.promoit.dao.OTPConfigDAO;
import com.promoit.dao.OTPCodeDAO;
import com.promoit.model.OTPCode;
import com.promoit.model.OTPConfig;

import java.time.LocalDateTime;
import java.util.Random;

public class OTPService {
    private OTPConfigDAO configDAO = new OTPConfigDAO();
    private OTPCodeDAO codeDAO = new OTPCodeDAO();

    public OTPConfig getConfig() throws Exception {
        return configDAO.getConfig();
    }

    public String generateCode(int userId, String operationId) throws Exception {
        OTPConfig config = getConfig();
        int length = config.getCodeLength();
        int ttl = config.getTtlSeconds();
        String code = generateRandomNumeric(length);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime exp = now.plusSeconds(ttl);
        OTPCode otpCode = new OTPCode(0, operationId, code, "ACTIVE", now, exp, userId);
        codeDAO.insert(otpCode);
        return code;
    }

    public boolean validateCode(String operationId, String code) throws Exception {
        OTPCode otp = codeDAO.findActiveByOperation(operationId);
        if (otp != null && otp.getCode().equals(code)) {
            if (otp.getExpiresAt().isAfter(LocalDateTime.now())) {
                codeDAO.updateStatus(otp.getId(), "USED");
                return true;
            } else {
                codeDAO.updateStatus(otp.getId(), "EXPIRED");
            }
        }
        return false;
    }

    private String generateRandomNumeric(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}

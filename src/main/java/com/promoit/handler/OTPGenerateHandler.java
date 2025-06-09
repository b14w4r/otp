
package com.promoit.handler;

import com.promoit.service.OTPService;
import com.promoit.service.NotificationService;
import com.promoit.util.JWTUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class OTPGenerateHandler implements HttpHandler {
    private OTPService otpService = new OTPService();
    private NotificationService notificationService = new NotificationService();

    @Override
    public void handle(HttpExchange exchange) {
        try {
            if ("POST".equals(exchange.getRequestMethod())) {
                String auth = exchange.getRequestHeaders().getFirst("Authorization");
                String token = auth.split(" ")[1];
                if (!JWTUtil.validateToken(token)) {
                    exchange.sendResponseHeaders(401, -1);
                    return;
                }
                String username = JWTUtil.extractUsername(token);
                JSONObject body = new JSONObject(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8)
                        .lines().collect(Collectors.joining("\n")));
                String operationId = body.getString("operationId");
                String channel = body.getString("channel");
                String destination = body.getString("destination");

                // For simplicity, userId is username hashCode
                int userId = username.hashCode();
                String code = otpService.generateCode(userId, operationId);

                switch (channel.toLowerCase()) {
                    case "email":
                        notificationService.sendEmail(destination, code);
                        break;
                    case "sms":
                        notificationService.sendSms(destination, code);
                        break;
                    case "telegram":
                        notificationService.sendTelegram("Ваш код: " + code);
                        break;
                    case "file":
                        notificationService.saveToFile(code);
                        break;
                    default:
                        break;
                }

                String response = "OTP sent";
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

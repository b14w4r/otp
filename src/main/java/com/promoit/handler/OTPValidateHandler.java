
package com.promoit.handler;

import com.promoit.service.OTPService;
import com.promoit.util.JWTUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class OTPValidateHandler implements HttpHandler {
    private OTPService otpService = new OTPService();

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

                JSONObject body = new JSONObject(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8)
                        .lines().collect(Collectors.joining("\n")));
                String operationId = body.getString("operationId");
                String code = body.getString("code");

                boolean valid = otpService.validateCode(operationId, code);
                String response = valid ? "Valid" : "Invalid or expired";
                exchange.sendResponseHeaders(valid ? 200 : 400, response.getBytes().length);
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

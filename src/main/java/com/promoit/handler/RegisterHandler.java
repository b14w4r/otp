
package com.promoit.handler;

import com.promoit.model.User;
import com.promoit.service.UserService;
import com.promoit.util.JWTUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class RegisterHandler implements HttpHandler {
    private UserService userService = new UserService();

    @Override
    public void handle(HttpExchange exchange) {
        try {
            if ("POST".equals(exchange.getRequestMethod())) {
                String body = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8).lines()
                        .collect(Collectors.joining("\n"));
                JSONObject json = new JSONObject(body);
                String username = json.getString("username");
                String password = json.getString("password");
                String role = json.getString("role");

                userService.register(username, password, role.toUpperCase());

                String response = "User registered";
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        } catch (Exception e) {
            try {
                String response = "Error: " + e.getMessage();
                exchange.sendResponseHeaders(400, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}

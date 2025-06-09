
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

public class LoginHandler implements HttpHandler {
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

                User user = userService.authenticate(username, password);
                if (user != null) {
                    String token = JWTUtil.generateToken(user.getUsername(), user.getRole());
                    JSONObject resp = new JSONObject();
                    resp.put("token", token);
                    byte[] response = resp.toString().getBytes();
                    exchange.sendResponseHeaders(200, response.length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(response);
                    os.close();
                } else {
                    exchange.sendResponseHeaders(401, -1);
                }
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

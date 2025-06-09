
package com.promoit;

import com.promoit.handler.*;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        server.createContext("/api/register", new RegisterHandler());
        server.createContext("/api/login", new LoginHandler());
        server.createContext("/api/otp/generate", new OTPGenerateHandler());
        server.createContext("/api/otp/validate", new OTPValidateHandler());
        // TODO: Add admin handlers

        server.setExecutor(null);
        System.out.println("Server started on port 8000");
        server.start();
    }
}

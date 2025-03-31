package com.example.proxy_server.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RequestProcessor {
    public static String process(String request) throws IOException {
        // Parse the HTTP request
        String[] lines = request.split("\r\n");
        String[] requestLine = lines[0].split(" ");
        String method = requestLine[0];
        String urlStr = requestLine[1];

        // Forward the request to the actual destination
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);

        // Get the response
        StringBuilder response = new StringBuilder();
        response.append("HTTP/1.1 ").append(connection.getResponseCode()).append(" ")
                .append(connection.getResponseMessage()).append("\r\n");

        // Add headers
        connection.getHeaderFields().forEach((key, value) -> {
            if (key != null) {
                response.append(key).append(": ").append(String.join(", ", value)).append("\r\n");
            }
        });
        response.append("\r\n");

        // Add body
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line).append("\n");
            }
        }

        return response.toString();
    }
}
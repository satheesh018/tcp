package com.example.proxy_server.socket;
import java.io.*;
import java.net.*;

public class TcpConnectionHandler implements Runnable {
    private Socket socket;

    public TcpConnectionHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Received request: " + line);
                // Simulate processing the HTTP request and sending a response
                String response = "HTTP/1.1 200 OK\n\nResponse from Offshore Proxy Server";
                writer.println(response);
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


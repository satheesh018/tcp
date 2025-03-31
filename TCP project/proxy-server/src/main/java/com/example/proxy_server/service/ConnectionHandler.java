package com.example.proxy_server.service;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionHandler extends Thread {
    private final Socket shipConnection;

    public ConnectionHandler(Socket shipConnection) {
        this.shipConnection = shipConnection;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(shipConnection.getInputStream()));
            PrintWriter out = new PrintWriter(shipConnection.getOutputStream(), true);

            while (true) {
                // Read request from ship proxy
                StringBuilder requestBuilder = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null && !line.isEmpty()) {
                    requestBuilder.append(line).append("\r\n");
                }
                String request = requestBuilder.toString();

                // Process the request
                String response = RequestProcessor.process(request);

                // Send response back to ship proxy
                out.println(response);
                out.println("END_OF_RESPONSE");
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
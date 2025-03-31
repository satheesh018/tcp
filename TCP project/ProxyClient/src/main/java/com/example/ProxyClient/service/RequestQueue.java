package com.example.ProxyClient.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.concurrent.LinkedBlockingQueue;
import java.net.Socket;

public class RequestQueue {
    private final LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();
    private final Socket offshoreConnection;
    private final Object lock = new Object();

    private final Logger logger = LoggerFactory.getLogger(RequestQueue.class);

    public RequestQueue(Socket offshoreConnection) {
        this.offshoreConnection = offshoreConnection;
    }

    public String processRequest(String request) {
        synchronized (lock) {
            try {
                // Add request to queue
                queue.put(request);

                // Send request to offshore server
                PrintWriter out = new PrintWriter(offshoreConnection.getOutputStream(), true);
                out.println(request);

                // Wait for response
                BufferedReader in = new BufferedReader(new InputStreamReader(offshoreConnection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    if (line.equals("END_OF_RESPONSE")) {
                        break;
                    }
                    response.append(line).append("\n");
                }

                return response.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return "HTTP/1.1 500 Internal Server Error\r\n\r\n";
            }
        }
    }
}
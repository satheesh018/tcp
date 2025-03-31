package com.example.ProxyClient;

import com.example.ProxyClient.service.ProxyHandler;
import com.example.ProxyClient.service.RequestQueue;
import org.springframework.boot.SpringApplication;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProxyClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProxyClientApplication.class, args);
        startProxyServer();
    }

    private static void startProxyServer() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            try (ServerSocket serverSocket = new ServerSocket(8080)) {
                System.out.println("Proxy client started on port 8080");

                // Connect to offshore proxy server
                Socket offshoreConnection = new Socket("proxy-server", 9090);
                System.out.println("Connected to offshore proxy server");

                RequestQueue requestQueue = new RequestQueue(offshoreConnection);

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    new ProxyHandler(clientSocket, requestQueue).start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

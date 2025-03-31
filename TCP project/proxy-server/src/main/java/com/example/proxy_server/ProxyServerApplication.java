package com.example.proxy_server;

import com.example.proxy_server.service.ConnectionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class ProxyServerApplication {

	public static Logger logger = LoggerFactory.getLogger(ProxyServerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ProxyServerApplication.class, args);
		startProxyServer();
	}

	private static void startProxyServer() {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.submit(() -> {
			try (ServerSocket serverSocket = new ServerSocket(9090)) {
				logger.info("Proxy server started on port 9090");

				// Accept connection from ship proxy
				Socket shipConnection = serverSocket.accept();
				logger.info("Accepted connection from ship proxy");

				ConnectionHandler connectionHandler = new ConnectionHandler(shipConnection);
				connectionHandler.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}

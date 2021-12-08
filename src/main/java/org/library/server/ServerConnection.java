package org.library.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ServerConnection {

    private ServerSocket server;
    private Integer count = 0;
    private Map<Integer, ServerProcess> process = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(ServerConnection.class);

    public void startServer(){
        try {
            server = new ServerSocket(8080);
            logger.info("Сервер запустился на порту: {}", 8080);

            while (true) {
                Socket socket = server.accept();
                ServerProcess serverProcess = new ServerProcess(count, socket);
                new Thread(serverProcess).start();
                process.put(count, serverProcess);
                count++;
                System.out.println("Клиент  подключился !");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void closeAll(){
        try {

            server.close();
            System.out.println("Сервер остановился !");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

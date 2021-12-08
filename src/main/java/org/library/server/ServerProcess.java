package org.library.server;

import java.io.*;
import java.net.Socket;

public class ServerProcess implements Runnable {

    private Integer id;
    private Socket socket;
    private BufferedReader messageFromServer;
    private BufferedWriter writeMessage;

    public ServerProcess(Integer id, Socket socket) {
        this.socket = socket;
        try {
            messageFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writeMessage = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            System.out.println(messageFromServer.readLine());
        } catch (IOException ex) {

        }
    }
}

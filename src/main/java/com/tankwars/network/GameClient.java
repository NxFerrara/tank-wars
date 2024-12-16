package com.tankwars.network;

import java.io.*;
import java.net.*;

public class GameClient {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public GameClient(String hostAddress, int port) throws IOException {
        socket = new Socket(hostAddress, port);
        System.out.println("Connected to host!");

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    public Socket getSocket(){
        return socket;
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public String receiveMessage() throws IOException {
        return in.readLine();
    }
    public String receiveMessageNonBlocking() throws IOException {
        if (in.ready()) { // Check if data is available
            return in.readLine();
        }
        return null;
    }

    public void close() throws IOException {
        socket.close();
    }
}

package com.tankwars.network;

import java.io.*;
import java.net.*;

public class GameHost {
    private ServerSocket serverSocket;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public GameHost(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Server started. Waiting for connections on port " + port);
    }

    public Socket acceptConnection() throws IOException {
        // Wait for a client to connect
        System.out.println("Waiting for a client to connect...");
        socket = serverSocket.accept(); // Store the socket in the field
        System.out.println("Client connected: " + socket.getInetAddress());
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
        return socket;
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public String receiveMessage() throws IOException {
        return in.readLine();
    }

    public String receiveMessageNonBlocking() throws IOException {
        if (in.ready()) {
            return in.readLine();
        }
        return null;
    }

    public void close() throws IOException {
        if (in != null) in.close();
        if (out != null) out.close();
        if (socket != null) socket.close();
        if (serverSocket != null) serverSocket.close();
    }
}

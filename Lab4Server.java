// COSC 20203
// Name: Micah St.Hill, Harlem Mariscal, Moises Rodriguez
// Date: 04/30/2025
// Email: micah.sthill@tcu.edu, harlem.mariscal@tcu.edu, m.a.rodriguez21@tcu.edu
// Assignment: Lab 4 - Server

import java.io.*;
import java.net.*;
import java.util.*;

public class Lab4Server {
    ServerSocket serverSocket;

    public Lab4Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server created on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connection made");

                Lab5ClientHandler clientHandler = new Lab5ClientHandler(socket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException i) {
            System.out.println("ERROR: " + i.getMessage());
        }
    }

    public static void main(String[] args) {
        new Lab4Server(4000);
    }

    // ========== Inner class ==========
    static class Lab5ClientHandler implements Runnable {
        private Socket socket;
        private BufferedReader reader;
        private BufferedWriter writer;
        private String clientName;

        public static ArrayList<Lab5ClientHandler> ClientHandlers = new ArrayList<>();

        public Lab5ClientHandler(Socket socket) {
            try {
                this.socket = socket;
                this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                this.clientName = reader.readLine(); // Get client name as first message

                ClientHandlers.add(this);
                System.out.println("Client connected: " + clientName);
            } catch (IOException e) {
                closeEverything();
            }
        }

        @Override
        public void run() {
            String incomingMessage;

            while (socket.isConnected()) {
                try {
                    incomingMessage = reader.readLine();
                    if (incomingMessage == null) break;

                    for (Lab5ClientHandler clientHandler : ClientHandlers) {
                        if (!clientHandler.clientName.equals(clientName)) {
                            clientHandler.writer.write(incomingMessage);
                            clientHandler.writer.newLine();
                            clientHandler.writer.flush();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Client " + clientName + " disconnected.");
                    break;
                }
            }

            closeEverything();
        }

        private void closeEverything() {
            try {
                ClientHandlers.remove(this);
                if (reader != null) reader.close();
                if (writer != null) writer.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                System.out.println("Error closing resources for " + clientName);
            }
        }
    }
}

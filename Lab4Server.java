// COSC 20203
// Name: Micah St.Hill, Harlem Mariscal, Moises Rodriguez
// Date: 04/21/2025
// Email: micah.sthill@tcu.edu, harlem.mariscal@tcu.edu
// Assignment: Lab 4 - Server
// i love git

import java.io.*;
import java.net.*;

public class Lab4Server {
    Socket socket;
    ServerSocket serverSocket;
    BufferedReader input;

    public Lab4Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server created on port " + port);

            socket = serverSocket.accept();
            System.out.println("Client connection made");

            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String message;

            while ((message = input.readLine()) != null && !message.equalsIgnoreCase("END")) {
                System.out.println("Received: " + message);
            }

            System.out.println("Closing connection");
            socket.close();
            input.close();
        } catch (IOException i) {
            System.out.println("ERROR: " + i.getMessage());
        }
    }

    public static void main(String[] args) {
        Lab4Server server = new Lab4Server(3000);
    }
}

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Lab5Client {
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private String username;

    public Lab5Client(Socket socket, String username) {
        try {
            this.socket = socket;
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
        } catch (IOException e) {
            closeEverything();
        }
    }

    public void sendMessage() {
        try {
            writer.write(username);
            writer.newLine();
            writer.flush();

            Scanner scnr = new Scanner(System.in);
            while (socket.isConnected()) {
                String messageToSend = scnr.nextLine();
                writer.write(username + ": " + messageToSend);
                writer.newLine();
                writer.flush();
            }
        } catch (IOException e) {
            closeEverything();
        }
    }

    public void listenForMessage() {
        new Thread(() -> {
            String groupChatMessage;

            while (socket.isConnected()) {
                try {
                    groupChatMessage = reader.readLine();
                    if (groupChatMessage != null) {
                        System.out.println(groupChatMessage);
                    }
                } catch (IOException e) {
                    closeEverything();
                    break;
                }
            }
        }).start();
    }

    public void closeEverything() {
        try {
            if (reader != null) reader.close();
            if (writer != null) writer.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner scnr = new Scanner(System.in);
        System.out.println("Enter your username for the group chat:");
        String username = scnr.nextLine();

        Socket socket = new Socket("localhost", 4000);
        Lab5Client client = new Lab5Client(socket, username);
        client.listenForMessage();
        client.sendMessage();
    }
}

package com.example;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Player {
    private final int port;
    private final String name;
    private int messageCounter;

    public Player(String name, int port) {
        this.name = name;
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public void start(Scanner scanner) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println(name + " is listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                processMessage(socket,scanner);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processMessage(Socket socket, Scanner scanner) {
        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {

            String receivedMessage = (String) in.readObject();
            System.out.println(name + " received message: " + receivedMessage);

            System.out.print("Receiver: ");
            String resMsg = scanner.nextLine();
            String responseMessage = resMsg + " " + ++messageCounter;
            out.writeObject(responseMessage);
            System.out.println(name + " sent back message: " + responseMessage);
            
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

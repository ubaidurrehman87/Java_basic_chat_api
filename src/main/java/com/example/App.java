package com.example;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

// Main.java
public class App {
    public static void main(String[] args) throws InterruptedException{
        Player initiator = new Player("Initiator", 12345);
        Player receiver = new Player("Receiver", 12346);
        Scanner scanner = new Scanner(System.in);

        // Read a line of input from the user
        Thread initiatorThread = new Thread(() -> initiator.start(scanner));
        initiatorThread.start();
        Thread receiverThread = new Thread(() -> receiver.start(scanner));
        receiverThread.start();
        Thread.sleep(new Long(500));
        for (int i = 0; i < 10; i++) {
            System.out.print("\nMsg#"+(i+1)+")\nInitiator: ");
            String message = scanner.nextLine();
            String response = sendMessage(receiver, message);
            System.out.println("Initiator received response: " + response);
        }
        scanner.close();
        System.out.print("\nTo Terminate program Press Ctrl+C");
    }

    private static String sendMessage(Player receiver, String message) {
        String response = null;

        try (Socket socket = new Socket("localhost", receiver.getPort());
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(message);
            response = (String) in.readObject();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return response;
    }
}

package client;
import java.io.*;
import java.net.*;

import shared.Message;

class ServerHandler implements Runnable {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public ServerHandler(Socket socket) {
        this.socket = socket;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendMessage(Message message) {
        try {
            System.out.println("Sending" + message);
            out.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        };
    }

    public void parseMessage(Message message) {
        synchronized (ChatClient.class) {
            ChatClient.receiveMessages(message);
        }
    }

    @Override
    public void run() {
        try {
            Message message;
            while ((message = (Message) in.readObject()) != null) {
                parseMessage(message);
            }           
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                System.out.println("Closing");
                ChatClient.forciblyDisconnect("Disconnected from server");
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

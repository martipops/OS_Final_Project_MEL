package server;
import java.io.*;
import java.net.*;

import shared.Message;
import shared.MessageType;

class ClientHandler implements Runnable {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private int id;

    public ClientHandler(int id, Socket socket) {
        this.id = id;
        this.socket = socket;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return this.id;
    }

    public void sendMessage(Message message) {
        try {
            out.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        };
    }

    public void parseMessage(Message message) {
        synchronized (ChatServer.class) {
            if (ChatServer.getTurn() == getId()) {
                ChatServer.broadcastMessage(message);
                ChatServer.nextTurn();
            } else {
                sendMessage(new Message(MessageType.NOTYOURTURN));;
            }
        }
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            sendMessage(new Message("yes"));;
            Message message;
            while ((message = (Message) in.readObject()) != null) {
                parseMessage(message);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
                synchronized(ChatServer.class) {
                    ChatServer.kickPlayer(getId());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

package server;
import java.io.*;
import java.net.*;

import shared.Message;
import shared.PlayerProfile;

class ClientHandler implements Runnable {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private PlayerProfile playerInfo;

    public ClientHandler(int id, Socket socket) {
        playerInfo = new PlayerProfile(id);
        this.socket = socket;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        sendPlayerList();
    }

    public int getId() {
        return playerInfo.getId();
    }

    public void sendPlayerList() {
        synchronized (ChatServer.class) {
            Message listMessage = new Message(ChatServer.getPlayers());
            sendMessage(listMessage);
        }
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
                sendMessage(new Message("Wait your turn"));;
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

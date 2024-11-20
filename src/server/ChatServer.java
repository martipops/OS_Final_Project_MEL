package server;
import java.io.*;
import java.net.*;
import java.util.*;

import shared.Message;
import shared.MessageType;
import shared.PlayerList;

public class ChatServer {
    private static final int PORT = 12345;
    private static HashMap<Integer, ClientHandler> clients = new HashMap<>();
    private static ArrayList<Integer> playerOrder = new ArrayList<>();
    private static PlayerList profileList = new PlayerList();
    private static int nextid = 0;
    private static int turnCounter = 0;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Chat server started...");
            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(nextid, socket);
                clients.put(nextid, clientHandler);
                playerOrder.add(nextid); 
                profileList.playerConnected(nextid);
                new Thread(clientHandler).start();
                nextid++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    synchronized static void broadcastMessage(Message message) {
        System.out.println("Broadcasting:" + message);
        if (message.getType() != MessageType.PLAYERPROFILE) {
            for (ClientHandler client : clients.values()) {
                client.sendMessage(message);
            }
        }
    }

    synchronized static void kickPlayer(int clientId) {
        if (isClientTurn(clientId)) {
            nextTurn();
        }
        clients.remove(clientId);
        playerOrder.remove(Integer.valueOf(clientId));
        profileList.removePlayer(clientId);
        broadcastMessage(new Message(profileList));
    }

    synchronized static void nextTurn() {
        if (!playerOrder.isEmpty()) {
            turnCounter = (turnCounter + 1) % playerOrder.size();
        }
    }

    synchronized static int getTurn() {
        return playerOrder.get(turnCounter); 
    }

    synchronized static boolean isClientTurn(int clientID) {
        return (getTurn() == clientID);
    }

    synchronized static PlayerList getPlayers() {
        return profileList;
    }
}

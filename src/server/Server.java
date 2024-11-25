package server;
import java.io.*;
import java.net.*;
import java.util.*;

import shared.Message;
import shared.MessageType;

public class Server {
    private static final int PORT = 12345;
    private static HashMap<Integer, ClientHandler> clients = new HashMap<>();
    private static ArrayList<Integer> playerOrder = new ArrayList<>();
    private static int nextid = 0;
    private static int turnCounter = 0;


    /**
     * Main method to start the server.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Chat server started...");
            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(nextid, socket);
                clients.put(nextid, clientHandler);
                playerOrder.add(nextid); 
                new Thread(clientHandler).start();
                nextid++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Broadcast a message to all clients.
     * 
     * @param message the message to be broadcasted
     */
    synchronized static void broadcastMessage(Message message) {
        System.out.println("Broadcasting:" + message);
        switch (message.getType()) {
            case MessageType.NOTYOURTURN:
                break;
            default:
                for (ClientHandler client : clients.values()) {
                    client.sendMessage(message);
                }
                break;
        }
    }

    /**
     * Remove the player from the game.
     * 
     * @param clientId the id of the player to be removed
     */
    synchronized static void kickPlayer(int clientId) {
        clients.remove(clientId);
        playerOrder.remove(Integer.valueOf(clientId));

        // If the player being kicked is the current player, move to the next player.
        if (isClientTurn(clientId)) {
            nextTurn();
        }
    }

    /**
     * Move to the next player's turn.
     */
    synchronized static void nextTurn() {
        if (!playerOrder.isEmpty()) {
            turnCounter = (turnCounter + 1) % playerOrder.size();
        }
    }

    /**
     * Get the current player's turn.
     * 
     * @return the current player's turn
     */
    synchronized static int getTurn() {
        return playerOrder.get(turnCounter); 
    }

    /**
     * Check if it is the client's turn.
     * 
     * @param clientID the id of the client
     * @return true if it is the client's turn, false otherwise
     */
    synchronized static boolean isClientTurn(int clientID) {
        return (getTurn() == clientID);
    }

}

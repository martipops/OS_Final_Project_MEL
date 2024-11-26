package server;
import java.io.*;
import java.net.*;
import java.util.*;

import shared.Message;
import shared.MessageType;


/**
 * The Server class is responsible for starting the server and handling the connection between the server and clients.
 * It contains methods to broadcast messages to all clients, remove a player from the game, move to the next player's turn,
 * All methods in this class are static so that they can be accessed without creating an instance of the Server class.
 * All methods in this class are also synchronized so that they can be accessed by multiple threads concurrently.
 * 
 * The synchronized keyword uses an object's intrensic mutex https://www.baeldung.com/java-mutex
 */

public class Server {
    private static final int PORT = 12345;
    private static HashMap<Integer, ClientHandler> clients = new HashMap<>();
    private static int nextAvailableId = 0;
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
                ClientHandler clientHandler = new ClientHandler(nextAvailableId, socket);
                clients.put(nextAvailableId, clientHandler);
                new Thread(clientHandler).start();
                nextAvailableId++;
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
                // Don't broadcast if it's not the client's turn.
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
        // playerOrder.remove(Integer.valueOf(clientId));

        // If the player being kicked is the current player, move to the next player.
        if (isClientTurn(clientId)) {
            nextTurn();
        }
    }

    /**
     * Move to the next player's turn.
     */
    synchronized static void nextTurn() {
        if (!clients.isEmpty()) {
            turnCounter = (turnCounter + 1) % clients.size();
        }
    }

    /**
     * Get the current player's turn.
     * 
     * @return the current player's turn
     */
    synchronized static int getTurn() {
        if (clients.isEmpty()) {
            return 0;
        }
        ClientHandler[] players = clients.values().toArray(new ClientHandler[0]);
        // If the current turn is out of bounds of the player list, move to the next turn.
        if (turnCounter >= players.length) {
            nextTurn();
            return getTurn();
        }
        return players[turnCounter].getId();
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

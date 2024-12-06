package server;
import java.io.*;
import java.net.*;

import shared.Message;
import shared.MessageType;


/**
 * The ClientHandler class is responsible for handling the connection between the server and a client.
 * It implements the Runnable interface so that it can be run in a separate thread,
 * allowing the server to handle multiple clients concurrently.
 */
class ClientHandler implements Runnable {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private int id;

    /**
     * Constructor for the ClientHandler class.
     * 
     * @param id the id of the client
     * @param socket the socket to be used
     */
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

    /**
     * Get the id of the client.
     * 
     * @return the id of the client
     */
    public int getId() {
        return this.id;
    }

    /**
     * Send a message to the client.
     * 
     * @param message
     */
    public void sendMessage(Message message) {
        try {
            out.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        };
    }

    /**
     * Parse the message received from the client.
     * 
     * @param message the message to be parsed
     */
    public void parseMessage(Message message) {

        // Synchronize only the critical section of code within a method.
        synchronized (Server.class) {
            if (Server.getTurn() == getId()) {
                Server.broadcastMessage(message);
                Server.nextTurn();
            } else {
                sendMessage(new Message(MessageType.NOTYOURTURN));
            }
        }
    }

    /**
     * Close the socket resources.
     */
    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Run the client handler. This method is called when the thread is started.
     */
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
                socket.close();
                synchronized(Server.class) {
                    Server.kickPlayer(getId());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

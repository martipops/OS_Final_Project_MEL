package client;
import java.io.*;
import java.net.*;

import shared.Message;


/**
 * The ServerHandler class is responsible for handling the server connection.
 * It implements the Runnable interface so that it can be run in a separate thread.
 */
class ServerHandler implements Runnable {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    /**
     * Constructor for the ServerHandler class.
     * 
     * @param socket the socket to be used
     */
    public ServerHandler(Socket socket) {
        this.socket = socket;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            closeResources();
        }
    }

    /**
     * Send a message to the server.
     * 
     * @param message the message to be sent
     */
    public void sendMessage(Message message) {
        try {
            System.out.println("Sending " + message);
            out.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Close the socket resources.
     */
    private void closeResources() {
        try {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Parse the message received from the server.
     * 
     * @param message the message to be parsed
     */
    public void parseMessage(Message message) {
        synchronized (Client.class) {
            Client.receiveMessages(message);
        }
    }

    /**
     * Run the server handler.
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
            System.out.println("Closing");
            Client.forciblyDisconnect("Disconnected from server");
            closeResources();
        }
    }
}

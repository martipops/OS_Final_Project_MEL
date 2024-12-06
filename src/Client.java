import java.io.*;
import java.net.*;

import javax.swing.JOptionPane;

/**
 * The Client class is responsible for starting the client application.
 * 
 * It contains the main method to start the client, and it is responsible for
 * handling the connection to the server, and starting the GUI.
 */
public class Client {
    public static final String DEFAULT_IP = "localhost";
    public static final int DEFAULT_PORT = 12345;
    
    private static ServerHandler server;
    private static Socket socket;
    private static DrawingApp app;
    private static JoinPage loginPage;

    /**
     * Main method to start the client.
     * It creates the JoinPage and DrawingApp windows.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        loginPage = new JoinPage();
        app = new DrawingApp();
        setGameView(false);
    }

    /**
     * Set the visibility of the login page and the game view.
     * 
     * @param shouldSetGameView whether the game view should be visible
     */
    public static void setGameView(boolean shouldSetGameView) {
        loginPage.setVisible(!shouldSetGameView);
        app.setVisible(shouldSetGameView);
    }

    /**
     * This method is called when the client is forcibly disconnected from the server.
     * 
     * @param errorMessage the error message to be displayed
     */
    public static void forciblyDisconnect(String errorMessage) {
        setGameView(false);
        JOptionPane.showMessageDialog(loginPage, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }


    /**
     * Connect to the server.
     * 
     * @param ip the IP address of the server
     * @param port the port number of the server
     * @return true if the connection was successful, false otherwise
     */
    public static boolean connectToServer(String ip, int port) {
        try {
            socket = new Socket(ip, port);
            server = new ServerHandler(socket);
            new Thread(server).start();
            System.out.println("Chat client started...");
            setGameView(true);
            return true;
        } catch (IOException e) {
            e.getStackTrace();
            return false;
        }
    }

    /**
     * Submit the drawing to the server.
     */
    public static void submit() {
        Message message = new Message(app.getCanvas());
        server.sendMessage(message);
    }

    /**
     * This method is called when the client recieves a message from the server.
     * Called by the ServerHandler class.
     * 
     * @param message the message to be received
     */
    public static void receiveMessages(Message message) {
        System.out.println("Message received:" + message);
        switch (message.getType()) {
            case MessageType.CANVAS:
                app.setCanvas(message.getCanvas());
                break;
            case MessageType.NOTYOURTURN:
                JOptionPane.showMessageDialog(loginPage, "Wait your turn!", "Error", JOptionPane.ERROR_MESSAGE);
                break;
            default:
                break;
        }
    }


}

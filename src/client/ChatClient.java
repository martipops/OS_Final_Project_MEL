package client;

import java.io.*;
import java.net.*;

import javax.swing.JOptionPane;

import client.ui.DrawingApp;
import client.ui.JoinPage;
import shared.Message;
import shared.MessageType;

public class ChatClient {
    public static final String DEFAULT_IP = "localhost";
    public static final int DEFAULT_PORT = 12345;
    private static ServerHandler server;
    private static Socket socket;
    private static DrawingApp app;
    private static JoinPage loginPage;

    public static void main(String[] args) {
        loginPage = new JoinPage();
        app = new DrawingApp();
        setGameView(false);
    }

    static void setGameView(boolean shouldSetGameView) {
        loginPage.setVisible(!shouldSetGameView);
        app.setVisible(shouldSetGameView);
    }

    static void forciblyDisconnect(String errorMessage) {
        setGameView(false);
        JOptionPane.showMessageDialog(loginPage, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }


    public synchronized static boolean connectToServer(String username, String ip, int port) {
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

    public synchronized static void submit() {
        Message message = new Message(app.getCanvas());
        server.sendMessage(message);
    }

    synchronized static void receiveMessages(Message message) {
        System.out.println("Message received:" + message);
        switch (message.getType()) {
            case MessageType.CANVAS:
                app.setCanvas(message.getCanvas());
                break;
            case MessageType.PLAYERLIST:
                app.updatePlayerList(message.getPlayers());
                break;
            default:
                break;
        }
    }


}

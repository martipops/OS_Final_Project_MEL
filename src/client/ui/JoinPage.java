package client.ui;
import javax.swing.*;

import client.ChatClient;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JoinPage extends JFrame {

    private JTextField usernameField;
    private JTextField ipField;
    private JTextField portField;
    private JButton joinButton;

    public JoinPage() {
        setTitle("Join Server");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));

        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Server IP:"));
        ipField = new JTextField(ChatClient.DEFAULT_IP);
        panel.add(ipField);

        panel.add(new JLabel("Port:"));
        portField = new JTextField(ChatClient.DEFAULT_PORT + "");
        panel.add(portField);

        joinButton = new JButton("Join");
        joinButton.addActionListener(new JoinButtonListener());
        panel.add(joinButton);

        add(panel);
    }

    private class JoinButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String ip = ipField.getText();
            int port;
            boolean connected = false;
            try {
                port = Integer.parseInt(portField.getText());
                synchronized (ChatClient.class) {
                    connected = ChatClient.connectToServer(username, ip, port);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(JoinPage.this, "Invalid port number", "Error", JOptionPane.ERROR_MESSAGE);
            }
            if (!connected) {
                JOptionPane.showMessageDialog(JoinPage.this, "Could not connect to server", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JoinPage joinPage = new JoinPage();
            joinPage.setVisible(true);
        });
    }
}

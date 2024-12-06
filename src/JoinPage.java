
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * The JoinPage class represents the window where the user can join a server.
 * This is the first window that the user sees when they run the client.
 */
public class JoinPage extends JFrame {

    private JTextField ipField;
    private JTextField portField;
    private JButton joinButton;

    /**
     * Constructor for the JoinPage class.
     */
    public JoinPage() {
        setTitle("Join Server");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));

        panel.add(new JLabel("Server IP:"));
        ipField = new JTextField(Client.DEFAULT_IP);
        panel.add(ipField);

        panel.add(new JLabel("Port:"));
        portField = new JTextField(Client.DEFAULT_PORT + "");
        panel.add(portField);

        joinButton = new JButton("Join");
        joinButton.addActionListener(new JoinButtonListener());
        panel.add(joinButton);

        add(panel);
    }

    /**
     * The JoinButtonListener class is responsible for handling the join button click event.
     */
    private class JoinButtonListener implements ActionListener {
        
        /**
         * Handle the join button click event.
         * 
         * @param e the action event
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            String ip = ipField.getText();
            int port;
            boolean connected = false;
            // Try to connect to the server
            try {
                port = Integer.parseInt(portField.getText());
                synchronized (Client.class) {
                    connected = Client.connectToServer(ip, port);
                }
            } catch (NumberFormatException ex) {
                // Show an error message if the port number is invalid
                JOptionPane.showMessageDialog(JoinPage.this, "Invalid port number", "Error", JOptionPane.ERROR_MESSAGE);
            }
            if (!connected) {
                // Show an error message if the client could not connect to the server
                JOptionPane.showMessageDialog(JoinPage.this, "Could not connect to server", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}

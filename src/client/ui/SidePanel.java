package client.ui;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import shared.PlayerList;
import shared.PlayerProfile;

import java.awt.*;

public class SidePanel extends JPanel {
    public SidePanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(220, 220, 220)); 
    }

    public void updatePlayerList(PlayerList l) {
        this.removeAll();
        for (PlayerProfile p : l.profiles) {
            JLabel label = new JLabel(p.getName());
            add(label);
            add(Box.createVerticalStrut(10)); 
        }
    }
    
}

package client.ui;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SidePanel extends JPanel {
    public SidePanel(String[] labels) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(220, 220, 220)); // Optional background color for sidebar

        // Add each label from the provided array
        for (String labelText : labels) {
            JLabel label = new JLabel(labelText);
            add(label);
            add(Box.createVerticalStrut(10)); // Spacer between labels
        }
    }
}

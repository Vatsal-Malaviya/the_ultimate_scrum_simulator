package in.ser.the_ultimate_scrum_simulator.UserInterface;

import javax.swing.*;
import java.awt.*;

public class MyPanel extends JPanel {
    public MyPanel() {
        this.setBackground(Color.WHITE);
    }

    public void addTitleToContainer(JPanel container, String header) {
        JLabel title = new JLabel(header);
        title.setForeground(Color.BLACK);
        title.setFont(new Font("Space Mono", Font.PLAIN, 75));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(title);
        container.add(Box.createRigidArea(new Dimension(0, 30)));
    }
}
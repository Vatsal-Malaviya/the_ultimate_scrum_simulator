package in.ser.the_ultimate_scrum_simulator.UserInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MyPanel extends JPanel {
    public MyPanel() {
        this.setBackground(Color.WHITE);
    }

    public void addTitleToContainer(JPanel container, String header) {
        JLabel title = new JLabel(header);
        title.setForeground(Color.BLACK);
        title.setFont(new Font("Space Mono", Font.PLAIN, 75));
        title.setAlignmentX(CENTER_ALIGNMENT);
        container.add(title);
        container.add(Box.createRigidArea(new Dimension(0, 30)));
    }

    public void addRoundedButtonToContainer(JPanel container, String buttonText, ActionListener listener, int align, int gap) {
        RoundedButton button = new RoundedButton(buttonText);
        JPanel buttonPanel = new JPanel(new FlowLayout(align));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(button);
        container.add(Box.createRigidArea(new Dimension(0, gap)));
        if (listener != null) {
            button.addActionListener(listener);
        }
        container.add(buttonPanel);
    }
}
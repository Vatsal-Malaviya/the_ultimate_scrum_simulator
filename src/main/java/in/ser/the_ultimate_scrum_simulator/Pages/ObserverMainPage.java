package in.ser.the_ultimate_scrum_simulator.Pages;

import in.ser.the_ultimate_scrum_simulator.UserInterface.MyPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ObserverMainPage extends MyPanel {

    private JFrame parentFrame;

    public ObserverMainPage(JFrame frame) {
        this.parentFrame = frame;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 80, 15));

        addTitleToContainer(this);
        addButtonToContainer(this, "VIEW ACTIVE USERS");
        addButtonToContainer(this, "EXIT",e -> System.exit(0));
    }

    private void addTitleToContainer(JPanel container) {
        JLabel title = new JLabel("OBSERVER PAGE");
        title.setForeground(Color.BLACK);
        title.setFont(new Font("Space Mono", Font.PLAIN, 75));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(title);
        container.add(Box.createRigidArea(new Dimension(0, 30)));
    }

    private void addButtonToContainer(JPanel container, String buttonText) {
        addButtonToContainer(container, buttonText, null);
    }

    private void addButtonToContainer(JPanel container, String buttonText, ActionListener listener) {
        JButton button = new JButton(buttonText);
        button.setPreferredSize(new Dimension(300, 50));
        button.setMaximumSize(new Dimension(300, 50));
        button.setFont(new Font("Arial", Font.PLAIN, 30));

        if (listener != null) {
            button.addActionListener(listener);
        }
        container.add(Box.createRigidArea(new Dimension(0, 20)));
        container.add(button);
    }
}
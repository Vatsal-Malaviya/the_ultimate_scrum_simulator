package in.ser.the_ultimate_scrum_simulator.Pages;

import in.ser.the_ultimate_scrum_simulator.UserInterface.MyPanel;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;



public class DeleteUser extends MyPanel {
    private final JFrame parentFrame;

    public DeleteUser(JFrame frame) {
        this.parentFrame = frame;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 100, 20));

        addTitleToContainer(this);

        JPanel credentialsPanel = new JPanel();
        credentialsPanel.setLayout(new BoxLayout(credentialsPanel, BoxLayout.Y_AXIS));

        JTextField usernameField = new JTextField(20);
        usernameField.setMaximumSize(usernameField.getPreferredSize());
        JTextField emailField = new JTextField(20);
        emailField.setMaximumSize(emailField.getPreferredSize());

        JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        usernamePanel.setOpaque(false); // Making it transparent
        usernamePanel.add(new JLabel("Username:"));
        usernamePanel.add(usernameField);

        JPanel emailPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        emailPanel.setOpaque(false); // Making it transparent
        emailPanel.add(new JLabel("E-mail ID:"));
        emailPanel.add(emailField);

        credentialsPanel.add(usernamePanel);
        credentialsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        credentialsPanel.add(emailPanel);

        Color borderColor = Color.BLACK; // Change to desired color
        credentialsPanel.setBorder(new LineBorder(borderColor, 3));
        credentialsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        credentialsPanel.setMaximumSize(credentialsPanel.getPreferredSize());
        credentialsPanel.setBackground(Color.white);

        this.add(credentialsPanel);

    }
    private void addTitleToContainer(JPanel container) {
        JLabel title = new JLabel("Delete User");
        title.setForeground(Color.BLACK);
        title.setFont(new Font("Space Mono", Font.PLAIN, 75));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(title);
        container.add(Box.createRigidArea(new Dimension(0, 30)));
    }



}
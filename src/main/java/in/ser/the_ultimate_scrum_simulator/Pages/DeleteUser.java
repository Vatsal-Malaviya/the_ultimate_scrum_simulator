package in.ser.the_ultimate_scrum_simulator.Pages;

import in.ser.the_ultimate_scrum_simulator.UserInterface.MyPanel;
import in.ser.the_ultimate_scrum_simulator.UserInterface.RoundedButton;
import in.ser.the_ultimate_scrum_simulator.DbWrapper;
import in.ser.the_ultimate_scrum_simulator.model.UserDeleteStatus;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;


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

        credentialsPanel.add(usernamePanel);
        addRoundedButtonToContainer(credentialsPanel, "delete",e->{
            int response = JOptionPane.showConfirmDialog(
                    frame,
                    "Please confirm if you would like to continue with removing this user",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE
            );
            if (response == JOptionPane.YES_OPTION) {
                DbWrapper db = new DbWrapper();
                UserDeleteStatus uds= db.deleteUser(usernameField.getText());
                if(uds.equals(UserDeleteStatus.SUCCESS)){
                    JOptionPane.showMessageDialog(frame, "User access has been removed from the system", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
                    parentFrame.getContentPane().removeAll();
                    parentFrame.add(new DeleteUser(parentFrame), BorderLayout.CENTER);
                    parentFrame.revalidate();
                    parentFrame.repaint();
                }

                else{
                    JOptionPane.showMessageDialog(frame, "Username not found in system", "Error", JOptionPane.INFORMATION_MESSAGE);
                    parentFrame.getContentPane().removeAll();
                    parentFrame.add(new DeleteUser(parentFrame), BorderLayout.CENTER);
                    parentFrame.revalidate();
                    parentFrame.repaint();
                }

            }

            else if (response == JOptionPane.NO_OPTION) {
                // User clicked NO
            }
        }, FlowLayout.CENTER, 20);
        addRoundedButtonToContainer(credentialsPanel, "reset",e->{
            parentFrame.getContentPane().removeAll();
            parentFrame.add(new DeleteUser(parentFrame), BorderLayout.CENTER);
            parentFrame.revalidate();
            parentFrame.repaint();
        }, FlowLayout.CENTER, 0);

        credentialsPanel.add(Box.createRigidArea(new Dimension(0, 5)));

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

    private void addRoundedButtonToContainer(JPanel container, String buttonText, ActionListener listener, int align, int gap) {
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
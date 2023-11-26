package in.ser.the_ultimate_scrum_simulator.Pages;

import in.ser.the_ultimate_scrum_simulator.DbWrapper;
import in.ser.the_ultimate_scrum_simulator.UserInterface.MyPanel;
import in.ser.the_ultimate_scrum_simulator.UserInterface.RoundedButton;
import in.ser.the_ultimate_scrum_simulator.model.AuthStatus;
import in.ser.the_ultimate_scrum_simulator.model.UserAuthResult;
import in.ser.the_ultimate_scrum_simulator.model.User;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;


public class LoginPage extends MyPanel {

    private static final int MAX_LOGIN_ATTEMPTS = 3;
    private static final Connection c = null;
    private static final PreparedStatement ps = null;
    private static int consecutiveLoginAttempts = 0;
    private final JFrame parentFrame;

    public LoginPage(JFrame frame) {
        this.parentFrame = frame;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 100, 20));

        addTitleToContainer(this,"Sign-In");

        JPanel credentialsPanel = new JPanel();
        credentialsPanel.setLayout(new BoxLayout(credentialsPanel, BoxLayout.Y_AXIS));

        JTextField usernameField = new JTextField(20);
        usernameField.setMaximumSize(usernameField.getPreferredSize());
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setMaximumSize(passwordField.getPreferredSize());

        JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        usernamePanel.setOpaque(false); // Making it transparent
        usernamePanel.add(new JLabel("Username:"));
        usernamePanel.add(usernameField);

        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        passwordPanel.setOpaque(false); // Making it transparent
        passwordPanel.add(new JLabel("Password:"));
        passwordPanel.add(passwordField);

        credentialsPanel.add(usernamePanel);
        credentialsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        credentialsPanel.add(passwordPanel);

        Color borderColor = Color.BLACK; // Change to desired color
        credentialsPanel.setBorder(new LineBorder(borderColor, 3));


        addRoundedButtonToContainer(credentialsPanel, "login", e -> {
            if (authenticateUser(usernameField.getText(), new String(passwordField.getPassword()))) {
                JOptionPane.showMessageDialog(frame, "Login successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                consecutiveLoginAttempts = 0;
                //show the main page here.

                if(User.accessGroup()==0){
                    frame.getContentPane().removeAll();
                    frame.add(new AdminMainMenu(frame));
                    frame.revalidate();
                    frame.repaint();
                }

                if(User.accessGroup()==1){
                    frame.getContentPane().removeAll();
                    frame.add(new ObserverMainPage(frame));
                    frame.revalidate();
                    frame.repaint();
                }

                if(User.accessGroup()!=0 && User.accessGroup()!=1){
                    frame.getContentPane().removeAll();
                    frame.add(new MainMenu(frame));
                    frame.revalidate();
                    frame.repaint();
                }

            } else {
                consecutiveLoginAttempts++;
                if (consecutiveLoginAttempts >= MAX_LOGIN_ATTEMPTS) {
                    JOptionPane.showMessageDialog(frame, "Too many login attempts. Closing application.", "Error", JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid credentials. Attempts: " + consecutiveLoginAttempts, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }, FlowLayout.CENTER, 20);

        credentialsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        credentialsPanel.setMaximumSize(credentialsPanel.getPreferredSize());
        credentialsPanel.setBackground(Color.white);

        this.add(credentialsPanel);


        addRoundedButtonToContainer(this, "go back", e -> {
            parentFrame.getContentPane().removeAll();
            parentFrame.add(new HomePage(parentFrame), BorderLayout.CENTER);
            parentFrame.revalidate();
            parentFrame.repaint();
        }, FlowLayout.CENTER, 20);
    }

    private static boolean authenticateUser(String username, String password) {


        DbWrapper db = new DbWrapper();

        UserAuthResult ua = db.loginWith(username, password);
        return ua.status() == AuthStatus.SUCCESS;
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
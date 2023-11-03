package in.ser.the_ultimate_scrum_simulator.Pages;


import in.ser.the_ultimate_scrum_simulator.DbWrapper;
import in.ser.the_ultimate_scrum_simulator.model.AuthStatus;
import in.ser.the_ultimate_scrum_simulator.model.UserAuthResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.LineBorder;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class LoginPage {

    private static Connection c = null;
    private static PreparedStatement ps = null;
    private static final int MAX_LOGIN_ATTEMPTS = 3;
    private static int consecutiveLoginAttempts = 0;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Login Page");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(true);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.add(createLoginPage(frame), BorderLayout.CENTER);
            frame.setVisible(true);
        });
    }

    public static JPanel createLoginPage(JFrame frame) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 100, 20));

        JLabel title = new JLabel("Login Validator");
        title.setForeground(Color.BLACK);
        title.setFont(new Font("Space Mono", Font.PLAIN, 50));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField usernameField = new JTextField(20);
        usernameField.setMaximumSize(usernameField.getPreferredSize());
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setMaximumSize(passwordField.getPreferredSize());

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener((ActionEvent e) -> {
            if (authenticateUser(usernameField.getText(), new String(passwordField.getPassword()))) {
                JOptionPane.showMessageDialog(frame, "Login successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                consecutiveLoginAttempts = 0;
                //show the main page here.
                frame.getContentPane().removeAll();
                frame.add(new MainMenu(frame));
                frame.revalidate();
                frame.repaint();
            } else {
                consecutiveLoginAttempts++;
                if (consecutiveLoginAttempts >= MAX_LOGIN_ATTEMPTS) {
                    JOptionPane.showMessageDialog(frame, "Too many login attempts. Closing application.", "Error", JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid credentials. Attempts: " + consecutiveLoginAttempts, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(loginButton);
        addButtonToContainer(panel, "EXIT",e -> System.exit(0));

        return panel;
    }

    private static boolean authenticateUser(String username, String password) {


        DbWrapper db = new DbWrapper();

        UserAuthResult ua = db.loginWith(username, password);
        if(ua.status() == AuthStatus.SUCCESS) {
            return true;
        }

        return false;
    }

    private static void addButtonToContainer(JPanel container, String buttonText, ActionListener listener) {
        JButton button = new JButton(buttonText);
        button.setPreferredSize(new Dimension(300, 50));
        button.setMaximumSize(new Dimension(300, 50));
        button.setFont(new Font("Arial", Font.PLAIN, 30));
        button.setBackground(Color.WHITE);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBorder(new LineBorder(Color.black, 3));

        if (listener != null) {
            button.addActionListener(listener);
        }
        container.add(Box.createRigidArea(new Dimension(0, 20)));
        container.add(button);
    }
}
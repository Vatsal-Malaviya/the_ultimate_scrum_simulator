package in.ser.the_ultimate_scrum_simulator.Pages;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
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

    private static JPanel createLoginPage(JFrame frame) {
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

        return panel;
    }

    private static boolean authenticateUser(String username, String password) {
        // The same database connection and authentication logic from the original App.java will be used here
        // ... (omitted for brevity in this display)

        // This is a stub for now; you would integrate the actual logic here
        return false;
    }
}


package in.ser.the_ultimate_scrum_simulator.Pages;

import in.ser.the_ultimate_scrum_simulator.DbWrapper;
import in.ser.the_ultimate_scrum_simulator.UserInterface.MyPanel;
import in.ser.the_ultimate_scrum_simulator.UserInterface.RoundedButton;
import in.ser.the_ultimate_scrum_simulator.model.UserCreateStatus;

import javax.swing.*;
import java.awt.*;

public class AddUser extends MyPanel {
    private final JFrame parentFrame;
    private JTextField fullNameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> userTypeDropdown;
    private RoundedButton addButton;
    private RoundedButton resetButton;
    private RoundedButton goBackButton;

    public AddUser(JFrame jFrame) {
        this.parentFrame = jFrame;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


        fullNameField = new JTextField(20);
        fullNameField.setMaximumSize(fullNameField.getPreferredSize());
        addInputField(this, "Enter Full Name", fullNameField);


        emailField = new JTextField(20);
        emailField.setMaximumSize(emailField.getPreferredSize());
        addInputField(this, "Enter Email Address", emailField);


        passwordField = new JPasswordField(20);
        passwordField.setMaximumSize(passwordField.getPreferredSize());
        addInputField(this, "Enter Password", passwordField);


        String[] userTypes = {"Admin", "Observer", "Moderator", "Game Master", "Student"};
        userTypeDropdown = new JComboBox<>(userTypes);
        userTypeDropdown.setMaximumSize(userTypeDropdown.getPreferredSize());
        addInputField(this, "User Type", userTypeDropdown);


        addButton = new RoundedButton("Add");
        resetButton = new RoundedButton("Reset");
        goBackButton = new RoundedButton("Go Back");

        addButtonToContainer(this, addButton, FlowLayout.CENTER, 20);
        addButtonToContainer(this, resetButton, FlowLayout.CENTER, 20);
        addButtonToContainer(this, goBackButton, FlowLayout.CENTER, 20);

        // Event Listeners
        addButton.addActionListener(e -> {
            DbWrapper db = new DbWrapper();
            UserCreateStatus usc = db.registerUser(emailField.getText(), new String(passwordField.getPassword()), userTypeDropdown.getSelectedIndex());
            if (usc.equals(UserCreateStatus.SUCCESS)) {
                JOptionPane.showMessageDialog(addButton, fullNameField.getText() + " added to Database");
            } else {
                JOptionPane.showMessageDialog(addButton, "Error: " + usc);
            }
            resetButton.doClick();
        });

        resetButton.addActionListener(e -> {
            fullNameField.setText("");
            emailField.setText("");
            passwordField.setText("");
            userTypeDropdown.setSelectedIndex(0);
        });

        goBackButton.addActionListener(e -> {
            parentFrame.getContentPane().removeAll();
            parentFrame.add(new HomePage(parentFrame), BorderLayout.CENTER);
            parentFrame.revalidate();
            parentFrame.repaint();
        });
    }

    private void addInputField(JPanel container, String label, JComponent field) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.add(new JLabel(label));
        panel.add(field);
        container.add(panel);
    }

    private void addButtonToContainer(JPanel container, JButton button, int align, int gap) {
        JPanel buttonPanel = new JPanel(new FlowLayout(align));
        buttonPanel.add(button);
        container.add(Box.createRigidArea(new Dimension(0, gap)));
        container.add(buttonPanel);
    }
}

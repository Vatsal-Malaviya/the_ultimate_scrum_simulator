package in.ser.the_ultimate_scrum_simulator.Pages;

import in.ser.the_ultimate_scrum_simulator.DbWrapper;
import in.ser.the_ultimate_scrum_simulator.UserInterface.MyFrame;
import in.ser.the_ultimate_scrum_simulator.model.UserCreateStatus;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddUser extends MyFrame {
    private JTextField name;
    private JTextField email;
    private JComboBox access;
    private JPasswordField password;
    private JButton add;
    private JPanel addUserPanel;
    private JButton reset;

    public AddUser() {
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                name.setText("");
                email.setText("");
                password.setText("");
            }
        });
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // add code to save in db
                DbWrapper db = new DbWrapper();
                UserCreateStatus ucs = db.registerUser(email.getText(), new String(password.getPassword()), access.getSelectedIndex());
                if (ucs.equals(UserCreateStatus.SUCCESS)) {
                    JOptionPane.showMessageDialog(add, name.getText() + " added to Database");
                } else {
                    JOptionPane.showMessageDialog(add, "Error " + ucs);
                }
                reset.doClick();
            }
        });
    }

    public static void main(String[] args) {
        AddUser frame = new AddUser();
        frame.setContentPane(frame.addUserPanel);
        frame.setTitle("Add User");
        frame.setVisible(true);
    }
}

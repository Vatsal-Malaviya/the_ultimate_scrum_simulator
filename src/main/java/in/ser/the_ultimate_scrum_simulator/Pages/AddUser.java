package in.ser.the_ultimate_scrum_simulator.Pages;

import in.ser.the_ultimate_scrum_simulator.UserInterface.MyFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddUser extends MyFrame {
    private final JFrame parentFrame;
    private JTextField name;
    private JTextField email;
    private JComboBox access;
    private JPasswordField password;
    private JButton add;
    private JPanel addUserPanel;
    private JButton reset;
    private JButton back;

    public AddUser(JFrame jFrame) {
        this.parentFrame = jFrame;
        this.setContentPane(this.addUserPanel);
        this.setTitle("Add User");
        this.setVisible(true);
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
                JOptionPane.showMessageDialog(add, name.getText() + " added to Database");
                reset.doClick();
            }
        });
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                parentFrame.setVisible(true);
            }
        });


    }
}

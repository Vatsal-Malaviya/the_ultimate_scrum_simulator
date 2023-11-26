package in.ser.the_ultimate_scrum_simulator.Pages;

import in.ser.the_ultimate_scrum_simulator.UserInterface.MyPanel;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class AdminMainMenu extends MyPanel {

    private JFrame parentFrame;

    public AdminMainMenu(JFrame frame) {
        this.parentFrame = frame;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 100, 20));

        addTitleToContainer(this,"ADMIN MAIN MENU");
        addButtonToContainer(this, "REGISTER USER", e -> {
            parentFrame.getContentPane().removeAll();
            parentFrame.add(new AddUser(parentFrame), BorderLayout.CENTER);
            parentFrame.revalidate();
            parentFrame.repaint();
        });
        addButtonToContainer(this, "DELETE USER", e -> {
            parentFrame.getContentPane().removeAll();
            parentFrame.add(new DeleteUser(parentFrame), BorderLayout.CENTER);
            parentFrame.revalidate();
            parentFrame.repaint();
        });
        addButtonToContainer(this, "EXIT", e -> System.exit(0));
    }


    private void addButtonToContainer(JPanel container, String buttonText, ActionListener listener) {
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


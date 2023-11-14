package in.ser.the_ultimate_scrum_simulator.Pages;

import in.ser.the_ultimate_scrum_simulator.UserInterface.MyPanel;
import in.ser.the_ultimate_scrum_simulator.UserInterface.RoundedButton;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class PlayGameViewInstructions extends MyPanel {

    private final JFrame parentFrame;

    public PlayGameViewInstructions(JFrame frame, String role) {
        this.parentFrame = frame;

        this.setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        addTitleToContainer(centerPanel);
        addButtonToContainer(centerPanel, "PLAY");
        addButtonToContainer(centerPanel, "INSTRUCTIONS", e -> {
            parentFrame.getContentPane().removeAll();
            parentFrame.add(new InstructionForStudent(parentFrame, role), BorderLayout.CENTER);
            parentFrame.revalidate();
            parentFrame.repaint();
        });

        this.add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addRoundedButtonToContainer(bottomPanel, "go back", e -> {
            parentFrame.getContentPane().removeAll();
            parentFrame.add(new StudentSelectRole(parentFrame), BorderLayout.CENTER);
            parentFrame.revalidate();
            parentFrame.repaint();
        });

        this.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void addTitleToContainer(JPanel container) {
        JLabel title = new JLabel("Start Game");
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
        button.setBackground(Color.WHITE);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBorder(new LineBorder(Color.black, 3));

        if (listener != null) {
            button.addActionListener(listener);
        }
        container.add(Box.createRigidArea(new Dimension(0, 20)));
        container.add(button);
    }

    private void addRoundedButtonToContainer(JPanel container, String buttonText, ActionListener listener) {
        RoundedButton button = new RoundedButton(buttonText);
        if (listener != null) {
            button.addActionListener(listener);
        }
        container.add(button);
    }
}


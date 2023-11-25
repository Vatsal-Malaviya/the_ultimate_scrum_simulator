package in.ser.the_ultimate_scrum_simulator.Pages;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class StoryBoard extends JPanel {
    private JFrame parentFrame;
    private JPanel backlogPanel, todoPanel, inProgressPanel, completedPanel;

    public StoryBoard(JFrame frame, String role) {
        this.parentFrame = frame;
        this.setLayout(new BorderLayout());

        backlogPanel = createSwimlane("BACKLOG");
        todoPanel = createSwimlane("TO DO");
        inProgressPanel = createSwimlane("IN PROGRESS");
        completedPanel = createSwimlane("COMPLETED");


        JPanel swimlanesPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        swimlanesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        swimlanesPanel.add(backlogPanel);
        swimlanesPanel.add(todoPanel);
        swimlanesPanel.add(inProgressPanel);
        swimlanesPanel.add(completedPanel);
        JLabel titleLabel = new JLabel("SCRUM BOARD", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        this.add(titleLabel, BorderLayout.NORTH);
        this.add(swimlanesPanel, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(createButton("Go Back", e -> openPlayGameViewInstructions(role)));
        buttonPanel.add(createButton("Create Burndown Chart", e -> createBurndownChart()));
        buttonPanel.add(createButton("Create Velocity Chart", e -> createVelocityChart()));
        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    private JButton createButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        if (actionListener != null) {
            button.addActionListener(actionListener);
        }
        return button;
    }

    private void createBurndownChart() {
        BurndownChart burndownChartApp = new BurndownChart();
    }

    private void createVelocityChart() {
        VelocityChart velocityChartApp = new VelocityChart();
        velocityChartApp.setVisible(true);
    }

    public void openPlayGameViewInstructions(String role) {
        parentFrame.getContentPane().removeAll();
        parentFrame.add(new PlayGameViewInstructions(parentFrame, role));
        parentFrame.revalidate();
        parentFrame.repaint();
    }
    private JPanel createSwimlane(String title) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(title));
        return panel;
    }

    public static void main(String[] args) {

        JFrame frame = new JFrame("Kanban Board");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(800, 600));
        //frame.add(new StoryBoard(frame, role));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
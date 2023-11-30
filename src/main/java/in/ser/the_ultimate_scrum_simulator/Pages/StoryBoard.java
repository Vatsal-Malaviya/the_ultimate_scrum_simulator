
package in.ser.the_ultimate_scrum_simulator.Pages;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StoryBoard extends JPanel {
    private JFrame parentFrame;
    private JPanel backlogPanel, todoPanel, inProgressPanel, completedPanel;
    private JPanel selectedTask = null;

    public StoryBoard(JFrame frame, String role) {
        this.parentFrame = frame;
        this.setLayout(new BorderLayout());

        backlogPanel = createSwimlane("BACKLOG", this.getSize());
        todoPanel = createSwimlane("TO DO", null);
        inProgressPanel = createSwimlane("IN PROGRESS", null);
        completedPanel = createSwimlane("COMPLETED", null);

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
        buttonPanel.add(createButton("Create Burndown Chart", e -> openBurndownChart()));
        buttonPanel.add(createButton("Create Velocity Chart", e -> createVelocityChart()));
        JButton forwardButton = createButton("→", e -> moveTaskForward());
        JButton backwardButton = createButton("←", e -> moveTaskBackward());
        buttonPanel.add(backwardButton);
        buttonPanel.add(forwardButton);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createSwimlane(String title, Dimension dimension) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(title));

        if (title.equals("BACKLOG") && dimension != null) {
            JPanel taskBox = createTaskBox();
            panel.add(taskBox);
        }

        return panel;
    }

    private JPanel createTaskBox() {
        JPanel taskBox = new JPanel();
        taskBox.setLayout(new BoxLayout(taskBox, BoxLayout.X_AXIS));
        taskBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        taskBox.add(new JLabel("Sample Task"));
        Dimension taskBoxDimension = new Dimension(400, 200);

        taskBox.setPreferredSize(taskBoxDimension);
        taskBox.setMaximumSize(taskBoxDimension);
        taskBox.setMinimumSize(taskBoxDimension);

        taskBox.setAlignmentY(Component.TOP_ALIGNMENT);
        taskBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (selectedTask != null) {
                    selectedTask.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                }
                selectedTask = taskBox;
                selectedTask.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
            }
        });


        return taskBox;
    }





    private JButton createButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        if (actionListener != null) {
            button.addActionListener(actionListener);
        }
        return button;
    }

    private void moveTaskForward() {
        if (selectedTask != null) {
            Container parent = selectedTask.getParent();
            if (parent.equals(backlogPanel)) {
                moveTask(todoPanel);
            } else if (parent.equals(todoPanel)) {
                moveTask(inProgressPanel);
            } else if (parent.equals(inProgressPanel)) {
                moveTask(completedPanel);
            }
        }
    }

    private void moveTaskBackward() {
        if (selectedTask != null) {
            Container parent = selectedTask.getParent();
            if (parent.equals(todoPanel)) {
                moveTask(backlogPanel);
            } else if (parent.equals(inProgressPanel)) {
                moveTask(todoPanel);
            } else if (parent.equals(completedPanel)) {
                moveTask(inProgressPanel);
            }
        }
    }

    private void moveTask(JPanel targetPanel){
        Container parent = selectedTask.getParent();
        parent.remove(selectedTask);
        targetPanel.add(selectedTask);
        parent.revalidate();
        parent.repaint();
        targetPanel.revalidate();
        targetPanel.repaint();
    }

    private void openPlayGameViewInstructions(String role) {
        parentFrame.getContentPane().removeAll();
        parentFrame.add(new PlayGameViewInstructions(parentFrame, role));
        parentFrame.revalidate();
        parentFrame.repaint();
    }

    private void openBurndownChart() {
        // Burndown chart opening logic
    }

    private void createVelocityChart() {
        // Velocity chart creation logic
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Kanban Board");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(800, 600));
        frame.add(new StoryBoard(frame, "Role"));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}


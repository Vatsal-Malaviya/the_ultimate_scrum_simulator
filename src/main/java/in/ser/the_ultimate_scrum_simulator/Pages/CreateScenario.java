package in.ser.the_ultimate_scrum_simulator.Pages;

import in.ser.the_ultimate_scrum_simulator.UserInterface.MyPanel;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import in.ser.the_ultimate_scrum_simulator.model.User;

public class CreateScenario extends MyPanel {
    private JFrame parentFrame;
    private JTextField titleField;
    private JComboBox<String> difficultyComboBox;  // Difficulty ComboBox

    public CreateScenario(JFrame frame) {
        this.parentFrame = frame;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        addTitleToContainer(this, "CREATE SCENARIO");

        JPanel scenarioPanel = new JPanel();
        scenarioPanel.setLayout(new BoxLayout(scenarioPanel, BoxLayout.Y_AXIS));

        titleField = new JTextField(20);
        titleField.setMaximumSize(titleField.getPreferredSize());

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setOpaque(false);
        titlePanel.add(new JLabel("Scenario Title:"));
        titlePanel.add(titleField);

        JTextField createdBy = new JTextField(20);
        createdBy.setText(User.name());
        createdBy.setEditable(false);

        JPanel createdByField = new JPanel(new FlowLayout(FlowLayout.CENTER));
        createdByField.setOpaque(false);
        createdByField.add(new JLabel("Created By:"));
        createdByField.add(createdBy);

        String[] difficulties = {"Low", "Medium", "Hard"};
        difficultyComboBox = new JComboBox<>(difficulties);
        difficultyComboBox.setMaximumSize(difficultyComboBox.getPreferredSize());

        JPanel difficultyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        difficultyPanel.setOpaque(false);
        difficultyPanel.add(new JLabel("Difficulty:"));
        difficultyPanel.add(difficultyComboBox);

        scenarioPanel.add(titlePanel);
        scenarioPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        scenarioPanel.add(createdByField);
        scenarioPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        scenarioPanel.add(difficultyPanel);

        scenarioPanel.setMaximumSize(scenarioPanel.getPreferredSize());
        scenarioPanel.setBackground(Color.white);
        Border lineBorder = BorderFactory.createLineBorder(Color.BLACK, 2); // Change color and thickness as needed
        scenarioPanel.setBorder(lineBorder);

        addRoundedButtonToContainer(scenarioPanel, "next", e -> {
        }, FlowLayout.CENTER, 20);

        this.add(scenarioPanel);

        addRoundedButtonToContainer(this, "go back", e -> {
            parentFrame.getContentPane().removeAll();
            parentFrame.add(new GameMasterMainMenu(parentFrame), BorderLayout.CENTER);
            parentFrame.revalidate();
            parentFrame.repaint();
        }, FlowLayout.CENTER, 20);
    }
}

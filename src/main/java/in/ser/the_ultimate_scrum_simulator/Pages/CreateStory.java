package in.ser.the_ultimate_scrum_simulator.Pages;

import in.ser.the_ultimate_scrum_simulator.DbWrapper;
import in.ser.the_ultimate_scrum_simulator.UserInterface.MyPanel;
import in.ser.the_ultimate_scrum_simulator.model.CreateScenarioStatus;
import in.ser.the_ultimate_scrum_simulator.model.CreateStoryStatus;
import in.ser.the_ultimate_scrum_simulator.model.User;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CreateStory extends MyPanel {
    private JFrame parentFrame;
    private JComboBox<String> scenarioComboBox;
    private JTextField numberOfStoriesField;
    private JButton generateButton;
    private JPanel dynamicPanel;

    public class Story {
        private String title;
        private String description;
        private int estimate;

        public Story(String title, String description, int estimate) {
            this.title = title;
            this.description = description;
            this.estimate = estimate;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getEstimate() {
            return estimate;
        }

        public void setEstimate(int estimate) {
            this.estimate = estimate;
        }
    }

    public CreateStory(JFrame frame) {
        this.parentFrame = frame;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        addTitleToContainer(this, "CREATE STORY");

        initializeComponents(frame);
        layoutScenarioComponents();
        setupGenerateButtonAction();
    }

    private void initializeComponents(JFrame frame) {
        scenarioComboBox = new JComboBox<>();
        scenarioComboBox.setMaximumSize(scenarioComboBox.getPreferredSize());
        try {
            DbWrapper db = new DbWrapper();
            List<Integer> scenarioIds = db.getScenarioIds();
            for (Integer id : scenarioIds) {
                scenarioComboBox.addItem(id.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "No scenarios found", "Error", JOptionPane.INFORMATION_MESSAGE);
            parentFrame.getContentPane().removeAll();
            parentFrame.add(new GameMasterMainMenu(parentFrame), BorderLayout.CENTER);
            parentFrame.revalidate();
            parentFrame.repaint();
        }
        numberOfStoriesField = new JTextField(10);
        generateButton = new JButton("Generate Stories");
        dynamicPanel = new JPanel();
        dynamicPanel.setLayout(new BoxLayout(dynamicPanel, BoxLayout.Y_AXIS));
    }

    private void layoutScenarioComponents() {
        JPanel storyPanel = new JPanel();
        storyPanel.add(new JLabel("Select Scenario:"));
        storyPanel.add(scenarioComboBox);
        this.add(storyPanel);
        storyPanel.add(new JLabel("Number of Stories:"));
        storyPanel.add(numberOfStoriesField);
        storyPanel.add(generateButton);
        this.add(storyPanel);
        this.add(dynamicPanel);
        addRoundedButtonToContainer(this, "go back", e -> {
            parentFrame.getContentPane().removeAll();
            parentFrame.add(new GameMasterMainMenu(parentFrame), BorderLayout.CENTER);
            parentFrame.revalidate();
            parentFrame.repaint();
        }, FlowLayout.CENTER, 20);
        storyPanel.setBackground(Color.white);
    }
    private void setupGenerateButtonAction() {
        generateButton.addActionListener(e -> {
            dynamicPanel.removeAll();
            List<Story> stories = new ArrayList<>();
            int numStories;
            try {
                numStories = Integer.parseInt(numberOfStoriesField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number");
                return;
            }

            for (int i = 0; i < numStories; i++) {
                JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
                panel.setPreferredSize(new Dimension(400, 150));

                JTextField titleField = new JTextField(20);
                JTextField descriptionField = new JTextField(20);
                JTextField estimateField = new JTextField(20);

                panel.add(new JLabel("Story Title:"));
                panel.add(titleField);
                panel.add(new JLabel("Description:"));
                panel.add(descriptionField);
                panel.add(new JLabel("Estimate:"));
                panel.add(estimateField);


                int result = JOptionPane.showConfirmDialog(null, panel,
                        "Enter Details for Story " + (i + 1), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    String title = titleField.getText();
                    String description = descriptionField.getText();
                    int estimate;
                    try {
                        estimate = Integer.parseInt(estimateField.getText());
                        stories.add(new Story(title, description, estimate));
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Please enter a valid estimate number");
                        i--; // This will repeat the iteration for the same story
                    }
                } else {
                    break;
                }
            }

            StringBuilder reviewBuilder = new StringBuilder();
            reviewBuilder.append("Review of Entered Stories:\n\n");
            for (int i = 0; i < stories.size(); i++) {
                Story story = stories.get(i);
                reviewBuilder.append("Story ").append(i + 1).append(":\n");
                reviewBuilder.append("Title: ").append(story.getTitle()).append("\n");
                reviewBuilder.append("Description: ").append(story.getDescription()).append("\n");
                reviewBuilder.append("Estimate: ").append(story.getEstimate()).append("\n\n");
            }

            JTextArea textArea = new JTextArea(reviewBuilder.toString());
            textArea.setEditable(false);
            textArea.setWrapStyleWord(true);
            textArea.setLineWrap(true);

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 250));

            Object[] options = {"Submit", "Cancel"};

            int result = JOptionPane.showOptionDialog(
                    this,
                    scrollPane,
                    "Please confirm if you would like to submit these stories",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if (result == JOptionPane.YES_OPTION) {
                DbWrapper db = new DbWrapper();
                String owner=User.name();
                if(owner==null){
                    owner="test";
                }
                int scenarioId= scenarioComboBox.getSelectedIndex()+1;
                for (Story story : stories) {
                    CreateStoryStatus css = db.createStory(story.getTitle(),story.getDescription(),owner,story.getEstimate(),scenarioId);
                    if(css.equals(CreateStoryStatus.SUCCESS)){
                        continue;
                    }
                    else{
                        JOptionPane.showMessageDialog(this, "Error Adding Story: "+story.getTitle(), "Invalid", JOptionPane.INFORMATION_MESSAGE);
                    }
                }

                JOptionPane.showMessageDialog(this, "Stories added to scenario successfully. You can add more stories to scenarios or go back to the main menu.", "Task Complete", JOptionPane.INFORMATION_MESSAGE);
            }

            dynamicPanel.revalidate();
            dynamicPanel.repaint();
        });
    }

}

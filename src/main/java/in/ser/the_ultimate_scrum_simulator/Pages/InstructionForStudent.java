package in.ser.the_ultimate_scrum_simulator.Pages;

import in.ser.the_ultimate_scrum_simulator.UserInterface.MyPanel;
import in.ser.the_ultimate_scrum_simulator.UserInterface.RoundedButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class InstructionForStudent extends MyPanel {

    private JFrame parentFrame;

    public InstructionForStudent(JFrame frame) {
        this.parentFrame = frame;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 100, 20));

        addTitleToContainer(this);
        addDescriptionToContainer(this);
    }

    private void addTitleToContainer(MyPanel container) {
        JLabel title = new JLabel("HOW TO PLAY?");
        title.setForeground(Color.BLACK);
        title.setFont(new Font("Space Mono", Font.PLAIN, 75));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(title);
    }

    private void addDescriptionToContainer(MyPanel container) {
        //String role="developer";
        String text="";

        text="Developer Instructions:\n"
                + "1. Implement user stories from the sprint backlog.\n"
                + "2. Ensure code quality and conduct tests.\n"
                + "3. Collaborate with teammates.\n"
                + "4. Participate in daily stand-ups and report progress.\n"
                + "5. Seek clarifications on requirements from the Product Owner.\n";


//            text+="\nScrum Master Instructions:\n"
//                    + "1. Ensure the team adheres to Scrum principles.\n"
//                    + "2. Facilitate Scrum ceremonies.\n"
//                    + "3. Remove impediments faced by the team.\n"
//                    + "4. Protect the team from external interferences.\n"
//                    + "5. Help the team improve continuously.\n";
//
//            text+="\nProduct Owner Instructions:\n"
//                    + "1. Represent the customer's interest.\n"
//                    + "2. Prioritize the product backlog.\n"
//                    + "3. Clarify requirements and acceptance criteria to the team.\n"
//                    + "4. Accept or reject work results at the end of the sprint.\n"
//                    + "5. Provide feedback during the sprint review.";
        // }


        JTextPane description = new JTextPane();
        description.setText(text);
        description.setForeground(Color.BLACK);
        description.setFont(new Font("Space Mono", Font.PLAIN, 25));
        description.setEditable(false);
        description.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.BLACK, 2), new EmptyBorder(25, 50, 180, 50)));
        container.add(description);
        addRoundedButtonToContainer(this, "go back",e->{
            parentFrame.getContentPane().removeAll();
            parentFrame.add(new PlayGameViewInstructions(parentFrame), BorderLayout.CENTER);
            parentFrame.revalidate();
            parentFrame.repaint();
        },FlowLayout.LEFT, 20);
    }



    private void addRoundedButtonToContainer(JPanel container, String buttonText, ActionListener listener, int align, int gap) {
        RoundedButton button = new RoundedButton(buttonText);
        JPanel buttonPanel = new JPanel(new FlowLayout(align));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(button);
        container.add(Box.createRigidArea(new Dimension(0, gap)));
        if (listener != null) {
            button.addActionListener(listener);
        }
        container.add(buttonPanel);
    }
}



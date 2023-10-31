package in.ser.the_ultimate_scrum_simulator.Pages;
import in.ser.the_ultimate_scrum_simulator.UserInterface.MyPanel;
import in.ser.the_ultimate_scrum_simulator.UserInterface.RoundedButton;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;
public class InstructionManual extends MyPanel {

    private JFrame parentFrame;

    public InstructionManual(JFrame frame) {
        this.parentFrame = frame;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 100, 20));

        addTitleToContainer(this);
        addDescriptionToContainer(this);
        addGoBackButtonToContainer(this);
    }

    private void addTitleToContainer(MyPanel container) {
        JLabel title = new JLabel("HOW TO PLAY?");
        title.setForeground(Color.BLACK);
        title.setFont(new Font("Space Mono", Font.PLAIN, 75));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(title);
    }

    private void addDescriptionToContainer(MyPanel container) {
        String role="observer";
        String text="";

        if(role.equals("student")){
            text="As a Student, the user will have the ability to play the scrum simulation game by replicating the role of a Scrum Master, Product Owner or Developer. " +
                    "Based on the persona that is selected by the user, they will be presented with different scenarios and according to their role they have to provide the solution to clear the scenario." +
                    "The purpose of this game is to make the student comfortable and familiar with the scrum process.";
        }

        if(role.equals("gamemaster")){
            text="As a Game Master, the user is responsible for creating scenarios and challenges for different scrum roles (Scrum Master, Product Owner, Developer)." +
                    "The challenges that are created by the Game Master will then be displayed to the students where the students can simulate through" +
                    "the different scenarios and gain an understanding of the scrum process. The Game Master also has the ability to edit and make changes " +
                    "to any scenarios they had previously created. Once the game master submits a scenario, an approval request is sent to the Moderator. Once the moderator " +
                    "approves the scenario, only then will it be visible in-game to the students.";
        }

        if(role.equals("observer")){
            text="As an observer, the user has the ability to monitor and observe the gameplay of students that are playing the scrum simulator, The observer also has " +
                    "the ability to provide comments and hints to a student who is having trouble understanding a scenario.";
        }


        JTextPane description = new JTextPane();
        description.setText(text);
        description.setForeground(Color.BLACK);
        description.setFont(new Font("Space Mono", Font.PLAIN, 25));
        description.setEditable(false);
        description.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.BLACK, 2), new EmptyBorder(25, 50, 180, 50)));
        container.add(description);
    }

    private void addGoBackButtonToContainer(MyPanel container) {
        RoundedButton goBackButton = new RoundedButton("go back");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(goBackButton);
        container.add(Box.createRigidArea(new Dimension(0, 20)));
        container.add(buttonPanel); // Add the JPanel to the main container
    }
}

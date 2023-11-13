package in.ser.the_ultimate_scrum_simulator.Pages;
import in.ser.the_ultimate_scrum_simulator.DbWrapper;
import in.ser.the_ultimate_scrum_simulator.UserInterface.MyPanel;
import in.ser.the_ultimate_scrum_simulator.UserInterface.RoundedButton;
import in.ser.the_ultimate_scrum_simulator.model.AuthStatus;
import in.ser.the_ultimate_scrum_simulator.model.UserAuthResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.border.*;
public class ObserverActivePage extends MyPanel {

    private JFrame parentFrame;

    public ObserverActivePage(JFrame frame) throws SQLException {
        this.parentFrame = frame;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 100, 20));

        addTitleToContainer(this);
        addDescriptionToContainer(this);

    }

    private void addTitleToContainer(MyPanel container) {
        JLabel title = new JLabel("Currently Active Users");
        title.setForeground(Color.BLACK);
        title.setFont(new Font("Space Mono", Font.PLAIN, 75));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(title);
    }

    private void addDescriptionToContainer(MyPanel container) throws SQLException {

        DbWrapper db = new DbWrapper();
        List<String> userList = db.selectAll();

        //System.out.println(userList);

        JTextPane description = new JTextPane();


            description.setText(String.valueOf(userList));
            description.setForeground(Color.BLACK);
            description.setFont(new Font("Space Mono", Font.PLAIN, 25));
            description.setEditable(false);
            description.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.BLACK, 2), new EmptyBorder(25, 50, 180, 50)));
            container.add(description);


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

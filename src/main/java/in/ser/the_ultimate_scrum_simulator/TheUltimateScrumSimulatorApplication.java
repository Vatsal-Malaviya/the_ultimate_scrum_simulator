package in.ser.the_ultimate_scrum_simulator;

import in.ser.the_ultimate_scrum_simulator.Pages.HomePage;
import in.ser.the_ultimate_scrum_simulator.UserInterface.MyFrame;
import in.ser.the_ultimate_scrum_simulator.UserInterface.MyPanel;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;
import java.awt.*;

@SpringBootApplication
public class TheUltimateScrumSimulatorApplication extends MyPanel {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new MyFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.add(new HomePage(frame), BorderLayout.CENTER);
            frame.setVisible(true);
        });
    }
}

package in.ser.the_ultimate_scrum_simulator.UserInterface;

import javax.swing.*;
import java.awt.*;

public class MyFrame extends JFrame {
    public MyFrame() {
        this.setTitle("Scrum Simulator");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(true);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setSize(1000, 800);
        this.setExtendedState(MAXIMIZED_BOTH);
        this.setLayout(new BorderLayout());

    }

}

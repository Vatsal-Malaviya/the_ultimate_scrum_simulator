import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;

public class MainApp extends MyPanel{
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



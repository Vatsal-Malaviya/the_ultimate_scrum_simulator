package in.ser.the_ultimate_scrum_simulator.Pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class BurndownChartPanel extends JPanel {

    private JFrame parentFrame;
    private String role;

    public BurndownChartPanel(JFrame frame, String role) {
        this.setLayout(new BorderLayout());
        this.parentFrame = frame;
        this.role = role;

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(255, 255, 255));
        JLabel titleLabel = new JLabel("BURNDOWN CHART", SwingConstants.CENTER);
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        this.add(titlePanel, BorderLayout.NORTH);

        JPanel chartPanel = new JPanel();
        chartPanel.setLayout(new BorderLayout());
        try {
            Image chartImage = ImageIO.read(new File("/Users/maruti/the_ultimate_scrum_simulator/src/main/resources/placeholders/BurndownChart.png"));
            Image scaledChartImage = chartImage.getScaledInstance(400, 300, Image.SCALE_SMOOTH);
            JLabel chartLabel = new JLabel(new ImageIcon(scaledChartImage));
            chartPanel.add(chartLabel, BorderLayout.CENTER);
        } catch (IOException e) {
            e.printStackTrace();
            JLabel errorLabel = new JLabel("Error loading chart image", SwingConstants.CENTER);
            chartPanel.add(errorLabel, BorderLayout.CENTER);
        }
        this.add(chartPanel, BorderLayout.CENTER);


        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton goBackButton = createButton("Go Back",  e -> goBack());
        bottomPanel.add(goBackButton);
        this.add(bottomPanel, BorderLayout.SOUTH);

    }


    private JButton createButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        if (actionListener != null) {
            button.addActionListener(actionListener);
        }
        return button;
    }

    public void goBack() {
        parentFrame.getContentPane().removeAll();
        parentFrame.add(new StoryBoard(parentFrame, role));
        parentFrame.revalidate();
        parentFrame.repaint();
    }
}

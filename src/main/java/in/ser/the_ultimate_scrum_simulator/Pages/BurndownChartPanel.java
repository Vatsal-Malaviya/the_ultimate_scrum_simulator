package in.ser.the_ultimate_scrum_simulator.Pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

public class BurndownChartPanel extends JPanel {

    private JFrame parentFrame;
    private String role;
    private ArrayList<Integer> efforts;
    private JPanel chartPanel;
    private JTextField inputField; // Field to enter new effort values

    public BurndownChartPanel(JFrame frame, String role) {
        this.setLayout(new BorderLayout());
        this.parentFrame = frame;
        this.role = role;
        this.efforts = new ArrayList<>();

        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(255, 255, 255));
        JLabel titleLabel = new JLabel("BURNDOWN CHART", SwingConstants.CENTER);
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        this.add(titlePanel, BorderLayout.NORTH);

        // Chart Panel
        chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBurndownChart(g);
            }
        };
        this.add(chartPanel, BorderLayout.CENTER);

        // Input Panel
        JPanel inputPanel = new JPanel();
        inputField = new JTextField(10);
        JButton updateButton = new JButton("Update Chart");
        updateButton.addActionListener(e -> updateChart());
        inputPanel.add(new JLabel("Enter Effort:"));
        inputPanel.add(inputField);
        inputPanel.add(updateButton);

        this.add(inputPanel, BorderLayout.NORTH);

        // Bottom Panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton goBackButton = createButton("Go Back", e -> goBack());
        bottomPanel.add(goBackButton);
        this.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void updateChart() {
        try {
            int effort = Integer.parseInt(inputField.getText());
            efforts.add(effort);
            chartPanel.repaint(); // Redraw the chart
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter a number.");
        }
    }

    private void drawBurndownChart(Graphics g) {
        int padding = 50;
        int width = getWidth() - 2 * padding;
        int height = getHeight() - 2 * padding;
        int originX = padding;
        int originY = getHeight() - padding;

        // Draw axes
        g.drawLine(originX, originY, originX + width, originY); // X-axis
        g.drawLine(originX, originY, originX, originY - height); // Y-axis

        // Axes labels
        g.drawString("Days", originX + width / 2, originY + 40);
        g.drawString("Effort", originX - 45, originY - height / 2);

        // Plot the data points and lines
        if (!efforts.isEmpty()) {
            int maxEffort = Collections.max(efforts);
            int stepX = width / efforts.size();
            int stepY = height / maxEffort;

            for (int i = 0; i < efforts.size(); i++) {
                int x = originX + (i * stepX);
                int y = originY - (efforts.get(i) * stepY);

                g.fillOval(x - 2, y - 2, 4, 4); // Data point
                if (i > 0) {
                    int prevX = originX + ((i - 1) * stepX);
                    int prevY = originY - (efforts.get(i - 1) * stepY);
                    g.drawLine(prevX, prevY, x, y); // Line connecting points
                }

                // Label each day on X-axis
                g.drawString(String.valueOf(i + 1), x - 2, originY + 20);
            }
        }
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

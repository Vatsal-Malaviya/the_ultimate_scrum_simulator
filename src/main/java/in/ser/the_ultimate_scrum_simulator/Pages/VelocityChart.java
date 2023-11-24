package in.ser.the_ultimate_scrum_simulator.Pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VelocityChart extends JFrame {

    private int[] velocities;
    private String[] sprintNames;

    public VelocityChart() {
        super("Agile Velocity Chart");

        // Example data for demonstration
        velocities = new int[]{10, 15, 20};
        sprintNames = new String[]{"Sprint 1", "Sprint 2", "Sprint 3"};

        // Create a button to update the chart
        JButton updateButton = new JButton("Update Chart");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateChart();
            }
        });

        // Create a panel to draw the chart
        ChartPanel chartPanel = new ChartPanel();

        // Add components to the frame
        setLayout(new BorderLayout());
        add(chartPanel, BorderLayout.CENTER);
        add(updateButton, BorderLayout.SOUTH);

        // Set default close operation and size of the frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // Center the frame on the screen
    }

    private void updateChart() {
        // You can get input from the user and update the velocities accordingly
        // For simplicity, let's assume the user provides velocities for three sprints

        velocities[0] = Integer.parseInt(JOptionPane.showInputDialog("Enter velocity for Sprint 1:"));
        velocities[1] = Integer.parseInt(JOptionPane.showInputDialog("Enter velocity for Sprint 2:"));
        velocities[2] = Integer.parseInt(JOptionPane.showInputDialog("Enter velocity for Sprint 3:"));

        repaint(); // Redraw the chart with updated data
    }

    private class ChartPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Calculate bar width and spacing
            int barWidth = getWidth() / velocities.length;
            int spacing = 10;

            // Draw x-axis
            g.drawLine(50, getHeight() - 50, getWidth() - 50, getHeight() - 50);

            // Draw y-axis
            g.drawLine(50, 50, 50, getHeight() - 50);

            // Draw y-axis title
            g.drawString("Velocity", 20, getHeight() / 2);

            // Draw x-axis title
            g.drawString("Sprints", getWidth() / 2, getHeight() - 20);

            // Draw bars for each sprint
            for (int i = 0; i < velocities.length; i++) {
                int barHeight = velocities[i] * 5; // Scaling factor for better visualization
                int x = 50 + i * (barWidth + spacing);
                int y = getHeight() - 50 - barHeight;

                // Draw bars
                g.setColor(Color.BLUE);
                g.fillRect(x, y, barWidth, barHeight);

                // Draw x-axis values
                g.setColor(Color.BLACK);
                g.drawString(sprintNames[i], x + barWidth / 2 - 20, getHeight() - 30);

                // Draw y-axis values
                g.drawString(Integer.toString(velocities[i]), x + barWidth / 2 - 5, y - 5);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VelocityChart().setVisible(true);
            }
        });
    }
}


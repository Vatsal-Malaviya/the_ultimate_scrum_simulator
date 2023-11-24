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



    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VelocityChart().setVisible(true);
            }
        });
    }
}


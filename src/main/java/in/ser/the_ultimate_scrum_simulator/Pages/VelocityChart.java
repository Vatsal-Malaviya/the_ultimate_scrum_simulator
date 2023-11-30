package in.ser.the_ultimate_scrum_simulator.Pages;

import javax.swing.*;
import java.awt.*;

public class VelocityChart extends JPanel {

    private int[] velocities;
    private String[] sprintNames;
    private JFrame parentFrame;
    private String role;

    public VelocityChart(JFrame frame, String role) {
        this.parentFrame = frame;
        this.role = role;


        velocities = new int[]{10, 15, 20};
        sprintNames = new String[]{"Sprint 1", "Sprint 2", "Sprint 3"};


        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(255, 255, 255));
        JLabel titleLabel = new JLabel("AGILE VELOCITY CHART", SwingConstants.CENTER);
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);


        ChartPanel chartPanel = new ChartPanel();
        JPanel chartPanelWrapper = new JPanel(new BorderLayout());
        chartPanelWrapper.add(chartPanel, BorderLayout.CENTER);


        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));


        JButton updateChartButton = new JButton("Update Chart");
        updateChartButton.setFont(new Font("Arial", Font.BOLD, 14));
        updateChartButton.addActionListener(e -> updateChart());
        bottomPanel.add(updateChartButton);


        JButton goBackButton = new JButton("Go Back");
        goBackButton.setFont(new Font("Arial", Font.BOLD, 14));
        goBackButton.addActionListener(e -> goBack(parentFrame));
        bottomPanel.add(goBackButton);


        setLayout(new BorderLayout());
        add(titlePanel, BorderLayout.NORTH);
        add(chartPanelWrapper, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void goBack(JFrame parentFrame) {

        parentFrame.getContentPane().removeAll();
        parentFrame.add(new StoryBoard(parentFrame, role));
        parentFrame.revalidate();
        parentFrame.repaint();
    }


    private void updateChart() {


        velocities[0] = Integer.parseInt(JOptionPane.showInputDialog("Enter velocity for Sprint 1:"));
        velocities[1] = Integer.parseInt(JOptionPane.showInputDialog("Enter velocity for Sprint 2:"));
        velocities[2] = Integer.parseInt(JOptionPane.showInputDialog("Enter velocity for Sprint 3:"));

        repaint();
    }

    private class ChartPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            int barWidth = 200;
            int spacing = 10;

            g.drawLine(50, getHeight() - 50, getWidth() - 50, getHeight() - 50);

            g.drawLine(50, 50, 50, getHeight() - 50);

            g.drawString("Velocity", 5, getHeight() / 2);

            g.drawString("Sprints", getWidth() / 2 - g.getFontMetrics().stringWidth("Sprints") / 2, getHeight() - 10);

            for (int i = 0; i < velocities.length; i++) {
                int barHeight = velocities[i] * 5;
                int x = 50 + i * (barWidth + spacing);
                int y = getHeight() - 50 - barHeight;

                g.setColor(Color.BLUE);
                g.fillRect(x, y, barWidth, barHeight);

                g.setColor(Color.BLACK);
                g.drawString(sprintNames[i], x + barWidth / 2 - g.getFontMetrics().stringWidth(sprintNames[i]) / 2, getHeight() - 35);

                g.drawString(Integer.toString(velocities[i]), x + barWidth / 2 - g.getFontMetrics().stringWidth(Integer.toString(velocities[i])) / 2, y - 10);
            }
        }
    }


}


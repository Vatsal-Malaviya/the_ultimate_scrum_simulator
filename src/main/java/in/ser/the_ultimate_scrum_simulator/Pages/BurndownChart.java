package in.ser.the_ultimate_scrum_simulator.Pages;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class BurndownChart {
    private JFrame frame;
    private JTextField totalScopeField;
    private JTextField remainingScopeField;
    private JButton updateButton;
    private BurndownChartPanel chartPanel;

    private List<Integer> remainingScopeData;

    public BurndownChart() {
        frame = new JFrame("Agile Burndown Chart");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        totalScopeField = new JTextField(10);
        remainingScopeField = new JTextField(10);
        updateButton = new JButton("Update Chart");
        chartPanel = new BurndownChartPanel();

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Total Scope:"));
        inputPanel.add(totalScopeField);
        inputPanel.add(new JLabel("Remaining Scope:"));
        inputPanel.add(remainingScopeField);
        inputPanel.add(updateButton);

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(chartPanel, BorderLayout.CENTER);

        remainingScopeData = new ArrayList<>();

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateChart();
            }
        });

        frame.setSize(800, 600);
        frame.setVisible(true);
    }

    private void updateChart() {
        try {
            int totalScope = Integer.parseInt(totalScopeField.getText());
            int remainingScope = Integer.parseInt(remainingScopeField.getText());
            remainingScopeData.add(remainingScope);

            chartPanel.setChartData(totalScope, remainingScopeData);
            chartPanel.repaint();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Please enter valid numeric values.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BurndownChart();
            }
        });
    }
}

class BurndownChartPanel extends JPanel {
    private int totalScope;
    private List<Integer> remainingScopeData;

    public BurndownChartPanel() {
        totalScope = 0;
        remainingScopeData = new ArrayList<>();
    }

    public void setChartData(int totalScope, List<Integer> remainingScopeData) {
        this.totalScope = totalScope;
        this.remainingScopeData = remainingScopeData;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width = getWidth();
        int height = getHeight();

        // Draw the axes
        g.drawLine(50, height - 50, width - 50, height - 50);
        g.drawLine(50, height - 50, 50, 50);

        // Draw the total scope line
        g.drawLine(50, height - 50, width - 50, height - 50);

        // Draw the X-axis title
        g.drawString("Days Remaining", width / 2 - 10, height - 10);

        // Draw the Y-axis title
        Graphics2D g2d = (Graphics2D) g;
        g2d.rotate(-Math.PI / 2);
        g2d.drawString("Remaining Scope", -height / 2, 20);
        g2d.rotate(Math.PI / 2);
        int step = (width - 100) / Math.max(1, remainingScopeData.size());
        for (int i = 0; i < remainingScopeData.size(); i++) {
            int x = 50 + i * step;
            int y = height - 50 - (remainingScopeData.get(i) * (height - 100) / totalScope);

            g.fillOval(x - 3, y - 3, 6, 6);

            if (i < remainingScopeData.size() - 1) {
                int nextX = 50 + (i + 1) * step;
                int nextY = height - 50 - (remainingScopeData.get(i + 1) * (height - 100) / totalScope);

                // Draw line to the next point
                g.drawLine(x, y, nextX, nextY);
            }
        }
    }
}
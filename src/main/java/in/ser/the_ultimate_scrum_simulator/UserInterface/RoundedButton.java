package in.ser.the_ultimate_scrum_simulator.UserInterface;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedButton extends JButton {
    private final int CORNER_RADIUS = 25;
    private final Color DEFAULT_BACKGROUND = Color.WHITE;
    private final Color HOVER_BACKGROUND = new Color(230, 230, 230);

    public RoundedButton(String Text) {
        super(Text);
        setForeground(Color.BLACK);
        setBackground(DEFAULT_BACKGROUND);
        setFont(new Font("Arial", Font.BOLD, 14));
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);

        // Hover functionality
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setBackground(HOVER_BACKGROUND);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                setBackground(DEFAULT_BACKGROUND);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Adjusted coordinates and dimensions for drawing
        int offset = 1;
        int width = getWidth() - 2 * offset;
        int height = getHeight() - 2 * offset;

        // Draw button background
        g2d.setColor(getBackground());
        g2d.fillRoundRect(offset, offset, width, height, CORNER_RADIUS, CORNER_RADIUS);

        // Draw black border
        g2d.setColor(Color.BLACK);
        g2d.drawRoundRect(offset, offset, width, height, CORNER_RADIUS, CORNER_RADIUS);

        super.paintComponent(g);
    }

    @Override
    public boolean contains(int x, int y) {
        return new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS).contains(x, y);
    }
}

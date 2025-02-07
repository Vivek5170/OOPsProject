import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class CircularButton extends JButton {
    private Color defaultColor = Color.decode("#3B3B3B");
    private Color pressedColor = Color.gray;
    private int v = 1;

    public void setV(int v) {
        this.v = v;
    }

    public  void toggleV(){
        v=-v;
        setBackground(pressedColor);
        repaint();
    }

    public CircularButton(String label) {
        super(label);
        setContentAreaFilled(false); // Make the button transparent
        setFocusPainted(false); // No focus outline
        setBorderPainted(false); // No border
        setOpaque(false); // Make it fully transparent

        // Set a larger font size for better visibility, ensuring it fits in the button
        setFont(new Font("Arial", Font.PLAIN, 20)); // Adjust font size as needed
        setBackground(defaultColor); // Set initial background color
        setForeground(Color.WHITE); // Set text color for visibility
        setMargin(new Insets(0, 0, 0, 0));
        // Add mouse listener for hover and pressed effects
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(defaultColor); // Change to hover color
                setBorder(BorderFactory.createLineBorder(Color.GRAY, 2, true)); 
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(defaultColor); // Reset to default color
                setBorder(BorderFactory.createEmptyBorder()); // Remove border
                repaint();

            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (v==1)
                defaultColor = pressedColor;

                else if (v==-1)
                defaultColor =Color.decode("#3B3B3B");
                v=-v;
                setBackground(pressedColor); // Change to pressed color
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setBackground(defaultColor); // Change back to hover color on release
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Draw circular shape
        g.setColor(getBackground());
        g.fillOval(0, 0, getWidth(), getHeight());
        super.paintComponent(g);

        // Draw circular border if the button has a border
        if (getBorder() instanceof LineBorder) {
            g.setColor(((LineBorder) getBorder()).getLineColor());
            g.drawOval(0, 0, getWidth() - 1, getHeight() - 1); // Draw circular border
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(40, 40); // Set the preferred size of the button to 40x40
    }

    @Override
    public boolean contains(int x, int y) {
        // Make sure the button responds to clicks only within the circular area
        double radius = getWidth() / 2.0;
        double centerX = radius;
        double centerY = radius;
        return Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2) <= Math.pow(radius, 2);
    }
}


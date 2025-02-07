import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class ButtonC extends JButton {
    private Color defaultColor = Color.BLACK;

    public void setDefaultColor(Color defaultColor) {
        this.defaultColor = defaultColor;
    }

    private Color hoverColor = new Color(15, 15, 15);

    private int v = 1;

    public ButtonC(String label) {
        super(label);
        setHorizontalAlignment(LEFT);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(true);
        setFont(new Font("Arial", Font.PLAIN, 20));
        setBackground(defaultColor);
        setForeground(Color.WHITE);
        // this.setFocusable(false);

        setMargin(new Insets(0, 0, 0, 0));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                v = 0;
                f(v);
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                v = 1;
                f(v);
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                v = 0;
                f(v);
                repaint();
            }
        });
    }

    private void f(int v) {
        if (v == 1) {
            setBackground(defaultColor);
        } else if (v == 0) {
            setBackground(hoverColor);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }

    void colourset() {
        setBackground(defaultColor);
    }
}

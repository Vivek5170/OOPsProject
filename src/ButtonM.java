import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ButtonM extends JButton {
    private Color defaultColor = Color.darkGray;  // Default color for the bar buttons
    private Color hoverColor = Color.decode("#5A5A5A");   // Hover color
    public void setDefaultColor(Color defaultColor) {
        this.defaultColor = defaultColor;
    }
    private int v=1;

    public ButtonM(String label) {
        super(label);
        this.setBackground(defaultColor);
        this.setForeground(Color.WHITE);
        this.setBorderPainted(false);
        this.setBorder(BorderFactory.createEmptyBorder());
        this.setContentAreaFilled(false); 
        this.setFont(new Font("Arial", Font.PLAIN, 16));
        this.setFocusable(false);
        this.setOpaque(true); 
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                v=0;
                f(v);
                repaint();  
            }

            @Override
            public void mouseExited(MouseEvent e) {
                v=1;
                f(v);
                repaint(); 
            }

            @Override
            public void mousePressed(MouseEvent e) {
                v=0;
                f(v);
                repaint();  
            }

        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        
        g.setColor(getBackground()); 
        g.fillRect(0, 0, getWidth(), getHeight()); 
        super.paintComponent(g); 
    }
    private void f(int v){
        if(v==1){
            setBackground(defaultColor);
        }
        else if(v==0){
            setBackground(hoverColor);
        }
    }
}

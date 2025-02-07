
import java.awt.*;
import javax.swing.*;

public class trash extends JPanel{
    private int cornerRadius = 25;
    private Color backgroundColor = new Color(255,50,50,128);
    JLabel p = new JLabel();
    trash(){
        this.setBackground(new Color(158, 161, 163));
        this.setLayout(null);
        p.setIcon(new ImageIcon("lib/trash.png"));
        p.setHorizontalAlignment(JLabel.CENTER);
        p.setVerticalAlignment(JLabel.CENTER);
        this.add(p);
    }

    protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setColor(backgroundColor);
    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
}



}
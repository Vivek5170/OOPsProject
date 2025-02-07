import javax.swing.*;
import java.awt.*;

public class Helpbar extends JPanel {
   CircularButton c1 = new CircularButton("");
   CircularButton c2 = new CircularButton("");
   CircularButton c3 = new CircularButton("");
   private ImageIcon i1 = new ImageIcon("lib\\qm1.png");
   private ImageIcon i2 = new ImageIcon("lib\\G.png");
   private ImageIcon i3 = new ImageIcon("lib\\w.png");
   private JLabel l1 = new JLabel("Help");

   Helpbar() {
      this.setBackground(Color.BLACK);
      c1.setV(0);
      c1.setIcon(i1);
      c2.setIcon(i2);
      c3.setIcon(i3);
      this.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
      l1.setFont(new Font("Arial", Font.BOLD, 20));
      l1.setForeground(Color.WHITE);
      this.add(c1);
      this.add(l1);
      JPanel temp = new JPanel();
      temp.setPreferredSize(new Dimension(150, 10));
      temp.setBackground(null);
      this.add(temp);
      this.add(c2);
      this.add(c3);
   }
}

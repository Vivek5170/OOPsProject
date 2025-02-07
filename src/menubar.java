import javax.swing.*;
import java.awt.*;

public class menubar extends JMenuBar {
  ButtonM b1 = new ButtonM("New");
  ButtonM b2 = new ButtonM("Load");
  ButtonM b3 = new ButtonM("Save");
  ButtonM b4 = new ButtonM("Snap");
  menubar() {
    this.setBackground(Color.DARK_GRAY);
    this.setBorder(null);
    
  
    this.setLayout(new GridLayout(1, 4,0,0));
    
    add(b1);
    add(b2);
    add(b3);
    add(b4);
  }
}

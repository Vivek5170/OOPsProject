import java.awt.Color;
import javax.swing.JPanel;

public class ControlPanel extends JPanel {
  menubar mb = new menubar();
  menupanel mp = new menupanel();
  Helpbar hp = new Helpbar();
  ButtonM s1 = new ButtonM("  GridSnap   ");
  ButtonM s2 = new ButtonM("  WallSnap   ");
  ButtonM saveButton = new ButtonM("Save Project");
  ButtonM pngButton = new ButtonM(" PNG File ");

  ControlPanel() {
    // controlpanel settigns
    this.setBackground(Color.BLACK);
    this.setLayout(null);

    // adding
    this.add(mb);
    this.add(hp);
    this.add(mp);

  }

  public int width() {
    return mp.width();
  }

  public int height() {
    return mp.height();
  }
}

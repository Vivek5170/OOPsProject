import javax.swing.*;
import java.awt.*;

public class menupanel extends JPanel {
    ButtonC b1 = new ButtonC("     - Add Room");
    ButtonC b2 = new ButtonC("     - Add Furniture");
    ButtonC b3 = new ButtonC("     - Add Fixture");
    private JLabel name = new JLabel();
    private inputs para = new inputs();
    ButtonC r1 = new ButtonC("         • Living Room");
    ButtonC r2 = new ButtonC("         • Bed Room");
    ButtonC r3 = new ButtonC("         • Dining Space");
    ButtonC r4 = new ButtonC("         • Kitchen");
    ButtonC r5 = new ButtonC("         • Bath Room");
    ButtonC f1 = new ButtonC("         • Bed");
    ButtonC f2 = new ButtonC("         • Table");
    ButtonC f3 = new ButtonC("         • Sofa");
    ButtonC f4 = new ButtonC("         • Chair");
    ButtonC f5 = new ButtonC("         • Dining Table");
    ButtonC f6 = new ButtonC("         • Sink");
    ButtonC f7 = new ButtonC("         • Stove");
    ButtonC f8 = new ButtonC("         • Commode");
    ButtonC f9 = new ButtonC("         • Wash Basin");
    ButtonC f10 = new ButtonC("         • Shower");
    boolean m1 = false;
    boolean m2 = false;
    boolean m3 = false;

    menupanel() {
        this.setBackground(null);
        this.setLayout(new GridLayout(13, 1, 10, 0));
        name.setText("  Design Kit");
        name.setBackground(Color.BLACK);
        name.setFont(new Font("Arial", Font.BOLD, 25));
        name.setForeground(Color.white);
        b1.setFont(new Font("Arial", Font.ROMAN_BASELINE, 22));
        b2.setFont(new Font("Arial", Font.ROMAN_BASELINE, 22));
        b3.setFont(new Font("Arial", Font.ROMAN_BASELINE, 22));
        this.add(name);
        this.add(b1);
        this.add(b2);
        this.add(b3);
    }

    public void fun1() {
        m1 = true;
        m2 = false;
        m3 = false;
        b1.setDefaultColor(new Color(15, 15, 15));
        b2.setDefaultColor(Color.BLACK);
        b3.setDefaultColor(Color.BLACK);
        b1.colourset();
        b2.colourset();
        b3.colourset();
        this.removeAll();
        this.add(name);
        this.add(b1);
        this.add(para);
        this.add(r1);
        this.add(r2);
        this.add(r3);
        this.add(r4);
        this.add(r5);
        this.add(b2);
        this.add(b3);
        this.revalidate();
        this.repaint();
    }

    public void fun4() {
        m1 = false;
        m2 = false;
        m3 = false;
        b1.setDefaultColor(Color.BLACK);
        b2.setDefaultColor(Color.BLACK);
        b3.setDefaultColor(Color.BLACK);
        b1.colourset();
        b2.colourset();
        b3.colourset();
        this.removeAll();
        this.add(name);
        this.add(b1);
        this.add(b2);
        this.add(b3);
        this.revalidate();
        this.repaint();
    }

    public void fun2() {
        m1 = false;
        m2 = true;
        m3 = false;
        b2.setDefaultColor(new Color(15, 15, 15));
        b1.setDefaultColor(Color.BLACK);
        b3.setDefaultColor(Color.BLACK);
        b1.colourset();
        b2.colourset();
        b3.colourset();
        this.removeAll();
        this.add(name);
        this.add(b1);
        this.add(b2);
        this.add(f1);
        this.add(f2);
        this.add(f3);
        this.add(f4);
        this.add(f5);
        this.add(b3);
        this.revalidate();
        this.repaint();
    }

    public void fun3() {
        m1 = false;
        m2 = false;
        m3 = true;
        b3.setDefaultColor(new Color(15, 15, 15));
        b1.setDefaultColor(Color.BLACK);
        b2.setDefaultColor(Color.BLACK);
        b1.colourset();
        b2.colourset();
        b3.colourset();
        this.removeAll();
        this.add(name);
        this.add(b1);
        this.add(b2);
        this.add(b3);
        this.add(f6);
        this.add(f7);
        this.add(f8);
        this.add(f9);
        this.add(f10);
        this.revalidate();
        this.repaint();
    }

    public int width() {
        return para.width();
    }

    public int height() {
        return para.height();
    }

}

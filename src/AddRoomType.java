import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JPopupMenu;

public class AddRoomType extends JPopupMenu{
    ButtonM b1 = new ButtonM("  Regular add ");
    ButtonM b2 = new ButtonM("  Relative add ");
    AddRoomType(){
        setBorder(BorderFactory.createEmptyBorder());
        setLayout(new GridLayout(2, 1, 0, 0));
        this.add(b1);
        this.add(b2);
    }

}

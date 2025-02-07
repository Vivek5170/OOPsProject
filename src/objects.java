import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class objects extends JPanel {
    //private static final long serialVersionUID = 1L;
    ImageIcon image;
    int width;
    int height;
    private static boolean enable = true;
    private boolean isSelected = false; // To keep track of whether the object is selected
    private static objects selectedObject; // Static reference to the selected object
    Point prevPt;
    int n;
    private int rotation = 0; // Rotation angle in degrees (0, 90, 180, 270)

    public static void deselect1() {
        if (selectedObject != null) {
            selectedObject.deselect();
        }
    }

    private void initListeners() {
        ClickAndDragListener listener = new ClickAndDragListener();
        addMouseListener(listener);
        addMouseMotionListener(listener);
    }

    // Restore functionality after deserialization
    public void restoreFunctionality() {
        initListeners(); // Reinitialize the event listeners
        if (image == null) { // Reload the image if it's null (transient field)
            loadImage(n); // Assuming 1 is the type for this object; adapt as needed
        }
    }

    private void loadImage(int n) {
        this.n =n;
        String path = switch (n) {
            case 1 -> "lib\\lib\\bed.png";
            case 4 -> "lib\\lib\\chair.png";
            case 8 -> "lib\\lib\\commode.png";
            case 9 -> "lib\\lib\\sink.png";
            case 5 -> "lib\\lib\\Diningset.png";
            case 6 -> "lib\\lib\\sink.png";
            case 3 -> "lib\\lib\\sofa.png";
            case 2 -> "lib\\lib\\table.png";
            case 10-> "lib\\lib\\shower.png";
            case 7 -> "lib\\lib\\stove.png";

            default -> null;
        };
        if (path != null) {
            image = new ImageIcon(path);
            width = image.getIconWidth();
            height = image.getIconHeight();
            setBounds(4, 4, width, height);
        } else {
            throw new IllegalArgumentException("Invalid object type");
        }
    }

    objects(int n) {

        loadImage(n);
        this.setBackground(null);
        initListeners();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        // Apply rotation
        g2d.rotate(Math.toRadians(rotation), getWidth() / 2.0, getHeight() / 2.0); 

        // Draw the image centered
        int x = (getWidth() - width) / 2;
        int y = (getHeight() - height) / 2;
        image.paintIcon(this, g2d, x, y);

        g2d.dispose();

        // Draw selection overlay if selected
        if (isSelected) {
            g.setColor(new Color(0, 0, 200, 75));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    // Rotate the object by 90 degrees clockwise
    public void rotateCW() {
        rotation = (rotation + 90) % 360;
        updateBounds();
        repaint();
    }

    // Rotate the object by 90 degrees counterclockwise
    public void rotateACW() {
        rotation = (rotation - 90 + 360) % 360;
        updateBounds();
        repaint();
    }

    // Update the bounding box to account for rotation
    private void updateBounds() {
        if (rotation % 180 != 0) { // 90 or 270 degrees
            setBounds(getX(), getY(), height, width);
        } else { // 0 or 180 degrees
            setBounds(getX(), getY(), width, height);
        }
        revalidate();
    }

    public void select() {
        if (selectedObject != null && selectedObject != this) {
            selectedObject.deselect();
        }
        selectedObject = this;
        isSelected = true;
        setBorder(BorderFactory.createLineBorder(Color.blue, 1));
        repaint();
    }

    public void deselect() {
        isSelected = false;
        selectedObject = null;
        setBorder(BorderFactory.createEmptyBorder());
        repaint();
    }

    private class ClickAndDragListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            prevPt = e.getPoint();

            if (SwingUtilities.isRightMouseButton(e)) {
                showContextMenu(e); // Show context menu on right-click
                return; // Do not handle selection when right-click is detected
            }

            if (SwingUtilities.isLeftMouseButton(e)) {
                if (!isSelected) {
                    select();
                } else {
                    deselect();
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            setBorder(BorderFactory.createEmptyBorder());
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (!enable)
                return;
            
            if (!isSelected) select();

            Point currentPt = e.getPoint();
            Container parent = getParent();
            Point panelLocation = getLocation();
            int newX = panelLocation.x + (currentPt.x - prevPt.x);
            int newY = panelLocation.y + (currentPt.y - prevPt.y);
            Rectangle temp = new Rectangle();
            if (parent != null) {
                boolean collisionDetected = false;
                Dimension parentSize = parent.getSize();
                if (newX > parentSize.width - 4 - width || newY > parentSize.height- 4 - height) 
                    collisionDetected = true;
                    newX = Math.max(4, Math.min(newX, parentSize.width - width));
                    newY = Math.max(4, Math.min(newY, parentSize.height - height));
                    temp.height = height;
                    temp.width = width;
                    temp.x = newX;
                    temp.y = newY;
                for (Component comp : parent.getComponents()) {
                    if (comp != objects.this && comp instanceof objects) {
                        Rectangle otherBounds = comp.getBounds();
                        if (temp.intersects(otherBounds)) {
                            collisionDetected = true;
                            break;
                        }
                    }
                }
                    if(!collisionDetected)
                    setLocation(newX, newY);
                
            }
        }

        private void showContextMenu(MouseEvent e) {
            JPopupMenu menu = new JPopupMenu();
            JMenuItem deleteObject = new JMenuItem("Delete this Object");
            JMenuItem deleteObjectselected = new JMenuItem("Delete selected Object");
            JMenuItem rotateCW = new JMenuItem("Rotate 90° CW");
            JMenuItem rotateACW = new JMenuItem("Rotate 90° ACW");
            deleteObject.addActionListener(event -> deleteObject());
            deleteObjectselected.addActionListener(event -> deleteObjectselected());
            rotateCW.addActionListener(event -> rotateCW());
            rotateACW.addActionListener(event -> rotateACW());
            menu.add(deleteObject);
            if (!(selectedObject == null))
                menu.add(deleteObjectselected);
            menu.add(rotateCW);
            menu.add(rotateACW);
            menu.show(e.getComponent(), e.getX(), e.getY());
        }

        private void deleteObject() {
            room parent = (room)getParent();
            if (parent != null) {
                parent.remove(objects.this);
                parent.placedobjects.remove(objects.this);
                parent.repaint();
            }
        }

        private void deleteObjectselected() {
            room parent = (room)getParent();
            if (parent != null) {
                parent.remove(objects.this);
                parent.placedobjects.remove(objects.this);
                parent.repaint();
            }
        }
    }

    public boolean isMouseInRoom(MouseEvent e, Container targetRoom) {
        Point mousePoint = SwingUtilities.convertPoint(this, e.getPoint(), targetRoom);
        return targetRoom.contains(mousePoint);
    }

    public boolean objectsOverlap(objects obj1, objects obj2) {
        return obj1.getBounds().intersects(obj2.getBounds());
    }
}

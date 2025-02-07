import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class room extends JPanel {
    //private static final long serialVersionUID = 1L;
    private int roomType;
    public int getRoomType() {
        return roomType;
    }

    private Color roomColor;
    private int width, height;
    private Point originalLocation;
    private boolean isDragged = false;
    private Point mouseOffset;
    private boolean isSelected = false;

    public boolean isSelected() {
        return isSelected;
    }

    private static room selectedRoom;
    private static boolean enable = true;
    List<objects> placedobjects = new ArrayList<>();
    List<door> placeddoorsandwindows = new ArrayList<>();
    List<window> placedwindows = new ArrayList<>();

    public static void toggle() {
        enable = !enable;
        if (selectedRoom != null) {
            selectedRoom.deselect();
        }
    }

    public static boolean isEnableFalse() {
        return !enable;
    }

    public static room getSelectedRoom() {
        return selectedRoom;
    }

    public static void clear() {
        selectedRoom = null;
    }

    public room(int roomType, int width, int height) {
        this.roomType = roomType;
        this.width = width;
        this.height = height;
        this.setBounds(5, 5, width, height);
        this.setPreferredSize(new Dimension(width, height));

        switch (roomType) {
            case 1 -> this.roomColor = Color.decode("#FFA500");
            case 2 -> this.roomColor = Color.decode("#32CD32");
            case 3 -> this.roomColor = Color.decode("#FFD700");
            case 4 -> this.roomColor = Color.decode("#FF6347");
            case 5 -> this.roomColor = Color.decode("#4682B4");
        }

        this.setBackground(roomColor);
        this.setLayout(null);

        restoreFunctionality();
    }

    private void handleRightClick(MouseEvent e) {
        JPopupMenu contextMenu = new JPopupMenu();
        JMenuItem deleteRoom = new JMenuItem("Delete selected Room");
        JMenuItem deleteRoomthis = new JMenuItem("Delete this Room");
        JMenuItem clearCanvas = new JMenuItem("Clear Canvas");

        // "Add Door" with direction submenu
        JMenu addDoorMenu = new JMenu("Add Door");
        JMenu addWindowMenu = new JMenu("Add Window");
        JMenuItem addDoorNorth = new JMenuItem("North");
        JMenuItem addDoorSouth = new JMenuItem("South");
        JMenuItem addDoorEast = new JMenuItem("East");
        JMenuItem addDoorWest = new JMenuItem("West");
        JMenuItem addWindowNorth = new JMenuItem("North");
        JMenuItem addWindowSouth = new JMenuItem("South");
        JMenuItem addWindowEast = new JMenuItem("East");
        JMenuItem addWindowWest = new JMenuItem("West");

        addWindowNorth.addActionListener(ev -> addWindow(1));
        addWindowSouth.addActionListener(ev -> addWindow(2));
        addWindowEast.addActionListener(ev -> addWindow(3));
        addWindowWest.addActionListener(ev -> addWindow(4));
        addDoorNorth.addActionListener(ev -> addDoor(1));
        addDoorSouth.addActionListener(ev -> addDoor(2));
        addDoorEast.addActionListener(ev -> addDoor(3));
        addDoorWest.addActionListener(ev -> addDoor(4));

        addDoorMenu.add(addDoorNorth);
        addDoorMenu.add(addDoorSouth);
        addDoorMenu.add(addDoorEast);
        addDoorMenu.add(addDoorWest);
        addWindowMenu.add(addWindowNorth);
        addWindowMenu.add(addWindowSouth);
        addWindowMenu.add(addWindowEast);
        addWindowMenu.add(addWindowWest);

        deleteRoom.addActionListener(ev -> {
            if (!(selectedRoom == null)) {
                CanvasPanel parentCanvas = (CanvasPanel) getParent();
                parentCanvas.placedRooms.remove(getSelectedRoom());
                parentCanvas.remove(getSelectedRoom());
                parentCanvas.repaint();
                clear();
            }
        });

        deleteRoomthis.addActionListener(ev -> {
            CanvasPanel parentCanvas = (CanvasPanel) getParent();
            if (room.this == selectedRoom) {
                selectedRoom = null;
            }
            parentCanvas.placedRooms.remove(this);
            parentCanvas.remove(this);
            parentCanvas.repaint();
        });

        clearCanvas.addActionListener(ev -> {
            CanvasPanel parentCanvas = (CanvasPanel) getParent();
            parentCanvas.clear();
            clear();
        });
        contextMenu.add(clearCanvas);
        contextMenu.add(deleteRoomthis);
        if (!(selectedRoom == null))
            contextMenu.add(deleteRoom);
        contextMenu.add(addDoorMenu);
        contextMenu.add(addWindowMenu);
        contextMenu.show(this, e.getX(), e.getY());
    }

    // Method to add door based on selected direction
    private void addDoor(int direction) {
        if(roomType==2||roomType==5){
            CanvasPanel parent= (CanvasPanel)getParent();
            if(parent.ReturnWallDirectionAdjacent(room.this, 0.25)[direction-1]==0){
                if(roomType==2)
                JOptionPane.showMessageDialog(this, "For bedroom you cannot add a door to outside");
                else
                JOptionPane.showMessageDialog(this, "For bathroom you cannot add a door to outside");
                return;
            }
        }
        door newDoor = new door(roomColor, direction);
        int roomWidth = getWidth();
        int roomHeight = getHeight();
        int x = 0, y = 0;
        switch (direction) {
            case 1 -> {
                x = 4;
                y = 0;
            }
            case 2 -> {
                x = 4;
                y = roomHeight - 4;
            }
            case 3 -> {
                x = roomWidth - 4;
                y = 4;
            }
            case 4 -> {
                x = 0;
                y = 4;
            }
        }
        boolean hasOverlap;
        do {
            hasOverlap = false;
            for (door placedDoor : placeddoorsandwindows) {
                if (placedDoor.getDoorAlignment() == direction
                        && placedDoor.getBounds().intersects(new Rectangle(x, y, 20, 20))) {
                    switch (direction) {
                        case 1, 2 -> x = placedDoor.getX() + 20; // Move horizontally
                        case 3, 4 -> y = placedDoor.getY() + 20; // Move vertically
                    }
                    hasOverlap = true;
                    break;
                }
            }
            for (window placedDoor : placedwindows) {
                if (placedDoor.getWindowAlignment() == direction
                        && placedDoor.getBounds().intersects(new Rectangle(x, y, 20, 20))) {
                    switch (direction) {
                        case 1, 2 -> x = placedDoor.getX() + 20; // Move horizontally
                        case 3, 4 -> y = placedDoor.getY() + 20; // Move vertically
                    }
                    hasOverlap = true;
                    break;
                }
            }
        } while (hasOverlap && (x < roomWidth || y < roomHeight)); // Ensure the door stays within bounds

        if ((direction == 1 || direction == 2) && x + 16 < roomWidth
                || (direction == 3 || direction == 4) && y + 16 < roomHeight) {
            add(newDoor);
            placeddoorsandwindows.add(newDoor);
            newDoor.setall();
            newDoor.setLocation(x, y);
            repaint();
        } else {
            JOptionPane.showMessageDialog(this, "Cannot place the door. No space found");
        }
    }

    private void addWindow(int direction) {
        CanvasPanel parent = (CanvasPanel)getParent();
        if(parent.ReturnWallDirectionAdjacent(room.this,0.6)[direction-1]==1){
            JOptionPane.showMessageDialog(this, "Cannot add window between two rooms");
            return;
        }
        window newDoor = new window(roomColor, direction); // Assuming door takes color and direction
        int roomWidth = getWidth();
        int roomHeight = getHeight();
        int x = 0, y = 0;
        switch (direction) {
            case 1 -> {
                x = 4;
                y = 0;
            }
            case 2 -> {
                x = 4;
                y = roomHeight - 4;
            }
            case 3 -> {
                x = roomWidth - 4;
                y = 4;
            }
            case 4 -> {
                x = 0;
                y = 4;
            }
        }
        boolean hasOverlap;
        do {
            hasOverlap = false;
            for (window placedDoor : placedwindows) {
                if (placedDoor.getWindowAlignment() == direction
                        && placedDoor.getBounds().intersects(new Rectangle(x, y, 20, 20))) {
                    switch (direction) {
                        case 1, 2 -> x = placedDoor.getX() + 20; // Move horizontally
                        case 3, 4 -> y = placedDoor.getY() + 20; // Move vertically
                    }
                    hasOverlap = true;
                    break;
                }
            }
            for (door placedDoor : placeddoorsandwindows) {
                if (placedDoor.getDoorAlignment() == direction
                        && placedDoor.getBounds().intersects(new Rectangle(x, y, 20, 20))) {
                    switch (direction) {
                        case 1, 2 -> x = placedDoor.getX() + 20; // Move horizontally
                        case 3, 4 -> y = placedDoor.getY() + 20; // Move vertically
                    }
                    hasOverlap = true;
                    break;
                }
            }
        } while (hasOverlap && (x < roomWidth || y < roomHeight)); // Ensure the door stays within bounds

        if ((direction == 1 || direction == 2) && x + 16 < roomWidth
                || (direction == 3 || direction == 4) && y + 16 < roomHeight) {
            add(newDoor);
            placedwindows.add(newDoor);
            newDoor.setall();
            newDoor.setLocation(x, y);
            repaint();
        } else {
            JOptionPane.showMessageDialog(this, "Cannot place the door. No space found");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isSelected) {
            g.setColor(new Color(0, 0, 200, 50));
            g.fillRect(-5, -5, getWidth() + 10, getHeight() + 10);
        }
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(8));
        g2d.drawRect(0, 0, getWidth(), getHeight());
    }

    public void select() {
        if (selectedRoom != null && selectedRoom != this) {
            selectedRoom.deselect();
        }
        selectedRoom = this;
        isSelected = true;
        setBorder(BorderFactory.createLineBorder(Color.blue, 1));
        repaint();
    }

    public void deselect() {
        isSelected = false;
        selectedRoom = null;
        setBorder(BorderFactory.createEmptyBorder());
        repaint();
    }

    public void addobject(objects obj) {
        add(obj);
        if (overlap(obj)) {
            placedobjects.add(obj);
            repaint();
        } else {
            remove(obj);
            repaint();
            JOptionPane.showMessageDialog(null, "No valid place found to add given object");
        }
    }

    public boolean overlap(objects r) {
        boolean hasOverlap = true;
        int attempts = 0;
        while (hasOverlap && attempts < 300) {
            hasOverlap = false;
            for (objects placed : placedobjects) {
                if (r.objectsOverlap(r, placed)) {
                    hasOverlap = true;
                    if (placed.getX() + placed.getWidth() + r.getWidth() <= getWidth()-4) {
                        r.setLocation(placed.getX() + placed.getWidth() + 5, r.getY());
                    } else if (placed.getY() + placed.getHeight() + r.getHeight() <= getHeight()-4) {
                        r.setLocation(4, placed.getY() + placed.getHeight() + 5);
                    } else {
                        return false;
                    }
                    break;
                }
            }
            attempts++;
        }
        return attempts <= 300;
    }

    private boolean checkRoomOverlap(CanvasPanel parentCanvas) {
        for (room placedRoom : parentCanvas.placedRooms) {
            if (placedRoom != this && parentCanvas.roomsOverlap(this, placedRoom)) {
                JOptionPane.showMessageDialog(null, "Overlap detected! Reverting to original position.");
                return true;
            }
        }
        return false;
    }

    public void addObject(objects obj) {
        placedobjects.add(obj);
        this.add(obj); // Add object to room UI
        obj.restoreFunctionality();
    }

    // public void addDoor(door d) {
    // placeddoorsandwindows.add(d);
    // this.add(d); // Add object to room UI
    // d.initListeners();
    // }

    public List<door> getDoors() {
        return placeddoorsandwindows;
    }

    public List<window> getWindows() {
        return placedwindows;
    }

    public List<objects> getObjects() {
        return placedobjects;
    }

    public void restoreFunctionality() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    handleRightClick(e);
                    return;
                }
                if (enable && SwingUtilities.isLeftMouseButton(e)) {
                    CanvasPanel parentCanvas = (CanvasPanel) getParent();
                    parentCanvas.remove(room.this);
                    parentCanvas.add(room.this, Integer.valueOf(3));
                    parentCanvas.repaint();
                    originalLocation = getLocation();
                    isDragged = true;
                    mouseOffset = e.getPoint();
                    if (!isSelected)
                        select();
                    else
                        deselect();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    // Let the context menu logic handle the right-click, do nothing here
                    return;
                }
                if (enable && isDragged && SwingUtilities.isLeftMouseButton(e)) {
                    CanvasPanel parentCanvas = (CanvasPanel) getParent();

                    if (parentCanvas.snapToGrid) {
                        setLocation(parentCanvas.snapToGrid(room.this));
                    }

                    if (parentCanvas.trashOverlap(parentCanvas.t, room.this)) {
                        parentCanvas.placedRooms.remove(room.this);
                        parentCanvas.remove(room.this);
                        selectedRoom = null;
                        parentCanvas.repaint();
                    } else {

                        parentCanvas.snapToWall(room.this);
                        if (checkRoomOverlap(parentCanvas)) {
                            setLocation(originalLocation);
                        }
                        parentCanvas.remove(room.this);
                        parentCanvas.add(room.this, Integer.valueOf(2));
                        parentCanvas.repaint();
                    }
                    isDragged = false;
                }
            }
        });

        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    // Let the context menu logic handle the right-click, do nothing here
                    return;
                }
                if (isDragged && SwingUtilities.isLeftMouseButton(e)) {
                    Point currentPt = e.getPoint();
                    Container parent = getParent();
                    Point panelLocation = getLocation();
                    int newX = panelLocation.x + (currentPt.x - mouseOffset.x);
                    int newY = panelLocation.y + (currentPt.y - mouseOffset.y);
                    Dimension parentSize = parent.getSize();
                    newX = Math.max(0, Math.min(newX, parentSize.width - width));
                    newY = Math.max(0, Math.min(newY, parentSize.height - height));
                    setLocation(newX, newY);
                    if (!isSelected)
                        select();
                }
            }
        });

    }
}

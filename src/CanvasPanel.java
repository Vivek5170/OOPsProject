import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class CanvasPanel extends JLayeredPane {
    List<room> placedRooms = new ArrayList<>();
    int currentX = 0, currentY = 0;
    trash t = new trash();
    boolean gridEnabled = false;
    boolean trashEnabled = false;
    boolean snapToGrid = false;
    boolean snapToWallEnabled = false;
    int snapThreshold = 10;

    JPanel statusPanel;
    JLabel gridStatusLabel = new JLabel("Grid Snap: ON");
    JLabel wallSnapStatusLabel = new JLabel("Wall Snap: ON");

    CanvasPanel() {
        setLayout(null);
        // add(t, Integer.valueOf(1));
        statusPanel = new JPanel(new FlowLayout());
        statusPanel.setOpaque(false);
        statusPanel.setBounds(0, getHeight() - 30, getWidth(), 30);

        gridStatusLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        gridStatusLabel.setForeground(new Color(0, 0, 0, 128));
        wallSnapStatusLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        wallSnapStatusLabel.setForeground(new Color(0, 0, 0, 128));
        add(statusPanel, Integer.valueOf(1));
        // Delete key functionality
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println(e.getKeyCode());
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    // deleteSelectedRoom();
                }
            }
        });
    }

    public void saveProject() {
        JFileChooser fileChooser = new JFileChooser();
        int choice = fileChooser.showSaveDialog(this);
        if (choice == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                ProjectFileManager.saveProject(file, placedRooms);
                JOptionPane.showMessageDialog(this, "Project saved successfully!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving project: " + ex.getMessage());
            }
        }
    }

    public void loadProject() {
        JFileChooser fileChooser = new JFileChooser();
        int choice = fileChooser.showOpenDialog(this);
        if (choice == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                List<room> loadedRooms = ProjectFileManager.loadProject(file);
                clear();
                placedRooms.addAll(loadedRooms);
                for (room r : loadedRooms) {
                    r.restoreFunctionality(); // Restore room functionality
                    add(r, Integer.valueOf(2));
                    if (r.getObjects() != null) {
                        for (objects obj : r.getObjects()) {
                            r.add(obj); // Add objects to each room
                            obj.restoreFunctionality(); // Restore object functionality
                        }
                    }
                    if (r.getDoors() != null) {
                        for (door d : r.getDoors()) {
                            r.add(d); // Add objects to each room
                            d.initListeners(); // Restore object functionality
                        }
                    }
                    if (r.getWindows() != null) {
                        for (window d : r.getWindows()) {
                            r.add(d); // Add objects to each room
                            d.setborder();
                            d.initListeners(); // Restore object functionality
                        }
                    }

                }
                repaint();
                JOptionPane.showMessageDialog(this, "Project loaded successfully!");
            } catch (IOException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "Error loading project: " + ex.getMessage());
            }
        }
    }

    public void savepng(File file) {
        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        if (!(room.getSelectedRoom() == null))
            room.getSelectedRoom().deselect();
        objects.deselect1();
        if (trashEnabled) {
            toggletrash();
            this.paint(g2d);
            toggletrash();
        } else {
            this.paint(g2d);
        }
        g2d.dispose();
        try {
            ImageIO.write(image, "png", file);
            JOptionPane.showMessageDialog(this, "Project saved as PNG successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving PNG: " + e.getMessage(), "Save Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void toggleSnapToGrid() {
        snapToGrid = !snapToGrid;
        updateStatusPanel();
    }

    public void toggleGrid() {
        gridEnabled = !gridEnabled;
        repaint();
    }

    public void toggleSnapToWall() {
        snapToWallEnabled = !snapToWallEnabled;
        updateStatusPanel();
    }

    private void updateStatusPanel() {
        statusPanel.removeAll();
        if (snapToGrid)
            statusPanel.add(gridStatusLabel);
        if (snapToWallEnabled)
            statusPanel.add(wallSnapStatusLabel);
        statusPanel.revalidate();
        statusPanel.repaint();
    }

    public void trashdimension(int x, int y, int z, int w) {
        t.setBounds(x, y, z, w);
    }

    public void toggletrash() {
        if (trashEnabled) {
            this.remove(t);
            repaint();
            trashEnabled = !trashEnabled;
        } else {
            trashEnabled = !trashEnabled;
            this.add(t, Integer.valueOf(1));
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (gridEnabled) {
            g.setColor(Color.GRAY);
            int dotSize = 2;
            for (int x = 0; x < getWidth(); x += 20) {
                for (int y = 0; y < getHeight(); y += 20) {
                    g.fillOval(x - dotSize / 2, y - dotSize / 2, dotSize, dotSize);
                }
            }
        }
    }

    public void addRoom(room room) {
        this.add(room, Integer.valueOf(2));
        if (overlap(room)) {
            placedRooms.add(room);
            repaint();
        } else {
            this.remove(room);
            JOptionPane.showMessageDialog(null, "No valid place found to add given room");
        }
    }

    public void addRoom(room room1, int a, int b) {
        room room2 = room.getSelectedRoom();
        Point newLocation = getNewLocation(room1, room2, a, b);
        if (newLocation == null) {
            JOptionPane.showMessageDialog(null, "Alignment not possible due to overlap");
            return;
        }
        room1.setLocation(newLocation);
        if (isWithinCanvasBounds(room1)) {
            placedRooms.add(room1);
            this.add(room1);
            repaint();
        } else {
            JOptionPane.showMessageDialog(null, "Room placement out of canvas bounds");
        }
    }

    private Point getNewLocation(room room1, room room2, int a, int b) {
        int newX = room2.getX();
        int newY = room2.getY();

        switch (a) {
            case 1 -> newY -= room1.getHeight();
            case 2 -> newY += room2.getHeight();
            case 3 -> newX += room2.getWidth();
            case 4 -> newX -= room1.getWidth();
        }

        switch (b) {
            case 2 -> {
                if (a == 1 || a == 2) {
                    newX += room2.getWidth() / 2 - room1.getWidth() / 2;
                } else {
                    newY += room2.getHeight() / 2 - room1.getHeight() / 2;
                }
            }
            case 3 -> {
                if (a == 1 || a == 2) {
                    newX += room2.getWidth() - room1.getWidth();
                } else {
                    newY += room2.getHeight() - room1.getHeight();
                }
            }
        }

        room1.setLocation(newX, newY);

        for (room placed : placedRooms) {
            if (roomsOverlap(room1, placed)) {
                return null;
            }
        }
        return new Point(newX, newY);
    }

    private boolean isWithinCanvasBounds(room room) {
        return room.getX() >= 0 && room.getY() >= 0
                && room.getX() + room.getWidth() < getWidth() - 20
                && room.getY() + room.getHeight() < getHeight() - 43;
    }

    public boolean overlap(room r) {
        boolean hasOverlap = true;
        int attempts = 0;
        while (hasOverlap && attempts < 300) {
            hasOverlap = false;
            for (room placed : placedRooms) {
                if (roomsOverlap(r, placed)) {
                    hasOverlap = true;
                    if (placed.getX() + placed.getWidth() + r.getWidth() <= getWidth() - 20) {
                        r.setLocation(placed.getX() + placed.getWidth() + 5, r.getY());
                    } else if (placed.getY() + placed.getHeight() + r.getHeight() <= getHeight() - 43) {
                        r.setLocation(5, placed.getY() + placed.getHeight() + 5);
                        break;
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

    public boolean roomsOverlap(room room1, room room2) {
        return room1.getBounds().intersects(room2.getBounds());
    }

    public boolean trashOverlap(trash t, room r) {
        if (trashEnabled)
            return t.getBounds().intersects(r.getBounds());
        else
            return false;
    }

    public void clear() {
        for (room r : placedRooms) {
            this.remove(r);
        }
        placedRooms.clear();
        repaint();
    }

    public int[] ReturnWallDirectionAdjacent(room r, double overlap) {
        int[] adjacency = new int[4]; // 0: North, 1: South, 2: East, 3: West
        Rectangle rBounds = r.getBounds();
    
        for (room placedRoom : placedRooms) {
            if (placedRoom == r) continue; // Skip the room itself
    
            Rectangle placedBounds = placedRoom.getBounds();
    
            // Check North adjacency (top edge of r touching bottom edge of placedRoom)
            if (rBounds.y == placedBounds.y + placedBounds.height) {
                int overlapWidth = Math.min(rBounds.x + rBounds.width, placedBounds.x + placedBounds.width)
                        - Math.max(rBounds.x, placedBounds.x);
                if (overlapWidth >= overlap * rBounds.width) {
                    adjacency[0] = 1;
                }
            }
    
            // Check South adjacency (bottom edge of r touching top edge of placedRoom)
            if (rBounds.y + rBounds.height == placedBounds.y) {
                int overlapWidth = Math.min(rBounds.x + rBounds.width, placedBounds.x + placedBounds.width)
                        - Math.max(rBounds.x, placedBounds.x);
                if (overlapWidth >= overlap * rBounds.width) {
                    adjacency[1] = 1;
                }
            }
    
            // Check East adjacency (right edge of r touching left edge of placedRoom)
            if (rBounds.x + rBounds.width == placedBounds.x) {
                int overlapHeight = Math.min(rBounds.y + rBounds.height, placedBounds.y + placedBounds.height)
                        - Math.max(rBounds.y, placedBounds.y);
                if (overlapHeight >= overlap * rBounds.height) {
                    adjacency[2] = 1;
                }
            }
    
            // Check West adjacency (left edge of r touching right edge of placedRoom)
            if (rBounds.x == placedBounds.x + placedBounds.width) {
                int overlapHeight = Math.min(rBounds.y + rBounds.height, placedBounds.y + placedBounds.height)
                        - Math.max(rBounds.y, placedBounds.y);
                if (overlapHeight >= overlap * rBounds.height) {
                    adjacency[3] = 1;
                }
            }
        }
    
        return adjacency;
    }
    

    public Point snapToGrid(room r) {
        int mainX = r.getX(), mainY = r.getY();

        int[] gridPoints = { (mainX / 20) * 20, (mainY / 20) * 20, (mainX / 20) * 20 + 20, (mainY / 20) * 20 + 20 };

        double[] distances = {
                distance(mainX, mainY, gridPoints[0], gridPoints[1]),
                distance(mainX, mainY, gridPoints[2], gridPoints[1]),
                distance(mainX, mainY, gridPoints[0], gridPoints[3]),
                distance(mainX, mainY, gridPoints[2], gridPoints[3])
        };

        int minIndex = 0;
        for (int i = 1; i < distances.length; i++) {
            if (distances[i] < distances[minIndex]) {
                minIndex = i;
            }
        }

        return switch (minIndex) {
            case 0 -> new Point(gridPoints[0], gridPoints[1]);
            case 1 -> new Point(gridPoints[2], gridPoints[1]);
            case 2 -> new Point(gridPoints[0], gridPoints[3]);
            default -> new Point(gridPoints[2], gridPoints[3]);
        };
    }

    private double distance(int x1, int y1, int x2, int y2) {
        return Math.hypot(x2 - x1, y2 - y1);
    }

    public void snapToWall(room movingRoom) {
        if (!snapToWallEnabled)
            return;
        int nearestHorizontalDistance = Integer.MAX_VALUE;
        int nearestVerticalDistance = Integer.MAX_VALUE;
        int horizontalSnapX = movingRoom.getBounds().x;
        int verticalSnapY = movingRoom.getBounds().y;

        Rectangle movingBounds = movingRoom.getBounds();

        for (room placedRoom : placedRooms) {
            if (placedRoom == movingRoom)
                continue;

            Rectangle placedBounds = placedRoom.getBounds();

            int dxLeft = Math.abs(movingBounds.x - (placedBounds.x + placedBounds.width));
            int dxRight = Math.abs((movingBounds.x + movingBounds.width) - placedBounds.x);

            int dyTop = Math.abs(movingBounds.y - (placedBounds.y + placedBounds.height));
            int dyBottom = Math.abs((movingBounds.y + movingBounds.height) - placedBounds.y);

            if (dxLeft <= snapThreshold && dxLeft < nearestHorizontalDistance
                    && hasVerticalOverlap(movingBounds, placedBounds)) {
                nearestHorizontalDistance = dxLeft;
                horizontalSnapX = placedBounds.x + placedBounds.width;
            } else if (dxRight <= snapThreshold && dxRight < nearestHorizontalDistance
                    && hasVerticalOverlap(movingBounds, placedBounds)) {
                nearestHorizontalDistance = dxRight;
                horizontalSnapX = placedBounds.x - movingBounds.width;
            }

            if (dyTop <= snapThreshold && dyTop < nearestVerticalDistance
                    && hasHorizontalOverlap(movingBounds, placedBounds)) {
                nearestVerticalDistance = dyTop;
                verticalSnapY = placedBounds.y + placedBounds.height;
            } else if (dyBottom <= snapThreshold && dyBottom < nearestVerticalDistance
                    && hasHorizontalOverlap(movingBounds, placedBounds)) {
                nearestVerticalDistance = dyBottom;
                verticalSnapY = placedBounds.y - movingBounds.height;
            }
        }

        if (nearestHorizontalDistance <= snapThreshold) {
            movingRoom.setLocation(horizontalSnapX, movingBounds.y);
            movingBounds = movingRoom.getBounds();
        }
        if (nearestVerticalDistance <= snapThreshold) {
            movingRoom.setLocation(movingBounds.x, verticalSnapY);
        }

    }

    private boolean hasVerticalOverlap(Rectangle r1, Rectangle r2) {
        return r1.y < r2.y + r2.height && r1.y + r1.height > r2.y;
    }

    private boolean hasHorizontalOverlap(Rectangle r1, Rectangle r2) {
        return r1.x < r2.x + r2.width && r1.x + r1.width > r2.x;
    }
}

import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.awt.*;

public class Frame extends JFrame {
    private ControlPanel cp = new ControlPanel();
    private CanvasPanel cap = new CanvasPanel();
    private JPopupMenu popupMenu = new JPopupMenu();
    private JPopupMenu savemenu = new JPopupMenu();
    private JPopupMenu rightClickMenu = new JPopupMenu();
    private AddRoomType[] roomOptions = { new AddRoomType(), new AddRoomType(), new AddRoomType(), new AddRoomType(),new AddRoomType() };

    Frame() {
        
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setExtendedState(MAXIMIZED_BOTH);
        this.setTitle("2DFloorPlanner");
        this.setIconImage(new ImageIcon("lib/blueprint2.png").getImage());
        this.getContentPane().setBackground(new Color(158, 161, 163));
        this.setLayout(null);
        this.add(cp);
        this.add(cap);

        // Re-center window
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                setLocation(-7, -7);
            }
        });

        // Popup menu for snap options
        setupPopupMenu();

        // Setup mechanism for control panel
        setupMechanish(this);

        // Room adding functionality
        for (int i = 0; i < roomOptions.length; i++) {
            final int roomType = i + 1;
            roomOptions[i].b1.addActionListener(e -> addRoom(roomType));
            roomOptions[i].b2.addActionListener(e -> showAlignmentPopup(roomType));
        }

        // Snap buttons functionality
        setupSnapButtons();

        // New button functionality
        cp.mb.b1.addActionListener(e -> {
            cap.clear();
            room.clear();
        });

        // Right-click popup menu setup
        setupRightClickMenu();

        // Delete key functionality
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println(e.getKeyCode());
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    deleteSelectedRoom();
                }
            }
        });

        this.setVisible(true);
    }

    // Method to delete the selected room
    public void deleteSelectedRoom() {
        if (room.getSelectedRoom() != null) {
            cap.placedRooms.remove(room.getSelectedRoom());
            cap.remove(room.getSelectedRoom());
            cap.repaint();
            room.clear();
        }
    }

    // Setup popup menu for snap options
    private void setupPopupMenu() {
        popupMenu.setLayout(new GridLayout(2, 1, 0, 0));
        popupMenu.add(cp.s1);
        popupMenu.add(cp.s2);
        popupMenu.setBorder(BorderFactory.createEmptyBorder());

        cp.mb.b4.addActionListener(e -> popupMenu.show(cp.mb.b4, 0, this.getHeight() / 30));

        savemenu.setLayout(new GridLayout(2, 1, 0, 0));
        savemenu.add(cp.saveButton);
        savemenu.add(cp.pngButton);
        savemenu.setBorder(BorderFactory.createEmptyBorder());
        cp.mb.b3.addActionListener(e -> savemenu.show(cp.mb.b3, 0, this.getHeight() / 30));
    }

    // Add a room based on its type
    public void addRoom(int roomType) {
        if(cp.width()<cap.getWidth()-15&&cp.height()<cap.getHeight()-38){
        room newRoom = new room(roomType, cp.width(), cp.height());
        cap.addRoom(newRoom);}
        else{
            JOptionPane.showMessageDialog(null, "Room width and height cannot be larger than canvas");
            return;
        }
    }

    // Add a room with alignment values
    public void addRoom(int roomType, int x, int y) {
        room newRoom = new room(roomType, cp.width(), cp.height());
        cap.addRoom(newRoom, x, y);
    }

    // Show alignment popup for adding room with custom alignment
    private void showAlignmentPopup(int roomType) {
        AlignmentPopup alignmentPopup = new AlignmentPopup(this);
        if (alignmentPopup.getAlignmentValues() == null)
            return;
        int[] values = alignmentPopup.getAlignmentValues();
        addRoom(roomType, values[0], values[1]);
    }

    // Setup snap buttons functionality
    private void setupSnapButtons() {
        cp.s1.addActionListener(e -> {
            if (cap.snapToWallEnabled) {
                cap.toggleSnapToWall();
            }
            if (!cap.gridEnabled) {
                cap.toggleGrid();
                cp.hp.c2.toggleV();
            }
            cap.toggleSnapToGrid();
        });

        cp.s2.addActionListener(e -> {
            if (cap.snapToGrid) {
                cap.toggleSnapToGrid();
            }
            cap.toggleSnapToWall();
        });

        cp.saveButton.addActionListener(e -> cap.saveProject());
        cp.pngButton.addActionListener(e->{ JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Canvas as PNG");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PNG Image", "png"));
            int userSelection = fileChooser.showSaveDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                if (!fileToSave.getAbsolutePath().endsWith(".png")) {
                    fileToSave = new File(fileToSave.getAbsolutePath() + ".png");
                }
                cap.savepng(fileToSave);
            }});
        cp.mb.b2.addActionListener(e -> cap.loadProject());
    }

    // Setup right-click popup menu
    private void setupRightClickMenu() {
        // Create popup menu options
        JMenuItem deleteRoomItem = new JMenuItem("Delete Selected Room");
        JMenuItem clearCanvasItem = new JMenuItem("Clear Canvas");

        // Add functionality to menu items
        deleteRoomItem.addActionListener(e -> deleteSelectedRoom());
        clearCanvasItem.addActionListener(e -> {
            cap.clear();
            room.clear();
        });

        // Add items to right-click menu
        if(!(room.getSelectedRoom()==null))
        rightClickMenu.add(deleteRoomItem);
        rightClickMenu.add(clearCanvasItem);

        // Add mouse listener for right-click event
        cap.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    // Get absolute screen width and mouse position
                    int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
                    int mouseX = e.getXOnScreen(); // Absolute X coordinate of the mouse

                    // Check if the right-click happened in the right 75% of the screen
                    if (mouseX > screenWidth * 0.25) {
                        if (rightClickMenu.isVisible()) {
                            rightClickMenu.setVisible(false); // Hide existing popup
                        }

                        // Show the right-click menu at the location of the mouse click
                        SwingUtilities.invokeLater(() -> {
                            if(!(room.getSelectedRoom()==null))
                            rightClickMenu.add(deleteRoomItem);
                            else
                            rightClickMenu.remove(deleteRoomItem);
                            rightClickMenu.show(cap, e.getX(), e.getY());
                        });
                    }
                }
            }
        });
    }

    public static void main(String[] args) {
        Frame x = new Frame();
        int x1 = x.getWidth();
        int y1 = x.getHeight();

        // ControlPanel layout setup
        x.cp.setBounds(0, 0, x1 / 4, y1);
        x.cap.setBounds(x1 / 4, 0, 3 * x1 / 4, y1);
        x.cap.setMinimumSize(new Dimension(3 * x1 / 4, y1));
        x.cap.setPreferredSize(new Dimension(3 * x1 / 4, y1));
        x.setMinimumSize(new Dimension(x1, y1));


        // Menu bar and panels
        x.cp.mb.setBounds(0, 0, x1 / 4, y1 / 30);
        x.cp.mp.setBounds(0, y1 / 30, x1 / 4, 26 * y1 / 30);
        x.cp.hp.setBounds(0, 27 * y1 / 30, x1 / 4, y1 / 15);

        // Trash panel
        x.cap.trashdimension(5 * x1 / 8, -25, x1 / 8, x1 / 8);
        x.cap.t.p.setBounds(0, 25, x1 / 8, x1 / 8 - 25);

        // Status panel
        x.cap.statusPanel.setBounds(5 * x1 / 8, 8 * y1 / 9, x1 / 8, x1 / 8);

        // Snap popup menu size
        x.popupMenu.setPopupSize(x1 / 16 + 1, y1 / 18);
        x.savemenu.setPopupSize(x1 / 16 + 1, y1 / 18);

        // Room option popup sizes
        for (AddRoomType option : x.roomOptions) {
            option.setPopupSize(x1 / 8 - 20, 2 * y1 / 30);
        }

        // Room and furniture adding
        setupRoomAdditionButtons(x, x1, y1);
    }

    // setup control panel button appearing mechanism
    private static void setupMechanish(Frame x) {
        x.cp.mp.b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!x.cp.mp.m1)
                    x.cp.mp.fun1();
                else {
                    x.cp.mp.fun4();
                }
            }
        });

        x.cp.mp.b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!x.cp.mp.m2) {
                    x.cp.mp.fun2();
                } else {
                    x.cp.mp.fun4();
                }
            }
        });

        x.cp.mp.b3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!x.cp.mp.m3) {
                    x.cp.mp.fun3();
                } else {
                    x.cp.mp.fun4();
                }
            }
        });

    }

    // Setup room addition buttons with listeners
    private static void setupRoomAdditionButtons(Frame x, int x1, int y1) {
        x.cp.mp.r1.addActionListener(e -> addRoomWithPopup(x, x.cp.mp.r1, 1, x1, y1));
        x.cp.mp.r2.addActionListener(e -> addRoomWithPopup(x, x.cp.mp.r2, 2, x1, y1));
        x.cp.mp.r3.addActionListener(e -> addRoomWithPopup(x, x.cp.mp.r3, 3, x1, y1));
        x.cp.mp.r4.addActionListener(e -> addRoomWithPopup(x, x.cp.mp.r4, 4, x1, y1));
        x.cp.mp.r5.addActionListener(e -> addRoomWithPopup(x, x.cp.mp.r5, 5, x1, y1));

        // Furniture adding
        x.cp.mp.f1.addActionListener(e -> addFurniture(1));
        x.cp.mp.f2.addActionListener(e -> addFurniture(2));
        x.cp.mp.f3.addActionListener(e -> addFurniture(3));
        x.cp.mp.f4.addActionListener(e -> addFurniture(4));
        x.cp.mp.f5.addActionListener(e -> addFurniture(5));
        x.cp.mp.f6.addActionListener(e -> addFurniture(6));
        x.cp.mp.f7.addActionListener(e -> addFurniture(7));
        x.cp.mp.f8.addActionListener(e -> addFurniture(8));
        x.cp.mp.f9.addActionListener(e -> addFurniture(9));
        x.cp.mp.f10.addActionListener(e -> addFurniture(10));

        // Help panel buttons
        x.cp.hp.c3.addActionListener(e -> {
            x.cap.toggletrash();
        });

        x.cp.hp.c2.addActionListener(e -> {
            x.cap.toggleGrid();
        });
    }

    // Helper method for adding rooms with popup
    private static void addRoomWithPopup(Frame x, ButtonC button, int roomType, int x1, int y1) {
        if (x.cp.height() == -1 || x.cp.width() == -1) {
            JOptionPane.showMessageDialog(null, "Enter dimensions");
        } else if (room.getSelectedRoom() == null) {
            x.addRoom(roomType);
        } else {
            x.roomOptions[roomType - 1].show(button, x1 / 8 + 20, 0);
        }
    }

    // add furniture
    private static void addFurniture(int Furnituretype) {
        if (room.getSelectedRoom() != null) {
            objects obj = new objects(Furnituretype);
            room.getSelectedRoom().addobject(obj);
        } else {
            JOptionPane.showMessageDialog(null, "First select a room to add Furniture");
        }
    }
}

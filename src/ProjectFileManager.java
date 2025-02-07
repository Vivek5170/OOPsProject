import java.io.*;
import java.util.List;

import javax.swing.JOptionPane;

public class ProjectFileManager {

    public static void saveProject(File file, List<room> rooms) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            if(!(room.getSelectedRoom()==null))
            room.getSelectedRoom().deselect();
            objects.deselect1();
            oos.writeObject(rooms);
            System.out.println("Project saved successfully to " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error saving project: " + e.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
    public static List<room> loadProject(File file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<room>) ois.readObject();
        }
    }
}


import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

class TempInputs extends JPanel {
    private JLabel label;
    private JTextField textField;

    TempInputs(String labelText) {
        label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 18));
        label.setForeground(Color.white);
        textField = new JTextField("100");
        textField.setForeground(Color.white); // Set text color to white
        textField.setFont(new Font("Arial", Font.PLAIN, 18));
        textField.setBackground(new Color(15, 15, 15));
        textField.setBorder(BorderFactory.createLineBorder(Color.darkGray, 2, true));
        textField.setCaretColor(Color.GRAY);
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new PositiveIntegerFilter());
        this.setBackground(null);
        this.setBorder(BorderFactory.createEmptyBorder());
        this.setLayout(new BorderLayout(10, 0));
        this.add(label, BorderLayout.WEST);
        this.add(textField, BorderLayout.CENTER);
    }

    public int val() {
        int x;
        if (!textField.getText().isEmpty())
            x = Integer.parseInt(textField.getText());
        else
            x = -1;
        return x;
    }
}

public class inputs extends JPanel {
    private TempInputs heightInput = new TempInputs("Height : ");
    private TempInputs widthInput = new TempInputs("Width : ");

    inputs() {
        this.setBackground(Color.black);
        this.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        this.setLayout(new GridLayout(1, 2, 10, 30));
        this.add(widthInput);
        this.add(heightInput);
    }

    public int height() {
        return heightInput.val();
    }

    public int width() {
        return widthInput.val();
    }
}

class PositiveIntegerFilter extends DocumentFilter {

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
            throws BadLocationException {
        if (isValidInput(string)) {
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
            throws BadLocationException {
        if (isValidInput(text)) {
            super.replace(fb, offset, length, text, attrs);
        }
    }

    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        super.remove(fb, offset, length);
    }

    private boolean isValidInput(String text) {
        // Allow only digits and ensure the input doesn't start with 0 unless it's the
        // number 0 itself
        return text.matches("\\d*") && !text.startsWith("0") || text.equals("0");
    }
}

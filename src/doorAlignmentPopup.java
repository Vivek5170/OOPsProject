import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class doorAlignmentPopup extends JDialog {
    private JRadioButton northButton, southButton, eastButton, westButton;
    private ButtonGroup directionGroup;
    private Integer direction = null;

    public doorAlignmentPopup(JFrame parentFrame) {
        super(parentFrame, "Select Door Alignment", true);
        setSize(300, 150);
        setLayout(new GridLayout(3, 1));
        setLocationRelativeTo(parentFrame);
        getContentPane().setBackground(Color.gray);

        JLabel text = new JLabel("Select Door Alignment", JLabel.CENTER);
        text.setFont(new Font("Arial", Font.PLAIN, 20));
        text.setForeground(Color.white);
        text.setOpaque(true);
        text.setBackground(Color.gray);
        add(text);

        JPanel directionPanel = new JPanel();
        directionPanel.setLayout(new GridLayout(1, 4));
        directionPanel.setBackground(Color.gray);

        northButton = createRadioButton("North");
        southButton = createRadioButton("South");
        eastButton = createRadioButton("East");
        westButton = createRadioButton("West");

        directionGroup = new ButtonGroup();
        directionGroup.add(northButton);
        directionGroup.add(southButton);
        directionGroup.add(eastButton);
        directionGroup.add(westButton);

        directionPanel.add(northButton);
        directionPanel.add(southButton);
        directionPanel.add(eastButton);
        directionPanel.add(westButton);
        add(directionPanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2));
        buttonPanel.setBackground(Color.gray);

        ButtonM submitButton = new ButtonM("Submit");
        ButtonM cancelButton = new ButtonM("Cancel");

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateSelection()) {
                    dispose(); // Close dialog after valid selection
                } else {
                    JOptionPane.showMessageDialog(doorAlignmentPopup.this,
                            "Please select a direction!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                direction = null;
                dispose();
            }
        });

        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel);

        setVisible(true);
    }

    private JRadioButton createRadioButton(String label) {
        JRadioButton radioButton = new JRadioButton(label);
        radioButton.setBackground(Color.gray);
        radioButton.setForeground(Color.white);
        radioButton.setBorderPainted(false);
        radioButton.setOpaque(true);
        return radioButton;
    }

    private boolean validateSelection() {
        if (northButton.isSelected()) {
            direction = 1;
        } else if (southButton.isSelected()) {
            direction = 2;
        } else if (eastButton.isSelected()) {
            direction = 3;
        } else if (westButton.isSelected()) {
            direction = 4;
        } else {
            direction =-1;
            return false;
        }

        return true;
    }

    public Integer getDirection() {
        return direction;
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AlignmentPopup extends JDialog {
    private JRadioButton northButton, southButton, eastButton, westButton;
    private ButtonGroup directionGroup;
    private JPanel alignmentPanel;
    private JRadioButton leftAlign, centerAlign, rightAlign, topAlign, bottomAlign;
    private Integer direction = null;
    private Integer alignment = null;

    public AlignmentPopup(JFrame parentFrame) {
        super(parentFrame, "Select Alignment Options", true);
        setSize(300, 200);
        setLayout(new GridLayout(4, 1));
        setLocationRelativeTo(parentFrame);
        getContentPane().setBackground(Color.gray);

        JLabel text = new JLabel("Select Required Alignments", JLabel.CENTER);
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

        alignmentPanel = new JPanel();
        alignmentPanel.setLayout(new GridLayout(1, 3));
        alignmentPanel.setBackground(Color.gray);
        add(alignmentPanel);

        ActionListener directionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateAlignmentOptions();
            }
        };

        northButton.addActionListener(directionListener);
        southButton.addActionListener(directionListener);
        eastButton.addActionListener(directionListener);
        westButton.addActionListener(directionListener);

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
                    JOptionPane.showMessageDialog(AlignmentPopup.this,
                            "Please select both direction and alignment!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                direction = null;
                alignment = null;
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

    private void updateAlignmentOptions() {
        alignmentPanel.removeAll();
        ButtonGroup alignmentGroup = new ButtonGroup();

        if (northButton.isSelected() || southButton.isSelected()) {
            leftAlign = createRadioButton("Left");
            centerAlign = createRadioButton("Center");
            rightAlign = createRadioButton("Right");
            alignmentGroup.add(leftAlign);
            alignmentGroup.add(centerAlign);
            alignmentGroup.add(rightAlign);
            alignmentPanel.add(leftAlign);
            alignmentPanel.add(centerAlign);
            alignmentPanel.add(rightAlign);
        } else if (eastButton.isSelected() || westButton.isSelected()) {
            topAlign = createRadioButton("Top");
            centerAlign = createRadioButton("Center");
            bottomAlign = createRadioButton("Bottom");
            alignmentGroup.add(topAlign);
            alignmentGroup.add(centerAlign);
            alignmentGroup.add(bottomAlign);
            alignmentPanel.add(topAlign);
            alignmentPanel.add(centerAlign);
            alignmentPanel.add(bottomAlign);
        }

        alignmentPanel.revalidate();
        alignmentPanel.repaint();
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
            return false;
        }

        if (leftAlign != null && leftAlign.isSelected()) {
            alignment = 1;
        } else if (centerAlign != null && centerAlign.isSelected()) {
            alignment = 2;
        } else if (rightAlign != null && rightAlign.isSelected()) {
            alignment = 3;
        } else if (topAlign != null && topAlign.isSelected()) {
            alignment = 1;
        } else if (bottomAlign != null && bottomAlign.isSelected()) {
            alignment = 3;
        } else {
            return false;
        }

        return true;
    }

    public int[] getAlignmentValues() {
        if (direction != null && alignment != null) {
            return new int[]{direction, alignment};
        }
        return null;
    }
}

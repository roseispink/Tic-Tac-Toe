package com.src;

import javax.swing.*;
import java.awt.*;

public class Square extends JLabel {
    JLabel label = new JLabel();

    public Square() {
        this.setBackground(Color.yellow);
        this.setLayout(new GridBagLayout());
        this.label.setFont(new Font("Arial", 1, 40));
        this.add(this.label);
    }

    public void setText(char text) {
        this.label.setForeground(text == 'X' ? Color.BLUE : Color.GREEN);
        this.label.setText(text + "");
    }
}

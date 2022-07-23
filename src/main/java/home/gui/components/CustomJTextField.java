package home.gui.components;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JTextField;

@SuppressWarnings("serial")
public final class CustomJTextField extends JTextField {

    private static final String FONT_NAME = "Courier";
    private static final int FONT_SIZE = 14;

    private CustomJTextField() {
    }

    public static JTextField create(int columns) {
        var textField = new CustomJTextField();
        textField.setColumns(columns);
        textField.setFont(new Font(FONT_NAME, Font.BOLD, FONT_SIZE));
        textField.setForeground(Color.BLACK);
        return textField;
    }
}

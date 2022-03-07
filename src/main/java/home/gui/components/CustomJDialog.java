package home.gui.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.KeyStroke;

@SuppressWarnings("serial")
public class CustomJDialog extends JDialog {

    private static final int GAP_BETWEEN_COMPONENTS = 2;

    private static final String CLOSE_DIALOG_ACTION = "closeDialogAction";

    private final String title;
    private final int widht;
    private final int height;

    public CustomJDialog(String title, int widht, int height) {
        this.title = title;
        this.widht = widht;
        this.height = height;

    }

    protected void init() {
        setTitle(title);
        setSize(widht, height);
        setMinimumSize(new Dimension(widht, height));
        setModal(true);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(GAP_BETWEEN_COMPONENTS, GAP_BETWEEN_COMPONENTS));

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        addHotKeyForClose();
    }

    private void addHotKeyForClose() {
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), CLOSE_DIALOG_ACTION);
        getRootPane().getActionMap().put(CLOSE_DIALOG_ACTION, new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
}

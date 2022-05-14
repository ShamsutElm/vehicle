package home.gui.components.dialog;

import java.awt.BorderLayout;
import java.util.Date;
import java.util.function.Predicate;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdesktop.swingx.JXDatePicker;

import home.Storage;
import home.gui.Gui;
import home.gui.IGuiConsts;
import home.gui.components.CustomJButton;
import home.gui.components.CustomJDialog;
import home.gui.components.CustomJLabel;
import home.gui.components.CustomJPanel;
import home.gui.components.CustomJPanel.PanelType;
import home.gui.components.CustomJTextField;
import home.gui.components.CustomJXDatePicker;
import home.models.AbstractVehicle;

@SuppressWarnings("serial")
public abstract class AbstractDialog extends CustomJDialog {

    private static final int TEXT_FIELD_COLUMN_NUMBERS = 9;

    private static Predicate<String> IS_FILLED = str -> str != null && !str.isBlank();

    private JLabel lblColor;
    private JLabel lblNumber;
    private JLabel lblDate;

    private JTextField tfColor;
    private JTextField tfNumber;
    private JXDatePicker tfDate;

    private JButton btnOk;
    private JButton btnCancel;

    protected JPanel pnlTextFields;
    private JPanel pnlButtons;

    protected AbstractVehicle dataObj;
    protected boolean isNewDataObj;
    protected int tblRowOfSelectedDataObj;

    public AbstractDialog(String title, int widht, int height,
            AbstractVehicle dataObj, int tblRowOfSelectedDataObj) {
        super(title, widht, height);
        this.tblRowOfSelectedDataObj = tblRowOfSelectedDataObj;
        if (dataObj != null) {
            this.dataObj = dataObj;
            isNewDataObj = false;
        } else {
            isNewDataObj = true;
        }
    }

    public void buildDialog() {
        init();

        if (isNewDataObj) {
            createDataObj();
        }

        createDataComponents();
        createButtons();
        createPanels();
        createDialog();
    }

    protected abstract void createDataObj();

    protected void createDataComponents() {
        lblColor = CustomJLabel.create(IGuiConsts.COLOR);
        lblNumber = CustomJLabel.create(IGuiConsts.NUMBER);
        lblDate = CustomJLabel.create(IGuiConsts.DATE);

        tfColor = CustomJTextField.create(TEXT_FIELD_COLUMN_NUMBERS);
        tfNumber = CustomJTextField.create(TEXT_FIELD_COLUMN_NUMBERS);

        tfDate = CustomJXDatePicker.create(new Date());

        if (!isNewDataObj) {
            tfColor.setText(dataObj.getColor());
            tfNumber.setText(dataObj.getNumber());
            tfDate.setDate(new Date(dataObj.getDateTime()));
        }
    }

    private void createButtons() {
        btnOk = CustomJButton.create(IGuiConsts.OK);
        btnOk.addActionListener(actionEvent -> {
            fillDataObj();
            if (isObjFilled()) {
                Storage.getInstance().updateStorage(dataObj, tblRowOfSelectedDataObj);
                Gui.getInstance().refreshTable();
                dispose();
            }
        });

        btnCancel = CustomJButton.create(IGuiConsts.CANCEL);
        btnCancel.addActionListener(actionEvent -> dispose());
    }

    protected void createPanels() {
        pnlTextFields = CustomJPanel.create(PanelType.DIALOG_TEXT_FIELDS_PANEL);
        pnlTextFields.add(lblColor);
        pnlTextFields.add(tfColor);
        pnlTextFields.add(lblNumber);
        pnlTextFields.add(tfNumber);
        pnlTextFields.add(lblDate);
        pnlTextFields.add(tfDate);

        pnlButtons = CustomJPanel.create(PanelType.DIALOB_BUTTON_PANEL);
        pnlButtons.add(btnOk);
        pnlButtons.add(btnCancel);
    }

    private void createDialog() {
        getContentPane().add(pnlTextFields, BorderLayout.CENTER);
        getContentPane().add(pnlButtons, BorderLayout.SOUTH);
        setVisible(true);
    }

    protected void fillDataObj() {
        dataObj.setColor(tfColor.getText());
        dataObj.setNumber(tfNumber.getText());
        dataObj.setDateTime(tfDate.getDate().getTime());
    }

    private boolean isObjFilled() {
        return IS_FILLED.test(dataObj.getColor()) && IS_FILLED.test(dataObj.getNumber());
    }
}

package home.gui.components.dialog;

import javax.swing.JCheckBox;

import home.gui.IGuiConsts;
import home.models.AbstractVehicle;
import home.models.Motorcycle;
import home.models.VehicleType;

@SuppressWarnings("serial")
public class DialogMoto extends AbstractDialog {

    private JCheckBox chkCradle;

    public DialogMoto(int widht, int height,
            AbstractVehicle dataObj, int tblRowOfSelectedDataObj) {
        super(VehicleType.MOTORCYCLE.name(), widht, height, dataObj, tblRowOfSelectedDataObj);
    }

    @Override
    protected void createDataComponents() {
        super.createDataComponents();
        chkCradle = new JCheckBox(IGuiConsts.HAS_CRADLE);

        if (!isNewDataObj) {
            chkCradle.setSelected(((Motorcycle) dataObj).hasCradle());
        }
    }

    @Override
    protected void createPanels() {
        super.createPanels();
        pnlTextFields.add(chkCradle);
    }

    @Override
    protected void createDataObj() {
        dataObj = new Motorcycle();
    }

    @Override
    protected void fillDataObj() {
        super.fillDataObj();
        ((Motorcycle) dataObj).setHasCradle(chkCradle.isSelected());
    }

}

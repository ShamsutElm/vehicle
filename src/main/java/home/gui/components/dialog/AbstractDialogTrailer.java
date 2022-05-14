package home.gui.components.dialog;

import javax.swing.JCheckBox;

import home.gui.IGuiConsts;
import home.models.AbstractVehicle;
import home.models.AbstractVehicleWithTrailer;

@SuppressWarnings("serial")
public abstract class AbstractDialogTrailer extends AbstractDialog {

    private JCheckBox chkHasTrailer;

    public AbstractDialogTrailer(String title, int widht, int height,
            AbstractVehicle dataObj, int tblRowOfSelectedDataObj) {
        super(title, widht, height, dataObj, tblRowOfSelectedDataObj);
    }

    @Override
    protected void createDataComponents() {
        super.createDataComponents();
        chkHasTrailer = new JCheckBox(IGuiConsts.HAS_TRAILER);

        if (!isNewDataObj) {
            chkHasTrailer.setSelected(((AbstractVehicleWithTrailer) dataObj).hasTrailer());
        }
    }

    @Override
    protected void createPanels() {
        super.createPanels();
        pnlTextFields.add(chkHasTrailer);
    }

    @Override
    protected void fillDataObj() {
        super.fillDataObj();
        ((AbstractVehicleWithTrailer) dataObj).setHasTrailer(chkHasTrailer.isSelected());
    }
}

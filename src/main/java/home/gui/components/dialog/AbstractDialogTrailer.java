package home.gui.components.dialog;

import javax.swing.JCheckBox;

import home.gui.IGuiConsts;
import home.models.AbstractVehicle;
import home.models.AbstractVehicleWithTrailer;

@SuppressWarnings("serial")
abstract sealed class AbstractDialogTrailer extends AbstractDialog permits DialogCar, DialogTruck {

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

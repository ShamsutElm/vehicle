package home.gui.components.dialog;

import javax.swing.JCheckBox;

import home.gui.IGuiConsts;
import home.models.AbstractVehicle;
import home.models.Car;
import home.models.VehicleType;

@SuppressWarnings("serial")
public final class DialogCar extends AbstractDialogTrailer {

    private JCheckBox chkPassengers;

    public DialogCar(int widht, int height,
            AbstractVehicle dataObj, int tblRowOfSelectedDataObj) {
        super(VehicleType.CAR.name(), widht, height, dataObj, tblRowOfSelectedDataObj);
    }

    @Override
    protected void createDataComponents() {
        super.createDataComponents();
        chkPassengers = new JCheckBox(IGuiConsts.TRANSPORTS_PASSENGERS);

        if (!isNewDataObj) {
            chkPassengers.setSelected(((Car) dataObj).isTransportsPassengers());
        }
    }

    @Override
    protected void createPanels() {
        super.createPanels();
        pnlTextFields.add(chkPassengers);
    }

    @Override
    protected void createDataObj() {
        dataObj = new Car();
    }

    @Override
    protected void fillDataObj() {
        super.fillDataObj();
        ((Car) dataObj).setTransportsPassengers(chkPassengers.isSelected());
    }
}

package home.gui;

import java.util.List;

import home.Storage;
import home.models.AbstractVehicle;

public final class DataActionInGui {

    public static void init(List<AbstractVehicle> dataObjs) {
        Storage.INSTANCE.initDataObjs(dataObjs);
        Gui.INSTANCE.refreshTable();
    }

    public static void add(List<AbstractVehicle> dataObjs) {
        Storage.INSTANCE.addDataObjs(dataObjs);
        Gui.INSTANCE.refreshTable();
    }

    public static void update(AbstractVehicle dataObj, int tblRowOfSelectedDataObj) {
        Storage.INSTANCE.updateDataObj(dataObj, tblRowOfSelectedDataObj);
        Gui.INSTANCE.refreshTable();
    }

    public static void delete(List<AbstractVehicle> objsMarkedForDelete) {
        Storage.INSTANCE.deleteDataObjs(objsMarkedForDelete);
        Gui.INSTANCE.refreshTable();
    }

    private DataActionInGui() {
    }
}

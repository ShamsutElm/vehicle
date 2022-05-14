package home;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import home.gui.Gui;
import home.models.AbstractVehicle;

public class Storage {

    public static final int NO_ROW_IS_SELECTED = -1;

    private final List<AbstractVehicle> dataObjsStorage = new ArrayList<>();
    private final Set<Long> dataObjIdsForDel = new HashSet<>();

    private static Storage instance;

    private Storage() {
    }

    public static Storage getInstance() {
        if (instance == null) {
            instance = new Storage();
        }
        return instance;
    }

    public void refresh(List<AbstractVehicle> dataObjs) {
        dataObjsStorage.clear();
        dataObjsStorage.addAll(dataObjs);
        Gui.getInstance().refreshTable();
    }

    public List<AbstractVehicle> getAll() {
        return dataObjsStorage;
    }

    public AbstractVehicle get(int row) {
        return dataObjsStorage.get(row);
    }

    public Long[] getIdsForDel() {
        return dataObjIdsForDel.stream().map(id -> Long.valueOf(id)).toArray(Long[]::new);
    }

    public void updateStorage(AbstractVehicle dataObj, int tblRowOfSelectedDataObj) {
        if (NO_ROW_IS_SELECTED == tblRowOfSelectedDataObj) {
            dataObjsStorage.add(dataObj);
        } else {
            dataObjsStorage.set(tblRowOfSelectedDataObj, dataObj);
        }
    }

    public void deleteObjects(List<AbstractVehicle> objsMarkedForDel) {
        for (AbstractVehicle objForDel : objsMarkedForDel) {
            long idObjForDel = objForDel.getId();
            if (idObjForDel > 0) {
                dataObjIdsForDel.add(idObjForDel);
            }
        }
        dataObjsStorage.removeAll(objsMarkedForDel);
    }
}

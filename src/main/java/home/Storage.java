package home;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import home.gui.Gui;
import home.models.AbstractVehicle;

public enum Storage {

    INSTANCE;

    public static final int NO_ROW_IS_SELECTED = -1;

    private final List<AbstractVehicle> dataObjsStorage = new LinkedList<>();
    private final Set<Long> dataObjIdsForDel = new HashSet<>();
    private final Set<Long> dataObjIdsForUpdate = new HashSet<>();

    public void refresh(List<AbstractVehicle> dataObjs) {
        dataObjIdsForDel.clear();
        dataObjIdsForUpdate.clear();
        dataObjsStorage.clear();
        dataObjsStorage.addAll(dataObjs);
        Gui.INSTANCE.refreshTable();
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

    public Set<Long> getIdsForUpdate() {
        return dataObjIdsForUpdate;
    }

    public void updateStorage(AbstractVehicle dataObj, int tblRowOfSelectedDataObj) {
        if (NO_ROW_IS_SELECTED == tblRowOfSelectedDataObj) {
            dataObjsStorage.add(dataObj);
        } else {
            dataObjsStorage.set(tblRowOfSelectedDataObj, dataObj);
            dataObjIdsForUpdate.add(dataObj.getId());
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

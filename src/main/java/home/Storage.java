package home;

import java.util.ArrayList;
import java.util.List;

import home.gui.Gui;
import home.models.AbstractVehicle;

public class Storage {

    private final List<AbstractVehicle> dataObjsStorage = new ArrayList<>();

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
}

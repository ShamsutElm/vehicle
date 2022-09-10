package home.gui;

import java.lang.reflect.Constructor;

import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import home.Storage;
import home.gui.components.dialog.AbstractDialog;
import home.gui.components.dialog.DialogCar;
import home.gui.components.dialog.DialogMoto;
import home.gui.components.dialog.DialogTruck;
import home.models.AbstractVehicle;
import home.models.VehicleType;
import home.utils.LogUtils;

public final class DialogCaller {

    private static final Logger LOG = LoggerFactory.getLogger(DialogCaller.class);

    private static final int OBJ_DIALOG_WIDTH = 450;
    private static final int OBJ_DIALOG_HEIGHT = 350;

    @SuppressWarnings("unchecked")
    public static <T extends AbstractDialog> void showObjDialog(JFrame frame,
            AbstractVehicle dataObj, int tblRowOfSelectedDataObj) {
        VehicleType objtType = dataObj.getType();

        Class<T> dialogClass = switch (objtType) {
            case CAR -> (Class<T>) DialogCar.class;
            case TRUCK -> (Class<T>) DialogTruck.class;
            case MOTORCYCLE -> (Class<T>) DialogMoto.class;
        };

        showObjDialog(frame, dialogClass, dataObj, tblRowOfSelectedDataObj);
    }

    public static <T extends AbstractDialog> void showObjDialog(JFrame frame, Class<T> dialogClass) {
        showObjDialog(frame, dialogClass, null, Storage.NO_ROW_IS_SELECTED);
    }

    public static <T extends AbstractDialog> void showObjDialog(JFrame frame, Class<T> dialogClass,
            AbstractVehicle dataObj, int tblRowOfSelectedDataObj) {
        try {
            Constructor<T> constructor = dialogClass.getConstructor(
                    new Class[]{int.class, int.class, AbstractVehicle.class, int.class});
            T dialog = constructor.newInstance(
                    OBJ_DIALOG_WIDTH, OBJ_DIALOG_HEIGHT, dataObj, tblRowOfSelectedDataObj);
            dialog.buildDialog();
        } catch (Exception e) {
            LogUtils.logAndShowError(LOG, frame, "Ошибка создания диалогового окна.\n"
                    + e.getMessage(), "Ошибка диалогового окна", e);
        }
    }

    private DialogCaller() {
    }
}

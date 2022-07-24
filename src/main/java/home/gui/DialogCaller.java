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

    public static <T extends AbstractDialog> void showObjDialog(JFrame frame, Class<T> dialogClass) {
        showObjDialog(frame, dialogClass, null, Storage.NO_ROW_IS_SELECTED);
    }

    @SuppressWarnings("unchecked")
    public static <T extends AbstractDialog> void showObjDialog(JFrame frame,
            AbstractVehicle dataObj, int tblRowOfSelectedDataObj) {
        Class<T> dialogClass = null;
        VehicleType objtType = dataObj.getType();
        switch (objtType) {
            case CAR:
                dialogClass = (Class<T>) DialogCar.class;
                break;

            case TRUCK:
                dialogClass = (Class<T>) DialogTruck.class;
                break;

            case MOTORCYCLE:
                dialogClass = (Class<T>) DialogMoto.class;
                break;

            default:
                LogUtils.logAndShowError(LOG, frame, "Нет диологового окна для [" + objtType + ']',
                        "Ошибка типа диологового окна",
                        new IllegalAccessException("Ошибка типа диологового окна"));
                return;
        }
        showObjDialog(frame, dialogClass, dataObj, tblRowOfSelectedDataObj);
    }

    private DialogCaller() {
    }
}

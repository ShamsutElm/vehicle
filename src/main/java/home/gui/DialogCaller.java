package home.gui;

import java.lang.reflect.Constructor;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import home.gui.components.dialog.AbstractDialog;
import home.gui.components.dialog.DialogCar;
import home.gui.components.dialog.DialogMoto;
import home.gui.components.dialog.DialogTruck;
import home.models.AbstractVehicle;
import home.models.VehicleType;

public class DialogCaller {

    private static final Logger LOG = LoggerFactory.getLogger(DialogCaller.class);

    private static final int OBJ_DIALOG_WIDTH = 450;
    private static final int OBJ_DIALOG_HEIGHT = 350;

    public static <T extends AbstractDialog> void showObjDialog(JFrame frame, Class<T> dialogClass) {
        showObjDialog(frame, dialogClass, null);
    }

    @SuppressWarnings("unchecked")
    public static <T extends AbstractDialog> void showObjDialog(JFrame frame, AbstractVehicle dataObj) {
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
            JOptionPane.showMessageDialog(frame, "Нет диологового окна для [" + objtType + ']',
                    "Ошибка типа диологового окна", JOptionPane.ERROR_MESSAGE);
            return;
        }
        showObjDialog(frame, dialogClass, dataObj);
    }

    public static <T extends AbstractDialog> void showObjDialog(JFrame frame, Class<T> dialogClass,
            AbstractVehicle dataObj) {

        try {
            Constructor<T> constructor = dialogClass.getConstructor(
                    new Class[] { int.class, int.class, AbstractVehicle.class });
            T dialog = constructor.newInstance(
                    OBJ_DIALOG_WIDTH, OBJ_DIALOG_HEIGHT, dataObj);
            dialog.buildDialog();
        } catch (Exception e) {
            LOG.error("Dialog error", e);
            JOptionPane.showInternalMessageDialog(frame, "Ошибка создания диалогового окна.\n"
                    + e.getMessage(), "Ошибка диалогового окна", JOptionPane.ERROR_MESSAGE);
        }
    }

    private DialogCaller() {
    }
}

package home.gui;

import java.text.SimpleDateFormat;

public interface IGuiConsts {

    SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy.MM.dd | HH:mm:ss");

    // Table columns names
    String TYPE = "Type";
    String COLOR = "Color";
    String NUMBER = "Number";
    String DATE = "Date";
    String DELETION_MARK = "Deletion mark";

    // Buttons names
    String CAR = "Car";
    String TRUCK = "Truck";
    String MOTO = "Motocycle";
    String DEL = "Delete";
    String SAVE = "Save";
    String CANCEL = "Cancel";
    String HAS_TRAILER = "has trailer";
    String HAS_CRADLE = "has cradle";
    String TRANSPORTS_PASSENGERS = "transports passengers";
    String TRANSPORTS_CARGO = "transports cargo";
}

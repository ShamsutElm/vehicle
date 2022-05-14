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
    String OK = "Ok";
    String CANCEL = "Cancel";
    String HAS_TRAILER = "has trailer";
    String HAS_CRADLE = "has cradle";
    String TRANSPORTS_PASSENGERS = "transports passengers";
    String TRANSPORTS_CARGO = "transports cargo";

    // Menu names
    String FILE = "File";
    String CREATE_OR_OPEN = "Create/Open";
    String SAVE = "Save";
    String STYLE = "Style";
    String DEFAULT = "Default";
    String SYSTEM = "System";
    String HELP = "Help";
    String ABOUT = "About";

    // Some default messages and titles in menu
    String ABOUT_TITLE = "About";
    String ABOUT_TEXT = "Some think have to be here, but i'm not sure";

    // DB Label
    String CHOOSE_DB_FILE = "Choose SQLite DB file via file -> Open/Create";

}

package home.gui;

public interface IGuiConsts {

    String DATE_FORMAT = "yyyy.MM.dd | HH:mm:ss";

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
    String SAVE_AS = "Save as...";
    String STYLE = "Style";
    String HELP = "Help";
    String ABOUT = "About";

    //// Some default messages and titles in menu

    // About dialog text
    String ABOUT_TITLE = "About";
    String ABOUT_TEXT = "Test application.\nVersion: %s";

    // Save dialog text
    String SAVE_TITLE = "Save";
    String SAVE_TEXT = "Save successfully";

    // DB Label
    String CHOOSE_DB_FILE = "Choose SQLite DB file via file -> Open/Create";
}

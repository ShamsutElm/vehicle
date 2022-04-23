package home;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import home.utils.Utils;

// TODO make sigletone
public class Settings {

    public static String DB_FILE_PATH;

    public static final String DB_FILE_PATH_SETTING_NAME = "db_file_path";

    private static final String SETTINGS_FILE_NAME = "settings.properties";
    private static final Properties SETTINGS = new Properties();

    private Settings() {
    }

    public static boolean hasPathToDBFile() {
        return DB_FILE_PATH != null && !DB_FILE_PATH.isBlank();
    }

    public static void writeSettings(String name, String value) throws IOException {
        SETTINGS.setProperty(name, value);
        try (var outputStream = new FileOutputStream(SETTINGS_FILE_NAME)) {
            SETTINGS.store(outputStream, null);
        } catch (IOException e) {
            throw Utils.getNewException(e, "Error while filling the setting file");
        }
        readSettings();
    }

    public static void readSettings() throws IOException {
        try (var inputStream = new FileInputStream(getSettingsPath())) {
            SETTINGS.load(inputStream);
        } catch (IOException e) {
            throw Utils.getNewException(e, "Error while read setttings from file: "
                    + SETTINGS_FILE_NAME);
        }

        DB_FILE_PATH = SETTINGS.getProperty(DB_FILE_PATH_SETTING_NAME, Default.DB_FILE_PATH);
    }

    public static String getSettingsPath() throws IOException {
        try {
            var file = new File(SETTINGS_FILE_NAME);
            if (!file.exists()) {
                file.createNewFile();
                fillWithDefaultSettings();
            }
            return file.getAbsolutePath();
        } catch (IOException e) {
            throw Utils.getNewException(e, "Error while creating the settings file");
        }
    }

    private static void fillWithDefaultSettings() throws IOException {
        try (var outputStream = new FileOutputStream(SETTINGS_FILE_NAME)) {
            SETTINGS.setProperty(DB_FILE_PATH_SETTING_NAME, Default.DB_FILE_PATH);
            SETTINGS.store(outputStream, null);
        } catch (IOException e) {
            throw Utils.getNewException(e, "Error while fill default settings");
        }
    }

    private final class Default {
        private static final String DB_FILE_PATH = "";
    }
}

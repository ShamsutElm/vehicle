package home.db;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import home.Settings;
import home.Settings.Setting;

public class DbInitializer {

    private static final Logger LOG = LoggerFactory.getLogger(DbInitializer.class);

    private static final String CREATE_TBL_QUERY = "CREATE TABLE if NOT EXISTS vehicle ("
            + " 'id' integer PRIMARY KEY AUTOINCREMENT,"
            + " 'type' text,"
            + " 'color' text,"
            + " 'number' text,"
            + " 'is_transports_cargo' integer,"
            + " 'is_transports_passengers' integer,"
            + " 'has_trailer' integer,"
            + " 'has_cradle' integer,"
            + " 'date_time' integer);";

    public static void createDBFileIfNotExists(File file) throws IOException {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            Settings.writeSetting(Setting.DB_FILE_PATH, file.getAbsolutePath());
        } catch (IOException e) {
            LOG.error("Error while creating the DB file", e);
            throw e;
        }
    }

    public static void createTableIfNotExists() throws SQLException {
        try (var connection = Connector.getConnetionToSQLite();
             var stmt = connection.createStatement()) {
            stmt.execute(CREATE_TBL_QUERY);
        }
    }

    private DbInitializer() {
    }
}

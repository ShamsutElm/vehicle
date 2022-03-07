package home.db;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbCreator {

    private static final Logger LOG = LoggerFactory.getLogger(DbCreator.class);

    private static final String DB_FILE_PATH = "database.db";

    public static String dbFileAbsolutePath() throws IOException {
        try {
            var file = new File(DB_FILE_PATH);
            if (!file.exists()) {
                file.createNewFile();
            }
            return file.getAbsolutePath();
        } catch (IOException e) {
            LOG.error("Ошибка при создании файла базы данных", e);
            throw e;
        }
    }

    private DbCreator() {
    }
}

package home.db;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import home.Settings;
import home.Storage;
import home.db.dao.DaoSQLite;
import home.models.AbstractVehicle;
import home.models.Car;

public class DbTest {

    private static final String DB_FILE_NAME = "database.db";
    private static final String TMP = "tmp_";

    private File generetedDbFile;

    @BeforeEach
    public void initializeTemporaryDbFile() {
        deletePreviousTempFile();
        try {
            generetedDbFile = File.createTempFile(TMP, DB_FILE_NAME);
            DbInitializer.createDBFileIfNotExists(generetedDbFile);
            DbInitializer.createTableIfNotExists();
        } catch (IOException e) {
            fail("Error while create DB file.", e);
        } catch (SQLException e) {
            fail("Error while create table in DB.", e);
        }
    }

    private void deletePreviousTempFile() {
        File tmpDir = new File(System.getProperty("java.io.tmpdir"));
        for (File file : tmpDir.listFiles()) {
            String tmpFileName = file.getName();
            if (file.isFile() && tmpFileName.startsWith(TMP)
                    && tmpFileName.contains(DB_FILE_NAME)) {
                file.delete();
            }
        }
    }

    @Test
    public void createDbFileTest() {
        try (var sampleDbFileStream = getClass().getResourceAsStream(DB_FILE_NAME)) {
            byte[] sampleDbFileBytes = sampleDbFileStream.readAllBytes();
            byte[] generetedDbFileBytes = Files.readAllBytes(generetedDbFile.toPath());
            assertNotNull(sampleDbFileBytes, "Sample DB file is null.");
            assertNotNull(generetedDbFileBytes, "Genereted DB file is null.");
            assertArrayEquals(sampleDbFileBytes, generetedDbFileBytes);
        } catch (IOException e) {
            fail("Errors while read file", e);
        }
    }

    @Test
    public void createReadDataTest() {
        try {
            // There os no id yet in new data object. Id will be set in database.
            // That is why we will set it later, from read database object.
            // It's need for correct comparison.
            var cretedDataObj = new Car();
            cretedDataObj.setColor("Green");
            cretedDataObj.setNumber("17454");
            cretedDataObj.setDateTime(System.currentTimeMillis());
            cretedDataObj.setTransportsPassengers(true);

            Storage.INSTANCE.updateStorage(cretedDataObj, Storage.NO_ROW_IS_SELECTED);
            DaoSQLite.getInstance().saveAllChanges();

            long id = 1;
            AbstractVehicle readedDataObject = DaoSQLite.getInstance().readOne(id);

            assertNotNull(readedDataObject, "Read data object is null.");

            // As said before, here we set id from read database object.
            cretedDataObj.setId(readedDataObject.getId());

            assertEquals(cretedDataObj, readedDataObject);
        } catch (SQLException e) {
            fail("Error while works with DB.", e);
        }
    }

    @AfterEach
    public void removeTemporaryDbFile() {
        try {
            Files.deleteIfExists(generetedDbFile.toPath());
            Settings.writeSettings(Settings.DB_FILE_PATH_SETTING_NAME, "");
        } catch (IOException e) {
            fail("Error while delete DB file.", e);
        }
    }

}

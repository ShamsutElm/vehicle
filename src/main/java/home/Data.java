package home;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import home.db.DbInitializer;
import home.db.dao.DaoSQLite;
import home.gui.Gui;
import home.gui.components.CustomJFileChooser;
import home.utils.Utils;

class Data {

    private static final Logger LOG = LoggerFactory.getLogger(Data.class);

    static void initDB() {
        if (Settings.hasPathToDBFile()) {
            readDataFromDB();
        } else {
            try {
                CustomJFileChooser.createAndShowChooser(null, CustomJFileChooser.ChooserOperation.CREATE_OR_OPEN);
                readDataFromDB();
                Gui.getInstance().setDBLabel(Settings.getDbFilePath());
            } catch (Exception e) {
                throw new IllegalStateException("Error while create/open DB file", e);
            }
        }
    }

    private static void readDataFromDB() {
        try {
            DbInitializer.createTableIfNotExists();

            Utils.runInThread("-> read data from DB", () -> {
                try {
                    Storage.INSTANCE.refresh(DaoSQLite.getInstance().readAll());
                } catch (SQLException e) {
                    Utils.logAndShowError(LOG, null, "Error while read data from DB: "
                            + e.getMessage(), "Data reading error", e);
                    throw new IllegalStateException("Error while read data from DB: " + e.getMessage(), e);
                }
            });
        } catch (Exception e) {
            throw new IllegalStateException("Error while read data from DB", e);
        }
    }
}

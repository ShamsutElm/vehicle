package home;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import home.db.DbInitializer;
import home.db.dao.DaoSQLite;
import home.gui.DataActionInGui;
import home.gui.Gui;
import home.gui.components.CustomJFileChooser;
import home.utils.LogUtils;
import home.utils.ThreadUtils;

final class Data {

    private static final Logger LOG = LoggerFactory.getLogger(Data.class);

    static void initDB() {
        if (Settings.hasPathToDBFile()) {
            readDataFromDB();
        } else {
            try {
                CustomJFileChooser.createAndShowChooser(null, CustomJFileChooser.ChooserOperation.CREATE_OR_OPEN);
                readDataFromDB();
                Gui.INSTANCE.setDBLabel(Settings.getDbFilePath());
            } catch (Exception e) {
                throw new IllegalStateException("Error while create/open DB file", e);
            }
        }
    }

    private static void readDataFromDB() {
        ThreadUtils.runInThread(() -> {
            Thread.currentThread().setName("-> read data from DB");
            try {
                DbInitializer.createTableIfNotExists();
                DataActionInGui.init(DaoSQLite.getInstance().readAll());
            } catch (SQLException e) {
                String errorMsg = "Error while read data from DB: " + e.getMessage();
                LogUtils.logAndShowError(LOG, null, errorMsg, "Data reading error", e);
                throw new IllegalStateException(errorMsg, e);
            }
        });
    }
}

package home;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import home.db.DbInitializer;
import home.db.dao.DaoSQLite;
import home.gui.Gui;
import home.gui.components.CustomJFileChooser;
import home.gui.components.CustomJFileChooser.ChooserOperation;
import home.utils.Utils;

public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            Settings.readSettings();
            Gui.getInstance().buildGui();

            if (Settings.hasPathToDBFile()) {
                initDB();
            } else {
                CustomJFileChooser.createAndShowChooser(null, ChooserOperation.CREATE_OR_OPEN);
                initDB();
                Gui.getInstance().setDBLabel(Settings.DB_FILE_PATH);
            }

            LOG.info("Приложение успешно запущенно.");
        } catch (Exception e) {
            LOG.error("Ошибка при запуске приложения", e);
            JOptionPane.showMessageDialog(null, "Ошибка при запуске приложения:\n"
                    + e.getMessage(), "Ошибка запуска", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private static void initDB() {
        try {
            DbInitializer.createTableIfNotExists();
            readDataFromDB();
        } catch (Exception e) {
            Utils.logAndShowError(LOG, null, e.getMessage(), "DB init error", e);
            System.exit(1);
        }
    }

    private static void readDataFromDB() {
        Utils.runInThread("-> read data from DB", () -> {
            try {
                Storage.INSTANCE.refresh(DaoSQLite.getInstance().readAll());
            } catch (Exception e) {
                Utils.logAndShowError(LOG, null, "Error while read data from DB: "
                        + e.getMessage(), "Data reading error", e);
                System.exit(1);
            }
        });
    }
}

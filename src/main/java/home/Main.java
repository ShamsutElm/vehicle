package home;

import static java.lang.Thread.UncaughtExceptionHandler;

import java.sql.SQLException;
import java.util.function.BiFunction;

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

    private static final String START_LOG_MESSAGE = "Application {} v{} started successfully.";

    private static final String DEFAULT_APP_NAME = "=VEHICLE_ACCOUNTING=";
    private static final String DEFAULT_APP_VERSION = "UNKNOWN";

    public static String appName;
    public static String appVersion;

    public static void main(String[] args) {
        boolean isStarted = false;
        try {
            startApplication();
            isStarted = true;
            LOG.info(START_LOG_MESSAGE, appName, appVersion);
        } catch (Exception e) {
            Utils.logAndShowError(LOG, null, e.getMessage(), "Application start error", e);
        } finally {
            if (!isStarted) {
                System.exit(1);
            }
        }
    }

    private static void startApplication() {
        setUncaughtExceptionProcessing();

        initAppDescription();
        Settings.readSettings();
        Gui.getInstance().buildGui();

        initDB();
    }

    private static void setUncaughtExceptionProcessing() {
        var handler = new UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                Utils.logAndShowError(LOG, null, e.getMessage(), "Error", e);
                System.exit(1);
            }
        };
        Thread.setDefaultUncaughtExceptionHandler(handler);
    }

    private static void initAppDescription() {
        BiFunction<String, String, String> getSafeVal = (val, def) -> val != null ? val : def;
        Package pacage = Main.class.getPackage();
        appName = getSafeVal.apply(pacage.getImplementationTitle(), DEFAULT_APP_NAME);
        appVersion = getSafeVal.apply(pacage.getImplementationVersion(), DEFAULT_APP_VERSION);
    }

    private static void initDB() {
        if (Settings.hasPathToDBFile()) {
            readDataFromDB();
        } else {
            try {
                CustomJFileChooser.createAndShowChooser(null, ChooserOperation.CREATE_OR_OPEN);
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

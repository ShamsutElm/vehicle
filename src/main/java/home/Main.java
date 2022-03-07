package home;

import java.sql.SQLException;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import home.db.DbInitializer;
import home.db.dao.DaoSQLite;
import home.gui.Gui;

public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            DbInitializer.createTableIfNotExists();
            Gui.getInstance().buildGui();
            Storage.getInstance().refresh(DaoSQLite.getInstance().readAll());
            LOG.info("Приложение успешно запущенно.");
        } catch (SQLException e) {
            LOG.error("Ошибка при запуске приложения", e);
            JOptionPane.showMessageDialog(null, "Ошибка при запуске приложения:\n"
                    + e.getMessage(), "Ошибка запуска", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(1);
        }
    }
}

package home.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import home.Settings;

public class Connector {

    private static final Logger LOG = LoggerFactory.getLogger(Connector.class);

    private static final String JDBC_DRIVER_SQLITE = "org.sqlite.JDBC";
    private static final String CONNECTION_URL_SQLITE = "jdbc:sqlite:%s";

    public static Connection getConnetion() throws SQLException {
        try {
            Class.forName(JDBC_DRIVER_SQLITE);
            return DriverManager.getConnection(
                    String.format(CONNECTION_URL_SQLITE, Settings.DB_FILE_PATH));
        } catch (ClassNotFoundException e) {
            String errorMsg = "Не найден JDBC драйвер";
            LOG.error(errorMsg);
            SQLException ex = new SQLException(errorMsg);
            ex.addSuppressed(e);
            throw ex;
        } catch (SQLException e) {
            LOG.error("Ошибка подключения к БД");
            throw e;
        }
    }

    private Connector() {
    }
}

package home.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Connector {

    private static final Logger LOG = LoggerFactory.getLogger(Connector.class);

    private static final String JDBC_DRIVER_SQLITE = "org.sqlite.JDBC";
    private static final String CONNECTION_URL_SQLITE = "jdbc:sqlite:%s";

    public static Connection getConnetion() throws SQLException {
        try {
            String dbFileAbsolutePath = DbCreator.dbFileAbsolutePath();
            Class.forName(JDBC_DRIVER_SQLITE);
            return DriverManager.getConnection(
                    String.format(CONNECTION_URL_SQLITE, dbFileAbsolutePath));
        } catch (ClassNotFoundException | IOException e) {
            String errorMsg = null;
            if (e instanceof ClassNotFoundException) {
                errorMsg = "Не найден JDBC драйвер";
            } else if (e instanceof IOException) {
                errorMsg = "Ошибка создания файла БД";
            }
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

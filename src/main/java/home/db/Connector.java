package home.db;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import home.Settings;
import home.utils.LogUtils;

public final class Connector {

    private static final Logger LOG = LoggerFactory.getLogger(Connector.class);

    private static final String QUERY_TIMEOUT = "30";

    private static final String JDBC_DRIVER_POSTGRESQL = "org.postgresql.Driver";
    private static final String URL_POSTGRESQL = "jdbc:postgresql://%s:%s/%s";

    private static final String JDBC_DRIVER_SQLITE = "org.sqlite.JDBC";
    private static final String URL_SQLITE = "jdbc:sqlite:%s";

    public static Connection getConnetionToPostgreSQL(String host, String port, String dbName,
            String user, String password) throws SQLException {
        String url = generetePostgreSqlUrl(host, port, dbName);

        var props = new Properties();
        props.setProperty("user", user);
        props.setProperty("password", password);
        props.setProperty("reWriteBatchedInserts", "true");
        props.setProperty("loginTimeout", QUERY_TIMEOUT);
        props.setProperty("connectTimeout", QUERY_TIMEOUT);
        props.setProperty("cancelSignalTimeout", QUERY_TIMEOUT);
        props.setProperty("socketTimeout", QUERY_TIMEOUT);

        return getConnetion(url, props, JDBC_DRIVER_POSTGRESQL);
    }

    private static String generetePostgreSqlUrl(String host, String port, String dbName) {
        String db;
        try {
            db = URLEncoder.encode(dbName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOG.error("Encoding error of database name.");
            db = dbName;
        }
        return String.format(URL_POSTGRESQL, host, port, db);
    }

    public static Connection getConnetionToSQLite() throws SQLException {
        return getConnetion(String.format(URL_SQLITE, Settings.getDbFilePath()), new Properties(), JDBC_DRIVER_SQLITE);
    }

    private static Connection getConnetion(String url, Properties props, String jdbcDriver) throws SQLException {
        try {
            Class.forName(jdbcDriver);

            // Driver driver = (Driver) Class.forName(jdbcDriver).newInstance();
            // DriverManager.registerDriver(driver);

            return DriverManager.getConnection(url, props);
        } catch (ClassNotFoundException e) {
            throw LogUtils.logAndCreateSqlException("Database driver is not found", LOG, e);
        } catch (SQLException e) {
            throw LogUtils.logAndCreateSqlException("Error while connecting to the database", LOG, e);
        }
    }

    private Connector() {
    }
}

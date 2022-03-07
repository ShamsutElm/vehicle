package home.db;

import java.sql.SQLException;

public class DbInitializer {

    private static final String CREATE_TBL_QUERY = "CREATE TABLE if NOT EXISTS vehicle ("
            + " 'id' integer PRIMARY KEY AUTOINCREMENT,"
            + " 'type' text,"
            + " 'color' text,"
            + " 'number' text,"
            + " 'is_transports_cargo' integer,"
            + " 'is_transports_passengers' integer,"
            + " 'has_trailer' integer,"
            + " 'has_cradle' integer,"
            + " 'date_time' integer);";

    public static void createTableIfNotExists() throws SQLException {
        try (var connection = Connector.getConnetion();
                var stmt = connection.createStatement()) {
            stmt.execute(CREATE_TBL_QUERY);
        }
    }

    private DbInitializer() {
    }
}

package home.db.dao;

import java.sql.SQLException;
import java.util.List;

import home.models.AbstractVehicle;

public sealed interface IDao permits AbstractDao {

    List<AbstractVehicle> readAll() throws SQLException;

    // because it uses only in test.
    @Deprecated(forRemoval = true)
    AbstractVehicle readOne(long id) throws SQLException;

    void saveAllChanges() throws SQLException;

    void saveAs() throws SQLException;
}

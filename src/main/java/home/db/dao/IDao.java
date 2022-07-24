package home.db.dao;

import java.sql.SQLException;
import java.util.List;

import home.models.AbstractVehicle;

public interface IDao {

    List<AbstractVehicle> readAll() throws SQLException;

    @Deprecated(forRemoval = true) // because it uses only in test.
    AbstractVehicle readOne(long id) throws SQLException;

    void saveAllChanges() throws SQLException;

    void saveAs() throws SQLException;
}

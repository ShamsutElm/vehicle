package home.db.dao;

import java.sql.SQLException;
import java.util.List;

import home.models.AbstractVehicle;

public interface IDao {

    AbstractVehicle readOne(long id) throws SQLException;

    List<AbstractVehicle> readAll() throws SQLException;

    void create(AbstractVehicle dataObj) throws SQLException;

    void update(AbstractVehicle dataObj) throws SQLException;

    void delete(Long[] ids) throws SQLException;
}

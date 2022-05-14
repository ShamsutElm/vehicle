package home.db.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import home.Storage;
import home.db.Connector;
import home.db.IDbConsts;
import home.models.AbstractVehicle;
import home.models.Car;
import home.models.Motorcycle;
import home.models.Truck;
import home.models.VehicleType;
import home.utils.Utils;

public class DaoSQLite implements IDao {

    private static final String SELECT_ALL = "SELECT * FROM vehicle;";

    private static final String SELECT_ONE = "SELECT * FROM vehicle WHERE id=%d;";

    private static final String INSERT = "INSERT INTO vehicle"
            + " ('type','color','number','date_time','is_transports_cargo',"
            + "'is_transports_passengers','has_trailer','has_cradle')"
            + " VALUES (?,?,?,?,?,?,?,?);";

    private static final String UPDATE = "UPDATE vehicle SET type = ?,"
            + " color = ?, number = ?, date_time = ?,"
            + " is_transports_cargo = ?, is_transports_passengers = ?,"
            + " has_trailer = ?, has_cradle = ? WHERE id = ?;";

    private static final String DELETE = "DELETE FROM vehicle WHERE id IN (%s);";

    private static IDao instance;

    private DaoSQLite() {
    }

    public static IDao getInstance() {
        if (instance == null) {
            instance = new DaoSQLite();
        }
        return instance;
    }

    @Override
    public AbstractVehicle readOne(long id) throws SQLException {
        String sql = String.format(SELECT_ONE, id);
        try (var conn = Connector.getConnetion();
                var stmt = conn.createStatement();
                var res = stmt.executeQuery(sql)) {
            var dataObjs = new ArrayList<AbstractVehicle>();
            while (res.next()) {
                dataObjs.add(convertResultToDataObj(res));
            }

            if (dataObjs.size() > 1) {
                throw new SQLException("Наличие объектов с одинаковым id: " + id);
            }
            return dataObjs.get(0);
        }
    }

    @Override
    public List<AbstractVehicle> readAll() throws SQLException {
        try (var conn = Connector.getConnetion();
                var stmt = conn.createStatement();
                var res = stmt.executeQuery(SELECT_ALL)) {
            var dataObjs = new ArrayList<AbstractVehicle>();
            while (res.next()) {
                dataObjs.add(convertResultToDataObj(res));
            }
            return dataObjs;
        }
    }

    private AbstractVehicle convertResultToDataObj(ResultSet res) throws SQLException {
        var type = res.getString(IDbConsts.TYPE);
        var vehicleType = VehicleType.getVehicleType(type);
        if (vehicleType == null) {
            throw new SQLException("Не правильный тип: " + type);
        }

        AbstractVehicle vehicle = null;
        switch (vehicleType) {
            case CAR:
                vehicle = new Car();
                var car = (Car) vehicle;
                car.setTransportsPassengers(
                        convertToBoolean(res.getInt(IDbConsts.IS_TRANSPORTS_PASSENGERS)));
                car.setHasTrailer(
                        convertToBoolean(res.getInt(IDbConsts.HAS_TRAILER)));
                break;

            case TRUCK:
                vehicle = new Truck();
                var truck = (Truck) vehicle;
                truck.setTransportsCargo(
                        convertToBoolean(res.getInt(IDbConsts.IS_TRANSPORTS_CARGO)));
                truck.setHasTrailer(
                        convertToBoolean(res.getInt(IDbConsts.HAS_TRAILER)));
                break;

            case MOTORCYCLE:
                vehicle = new Motorcycle();
                ((Motorcycle) vehicle).setHasCradle(
                        convertToBoolean(res.getInt(IDbConsts.HAS_CRADLE)));
                break;
        }

        vehicle.setId(res.getLong(IDbConsts.ID));
        vehicle.setColor(res.getString(IDbConsts.COLOR));
        vehicle.setNumber(res.getString(IDbConsts.NUMBER));
        vehicle.setDateTime(res.getLong(IDbConsts.DATE_TIME));

        return vehicle;
    }

    private boolean convertToBoolean(int intBoolean) throws SQLException {
        switch (intBoolean) {
            case 0:
                return false;

            case 1:
                return true;

            default:
                throw new SQLException("Не правильное логическое значение: " + intBoolean);
        }
    }

    @Override
    public void create(AbstractVehicle dataObj) throws SQLException {
        try (var conn = Connector.getConnetion();
                var pstmt = conn.prepareStatement(INSERT)) {
            VehicleType dataObjType = dataObj.getType();

            pstmt.setString(1, dataObjType.getType());
            pstmt.setString(2, dataObj.getColor());
            pstmt.setString(3, dataObj.getNumber());
            pstmt.setLong(4, dataObj.getDateTime());

            switch (dataObjType) {
                case CAR:
                    Car car = (Car) dataObj;
                    pstmt.setInt(6, converToInt(car.isTransportsPassengers()));
                    pstmt.setInt(7, converToInt(car.hasTrailer()));
                    break;

                case TRUCK:
                    Truck truck = (Truck) dataObj;
                    pstmt.setInt(5, converToInt(truck.isTransportsCargo()));
                    pstmt.setInt(7, converToInt(truck.hasTrailer()));
                    break;

                case MOTORCYCLE:
                    pstmt.setInt(8, converToInt(((Motorcycle) dataObj).hasCradle()));
                    break;
            }

            if (pstmt.executeUpdate() != 1) {
                throw new SQLException("Информация не была добавленна в БД");
            }
        }
    }

    private int converToInt(boolean booleanVal) {
        return booleanVal ? 1 : 0;
    }

    @Override
    public void update(AbstractVehicle dataObj) throws SQLException {
        try (var conn = Connector.getConnetion();
                var pstmt = conn.prepareStatement(UPDATE)) {
            VehicleType dataObjType = dataObj.getType();

            pstmt.setString(1, dataObjType.getType());
            pstmt.setString(2, dataObj.getColor());
            pstmt.setString(3, dataObj.getNumber());
            pstmt.setLong(4, dataObj.getDateTime());

            switch (dataObjType) {
                case CAR:
                    Car car = (Car) dataObj;
                    pstmt.setInt(6, converToInt(car.isTransportsPassengers()));
                    pstmt.setInt(7, converToInt(car.hasTrailer()));
                    break;

                case TRUCK:
                    Truck truck = (Truck) dataObj;
                    pstmt.setInt(5, converToInt(truck.isTransportsCargo()));
                    pstmt.setInt(7, converToInt(truck.hasTrailer()));
                    break;

                case MOTORCYCLE:
                    pstmt.setInt(8, converToInt(((Motorcycle) dataObj).hasCradle()));
                    break;
            }

            pstmt.setLong(9, dataObj.getId());

            if (pstmt.executeUpdate() != 1) {
                throw new SQLException("Информация не была обновлена в БД");
            }
        }
    }

    @Override
    public void delete(Long[] ids) throws SQLException {
        String placeHolders = "?,".repeat(ids.length);
        String sql = String.format(DELETE,
                placeHolders.substring(0, placeHolders.length() - 1));
        try (var conn = Connector.getConnetion();
                var pstmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < ids.length; i++) {
                pstmt.setLong(i + 1, ids[i]);
            }

            if (pstmt.executeUpdate() <= 0) {
                throw new SQLException("Информация не была удалена из БД: "
                        + Utils.idsToString(ids));
            }
        }
    }

    @Override
    public void saveAllChanges() throws SQLException {
        Long[] idsForDel = Storage.getInstance().getIdsForDel();
        if (idsForDel.length > 0) {
            delete(idsForDel);
        }

        for (AbstractVehicle dataObj : Storage.getInstance().getAll()) {
            long id = dataObj.getId();
            if (id > 0) {
                update(dataObj);
            } else {
                create(dataObj);
            }
        }
    }
}

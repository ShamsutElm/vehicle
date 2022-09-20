package home.file.csv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import home.file.IImporter;
import home.models.AbstractVehicle;
import home.models.AbstractVehicleWithTrailer;
import home.models.Car;
import home.models.Motorcycle;
import home.models.Truck;
import home.models.VehicleType;
import home.utils.LogUtils;
import home.utils.Utils;

public final class CsvImporter implements IImporter {

    private static final Logger LOG = LoggerFactory.getLogger(CsvImporter.class);

    @Override
    public List<AbstractVehicle> importDataObjsFromFile(File file) {
        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            List<String[]> rawDataObjs = reader.readAll();
            List<AbstractVehicle> dataObjs = parse(rawDataObjs);
            return dataObjs;
        } catch (FileNotFoundException e) {
            throw LogUtils.logAndCreateIllegalStateException(
                    "Can't find csn file for import data : " + file.getAbsolutePath(),
                    LOG, e);
        } catch (IOException e) {
            throw LogUtils.logAndCreateIllegalStateException(
                    "Error while reading csv file : " + file.getAbsolutePath(),
                    LOG, e);
        } catch (CsvException e) {
            throw LogUtils.logAndCreateIllegalStateException(
                    "Error while validate csv file : " + file.getAbsolutePath(),
                    LOG, e);
        }
    }

    private List<AbstractVehicle> parse(List<String[]> rawDataObjs) {
        checkElementsCountInRawDataObjs(rawDataObjs);

        var dataObjs = new ArrayList<AbstractVehicle>();
        for (String[] rawDataObj : getRawDataObjWithoutHeader(rawDataObjs)) {
            dataObjs.add(convertDataObj(rawDataObj));
        }
        return dataObjs;
    }

    private void checkElementsCountInRawDataObjs(List<String[]> rawDataObjs) {
        for (String[] rawDataObj : rawDataObjs) {
            if (ICsvConsts.CSV_ROW_SIZE != rawDataObj.length) {
                throw new IllegalArgumentException("Incorrect count of elements in : [%s]"
                        .formatted(String.join(", ", rawDataObj)));
            }
        }
    }

    private List<String[]> getRawDataObjWithoutHeader(List<String[]> rawDataObjs) {
        String[] header = rawDataObjs.get(0);
        return Arrays.equals(header, ICsvConsts.CSV_HEADER)
                ? rawDataObjs.subList(1, rawDataObjs.size())
                : rawDataObjs;
    }

    private AbstractVehicle convertDataObj(String[] rawDataObj) {
        String type = rawDataObj[ICsvConsts.TYPE_IDX];
        VehicleType vehicleType = VehicleType.getVehicleType(type);
        if (vehicleType == null) {
            throw new IllegalArgumentException("Wrong vehicle type received : " + type);
        }

        AbstractVehicle dataObj = switch (vehicleType) {
            case CAR -> new Car();
            case TRUCK -> new Truck();
            case MOTORCYCLE -> new Motorcycle();
        };

        for (int tagIdx = ICsvConsts.COLOR_IDX; tagIdx <= ICsvConsts.HAS_CRADLE_IDX; tagIdx++) {
            String value = rawDataObj[tagIdx];
            switch (tagIdx) {
                case ICsvConsts.COLOR_IDX -> dataObj.setColor(value);
                case ICsvConsts.NUMBER_IDX -> dataObj.setNumber(value);
                case ICsvConsts.DATE_IDX -> dataObj.setDateTime(Utils.getLongFromFormattedDate(value));
                case ICsvConsts.HAS_TRAILER_IDX -> {
                    if (vehicleType.in(VehicleType.CAR, VehicleType.TRUCK)) {
                        boolean hasTrailer = Boolean.parseBoolean(value);
                        ((AbstractVehicleWithTrailer) dataObj).setHasTrailer(hasTrailer);
                    }
                }
                case ICsvConsts.IS_TRANSPORTS_PASSENGERS_IDX -> {
                    if (VehicleType.CAR == vehicleType) {
                        boolean isTransportsPassengers = Boolean.parseBoolean(value);
                        ((Car) dataObj).setTransportsPassengers(isTransportsPassengers);
                    }
                }
                case ICsvConsts.IS_TRANSPORTS_CARGO_IDX -> {
                    if (VehicleType.TRUCK == vehicleType) {
                        boolean isTransportsCargo = Boolean.parseBoolean(value);
                        ((Truck) dataObj).setTransportsCargo(isTransportsCargo);
                    }
                }
                case ICsvConsts.HAS_CRADLE_IDX -> {
                    if (VehicleType.MOTORCYCLE == vehicleType) {
                        boolean hasCrandle = Boolean.parseBoolean(value);
                        ((Motorcycle) dataObj).setHasCradle(hasCrandle);
                    }
                }
                default -> throw new IllegalArgumentException(
                        "There is no processing for tag index" + tagIdx);
            }
        }
        return dataObj;
    }
}

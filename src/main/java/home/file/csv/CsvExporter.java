package home.file.csv;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import home.Storage;
import home.file.IExporter;
import home.models.AbstractVehicle;
import home.models.AbstractVehicleWithTrailer;
import home.models.Car;
import home.models.Motorcycle;
import home.models.Truck;
import home.models.VehicleType;
import home.utils.LogUtils;
import home.utils.Utils;

public final class CsvExporter implements IExporter {

    private static final Logger LOG = LoggerFactory.getLogger(CsvExporter.class);

    private static final char CSV_EMPTY_QUOTE_CHAR = Character.MIN_VALUE;
    private static final char CSV_PARAMS_SEPARATOR = ',';
    
    private static final String FALSE = Boolean.FALSE.toString();

    @Override
    public String exportAllDataObjsToString() {
        List<AbstractVehicle> dataObjStarage = Storage.INSTANCE.getAll();

        var convertedDataObj = new ArrayList<String[]>(dataObjStarage.size() + 1);
        convertedDataObj.add(ICsvConsts.CSV_HEADER);
        for (AbstractVehicle dataObj : dataObjStarage) {
            convertedDataObj.add(convertDataObjToArray(dataObj));
        }

        try (var stringWriter = new StringWriter();
             ICSVWriter csvWriter = new CSVWriterBuilder(stringWriter)
                     .withQuoteChar(CSV_EMPTY_QUOTE_CHAR)
                     .withSeparator(CSV_PARAMS_SEPARATOR).build()) {
            csvWriter.writeAll(convertedDataObj);
            String dataObjsInCsvFormat = stringWriter.toString();
            return dataObjsInCsvFormat;
        } catch (IOException e) {
            throw LogUtils.logAndCreateIllegalStateException("CSV concerter error", LOG, e);
        }
    }

    private String[] convertDataObjToArray(AbstractVehicle dataObj) {
        var array = new String[ICsvConsts.CSV_ROW_SIZE];

        VehicleType type = dataObj.getType();

        array[ICsvConsts.TYPE_IDX] = type.getType();
        array[ICsvConsts.COLOR_IDX] = dataObj.getColor();
        array[ICsvConsts.NUMBER_IDX] = dataObj.getNumber();
        array[ICsvConsts.DATE_IDX] = Utils.getFormatedDate(dataObj.getDateTime());

        String hasTrailerStr = FALSE;
        if (type.in(VehicleType.CAR, VehicleType.TRUCK)) {
            hasTrailerStr = Boolean.toString(((AbstractVehicleWithTrailer) dataObj).hasTrailer());
        }
        array[ICsvConsts.HAS_TRAILER_IDX] = hasTrailerStr;

        String isTransportsPassengersStr = FALSE;
        if (VehicleType.CAR == type) {
            isTransportsPassengersStr = Boolean.toString(((Car) dataObj).isTransportsPassengers());
        }
        array[ICsvConsts.IS_TRANSPORTS_PASSENGERS_IDX] = isTransportsPassengersStr;

        String isTransportsCargoStr = FALSE;
        if (VehicleType.TRUCK == type) {
            isTransportsCargoStr = Boolean.toString(((Truck) dataObj).isTransportsCargo());
        }
        array[ICsvConsts.IS_TRANSPORTS_CARGO_IDX] = isTransportsCargoStr;

        String hasCrandleStr = FALSE;
        if (VehicleType.MOTORCYCLE == type) {
            hasCrandleStr = Boolean.toString(((Motorcycle) dataObj).hasCradle());
        }
        array[ICsvConsts.HAS_CRADLE_IDX] = hasCrandleStr;

        return array;
    }
}

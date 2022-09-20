package home.file;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import home.Storage;
import home.models.AbstractVehicle;
import home.models.Car;
import home.models.Motorcycle;
import home.models.Truck;
import home.utils.Utils;

sealed class AbstractFileTest permits ImporterTest, ExporterTest {

    private static final String FILE_NAME = "data_objs.%s";

    private static final List<AbstractVehicle> dataObjs = new LinkedList<>();

    @BeforeAll
    static void fillStorage() {
        Storage.INSTANCE.initDataObjs(getTestDataObjs());
    }

    protected Path getFilePath(String extension) throws URISyntaxException {
        String filePath = FILE_NAME.formatted(extension);
        return Paths.get(getClass().getResource(filePath).toURI()).toAbsolutePath();
    }

    protected static List<AbstractVehicle> getTestDataObjs() {
        if (!dataObjs.isEmpty()) {
            return dataObjs;
        }

        var car = new Car();
        car.setColor("red");
        car.setNumber("1");
        car.setDateTime(Utils.getLongFromFormattedDate("2022.09.20 | 16:15:39"));
        car.setHasTrailer(true);
        car.setTransportsPassengers(false);
        dataObjs.add(car);

        var truck = new Truck();
        truck.setColor("black");
        truck.setNumber("2");
        truck.setDateTime(Utils.getLongFromFormattedDate("2022.09.20 | 16:15:51"));
        truck.setHasTrailer(false);
        truck.setTransportsCargo(true);
        dataObjs.add(truck);

        var moto = new Motorcycle();
        moto.setColor("white");
        moto.setNumber("3");
        moto.setDateTime(Utils.getLongFromFormattedDate("2022.09.20 | 16:15:59"));
        moto.setHasCradle(false);
        dataObjs.add(moto);

        return dataObjs;
    }

    @AfterAll
    static void cleanStorage() {
        Storage.INSTANCE.initDataObjs(Collections.emptyList());
    }
}

package home.file;

import java.io.File;
import java.util.List;

import home.models.AbstractVehicle;

public interface IImporter {

    List<AbstractVehicle> importDataObjsFromFile(File file);
}

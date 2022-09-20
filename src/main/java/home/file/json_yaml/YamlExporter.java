package home.file.json_yaml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import home.Storage;
import home.file.Tag;
import home.models.AbstractVehicle;

public final class YamlExporter extends AbstractJsonYamlExporter {

    private static final int INDENT = 4;
    private static final int INDICATOR_INDENT = 2;

    @Override
    public String exportAllDataObjsToString() {
        var convertedDataObjs = new ArrayList<Map<String, String>>();
        for (AbstractVehicle dataObj : Storage.INSTANCE.getAll()) {
            convertedDataObjs.add(convertDataObjToMap(dataObj));
        }

        var dataMap = new HashMap<String, Object>();
        dataMap.put(Tag.VEHICLES.getTagName(), convertedDataObjs);

        var options = new DumperOptions();
        options.setIndent(INDENT);
        options.setIndicatorIndent(INDICATOR_INDENT);
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        var yaml = new Yaml(options);
        String dataObjsInYamlFormat = yaml.dump(dataMap);
        dataObjsInYamlFormat = removeQoutesInValue(dataObjsInYamlFormat);
        dataObjsInYamlFormat = prettyPrintDasnOffsets(dataObjsInYamlFormat);
        return dataObjsInYamlFormat;
    }

    private String removeQoutesInValue(String str) {
        return str.replace("'", "");
    }

    private String prettyPrintDasnOffsets(String str) {
        String baseDashOffset = " ".repeat(INDICATOR_INDENT) + "-";
        String prettyDashOffset = baseDashOffset + "\n" + " ".repeat(INDENT - 1);
        return str.replace(baseDashOffset, prettyDashOffset);
    }
}

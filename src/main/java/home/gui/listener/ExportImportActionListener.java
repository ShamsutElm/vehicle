package home.gui.listener;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.slf4j.Logger;

import home.gui.components.CustomJfileChooserImpExp;
import home.gui.components.CustomJfileChooserImpExp.DataFormat;
import home.utils.LogUtils;
import home.utils.ThreadUtils;

public final class ExportImportActionListener implements ActionListener {

    private final DataFormat dataFormat;
    private final boolean isImport;
    private final Component parent;
    private final Logger log;

    public ExportImportActionListener(DataFormat dataFormat, boolean isImport, Component parent, Logger log) {
        this.dataFormat = dataFormat;
        this.isImport = isImport;
        this.parent = parent;
        this.log = log;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        ThreadUtils.runInThread(() -> {
            Thread.currentThread().setName("-> export/import operation");
            try {
                // TODO make in parallel thread only export/import operation, exclide gui
                //  (CustomJfileChooserImpExp from line 85 to line 115 must run in parallel thread).
                CustomJfileChooserImpExp.createAndShowChooser(parent, dataFormat, isImport);
            } catch (Exception e) {
                LogUtils.logAndShowError(log, parent, e.getMessage(), "Export/Import error", e);
            }
        });
    }
}

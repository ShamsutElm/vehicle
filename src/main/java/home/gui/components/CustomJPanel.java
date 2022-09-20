package home.gui.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public final class CustomJPanel extends JPanel {

    public enum PanelType {
        FRAME_TABLE_PANEL,
        FRAME_BUTTON_PANEL,
        DIALOG_TEXT_FIELDS_PANEL,
        DIALOB_BUTTON_PANEL
    }

    // Main table panel size
    private static final int FRAME_TBL_PNL_PREF_WIDTH = 300;
    private static final int FRAME_TBL_PNL_PREF_HEIGHT = 400;
    private static final int FRAME_TBL_PNL_MIN_WIDTH = 200;
    private static final int FRAME_TBL_PNL_MIN_HEIGHT = 100;
    private static final int FRAME_TBL_PNL_GAP = 2;

    // Main button panel size
    private static final int FRAME_BTN_PNL_PREF_WIDTH = 150;
    private static final int FRAME_BTN_PNL_PREF_HEIGHT = 400;
    private static final int FRAME_BTN_PNL_MIN_WIDTH = 100;
    private static final int FRAME_BTN_PNL_MIN_HEIGHT = 100;
    private static final int FRAME_BTN_PNL_GAP = 10;
    private static final int FRAME_BTN_PNL_ROWS = 8;
    private static final int FRAME_BTN_PNL_COLUMNS = 1;

    // Dialog text fields panel size
    private static final int DIALOG_TXT_FIELD_PANEL_WIDTH = 450;
    private static final int DIALOG_TXT_FIELD_PANEL_HEIGHT = 300;
    private static final int DIALOG_TXT_FIELD_PANEL_GRID_LAYOUT_ROWS = 8;
    private static final int DIALOG_TXT_FIELD_PANEL_GRID_LAYOUT_COLUMNS = 1;
    private static final int DIALOG_TXT_FIELD_PANEL_GRID_LAYOUT_GAP = 10;

    // Dialog buttons panel size
    private static final int DIALOG_BTN_PANEL_WIDTH = 450;
    private static final int DIALOG_BTN_PANEL_HEIGHT = 50;
    private static final int DIALOG_BTN_PANEL_FLOW_LAYOUT_H_GAP = 10;
    private static final int DIALOG_BTN_PANEL_FLOW_LAYOUT_V_GAP = 2;

    private static final int EMPTY_BORDER_SIZE = 10;

    private CustomJPanel() {
    }

    public static CustomJPanel create(PanelType panelType) {
        var panel = new CustomJPanel();
        panel.setPanelParams(panel, panelType);
        return panel;
    }

    private void setPanelParams(JPanel panel, PanelType panelType) {
        String panelName = panelType.name();
        switch (panelType) {
            case FRAME_TABLE_PANEL -> setPanelParams(panel, panelName, FRAME_TBL_PNL_PREF_WIDTH,
                    FRAME_TBL_PNL_PREF_HEIGHT,
                    FRAME_TBL_PNL_MIN_WIDTH, FRAME_TBL_PNL_MIN_HEIGHT,
                    new BorderLayout(FRAME_TBL_PNL_GAP, FRAME_TBL_PNL_GAP));

            case FRAME_BUTTON_PANEL -> setPanelParams(panel, panelName, FRAME_BTN_PNL_PREF_WIDTH,
                    FRAME_BTN_PNL_PREF_HEIGHT,
                    FRAME_BTN_PNL_MIN_WIDTH, FRAME_BTN_PNL_MIN_HEIGHT,
                    new GridLayout(FRAME_BTN_PNL_ROWS, FRAME_BTN_PNL_COLUMNS,
                            FRAME_BTN_PNL_GAP, FRAME_BTN_PNL_GAP));

            case DIALOG_TEXT_FIELDS_PANEL -> setPanelParams(panel, panelName, DIALOG_TXT_FIELD_PANEL_WIDTH,
                    DIALOG_TXT_FIELD_PANEL_HEIGHT,
                    new GridLayout(DIALOG_TXT_FIELD_PANEL_GRID_LAYOUT_ROWS,
                            DIALOG_TXT_FIELD_PANEL_GRID_LAYOUT_COLUMNS,
                            DIALOG_TXT_FIELD_PANEL_GRID_LAYOUT_GAP,
                            DIALOG_TXT_FIELD_PANEL_GRID_LAYOUT_GAP));

            case DIALOB_BUTTON_PANEL -> setPanelParams(panel, panelName, DIALOG_BTN_PANEL_WIDTH,
                    DIALOG_BTN_PANEL_HEIGHT,
                    new FlowLayout(FlowLayout.CENTER,
                            DIALOG_BTN_PANEL_FLOW_LAYOUT_H_GAP,
                            DIALOG_BTN_PANEL_FLOW_LAYOUT_V_GAP));
        }
    }

    private void setPanelParams(JPanel panel, String name, int width, int height,
            LayoutManager layout) {
        setPanelParams(panel, name, width, height, width, height, layout);
    }

    private void setPanelParams(JPanel panel, String name, int width, int height,
            int minWidth, int minHeight, LayoutManager layout) {
        panel.setName(name);
        panel.setSize(width, height);
        panel.setPreferredSize(new Dimension(width, height));
        panel.setMinimumSize(new Dimension(minHeight, minHeight));
        panel.setLayout(layout);
        panel.setBorder(new EmptyBorder(EMPTY_BORDER_SIZE, EMPTY_BORDER_SIZE,
                EMPTY_BORDER_SIZE, EMPTY_BORDER_SIZE));
    }
}

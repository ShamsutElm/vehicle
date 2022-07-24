package home.gui.components;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import home.gui.IGuiConsts;
import home.models.AbstractVehicle;
import home.utils.Utils;

@SuppressWarnings("serial")
final class CustomJTableDataModel extends AbstractTableModel {

    private static final int COLUMNS_COUNT = 5;

    public static final int TYPE_COL_IDX = 0;
    public static final int COLOR_COL_IDX = 1;
    public static final int NUMBER_COL_IDX = 2;
    public static final int DATE_COL_IDX = 3;
    public static final int DEL_MARK_COL_IDX = 4;

    private final List<AbstractVehicle> dataObjs;

    public CustomJTableDataModel(List<AbstractVehicle> dataObjs) {
        this.dataObjs = dataObjs;
    }

    @Override
    public int getRowCount() {
        return dataObjs.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMNS_COUNT;
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case TYPE_COL_IDX:
                return IGuiConsts.TYPE;
            case COLOR_COL_IDX:
                return IGuiConsts.COLOR;
            case NUMBER_COL_IDX:
                return IGuiConsts.NUMBER;
            case DATE_COL_IDX:
                return IGuiConsts.DATE;
            case DEL_MARK_COL_IDX:
                return IGuiConsts.DELETION_MARK;
            default:
                return "";
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        AbstractVehicle dataObj = dataObjs.get(rowIndex);
        switch (columnIndex) {
            case TYPE_COL_IDX:
                return dataObj.getType();
            case COLOR_COL_IDX:
                return dataObj.getColor();
            case NUMBER_COL_IDX:
                return dataObj.getNumber();
            case DATE_COL_IDX:
                return Utils.getFormatedDate(dataObj.getDateTime());
            case DEL_MARK_COL_IDX:
                return dataObj.isMarkedForDelete();
            default:
                return "";
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnIndex == DEL_MARK_COL_IDX ? Boolean.class
                : super.getColumnClass(columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == DEL_MARK_COL_IDX;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        dataObjs.get(rowIndex).setMarkedForDelete((Boolean) aValue);
        fireTableCellUpdated(rowIndex, columnIndex);
    }
}

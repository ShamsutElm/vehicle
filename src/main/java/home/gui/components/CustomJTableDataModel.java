package home.gui.components;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import home.IConsts;
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
        String columnName = switch (column) {
            case TYPE_COL_IDX -> IGuiConsts.TYPE;
            case COLOR_COL_IDX -> IGuiConsts.COLOR;
            case NUMBER_COL_IDX -> IGuiConsts.NUMBER;
            case DATE_COL_IDX -> IGuiConsts.DATE;
            case DEL_MARK_COL_IDX -> IGuiConsts.DELETION_MARK;
            default -> IConsts.EMPTY_STRING;
        };
        return columnName;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        AbstractVehicle dataObj = dataObjs.get(rowIndex);

        Object cellValue = switch (columnIndex) {
            case TYPE_COL_IDX -> dataObj.getType();
            case COLOR_COL_IDX -> dataObj.getColor();
            case NUMBER_COL_IDX -> dataObj.getNumber();
            case DATE_COL_IDX -> Utils.getFormatedDate(dataObj.getDateTime());
            case DEL_MARK_COL_IDX -> dataObj.isMarkedForDelete();
            default -> IConsts.EMPTY_STRING;
        };

        return cellValue;
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

package home.gui;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.AbstractTableModel;

import home.Storage;
import home.db.dao.DaoSQLite;
import home.gui.components.CustomJButton;
import home.gui.components.CustomJFrame;
import home.gui.components.CustomJPanel;
import home.gui.components.CustomJPanel.PanelType;
import home.gui.components.CustomJTable;
import home.gui.components.dialog.DialogCar;
import home.gui.components.dialog.DialogMoto;
import home.gui.components.dialog.DialogTruck;

public class Gui {

    private static final int CLICK_COUNT = 2;

    private static Gui instance;

    private JTable table;
    private AbstractTableModel tableModel;
    private JScrollPane tableScrollPane;

    private JButton btnCar;
    private JButton btnTruck;
    private JButton btnMoto;
    private JButton btnDel;

    private JPanel panelTable;
    private JPanel panelButton;

    private JFrame frame;

    private Gui() {
    }

    public static Gui getInstance() {
        if (instance == null) {
            instance = new Gui();
        }
        return instance;
    }

    public void refreshTable() {
        tableModel.fireTableDataChanged();
    }

    public void buildGui() {
        // TODO make swich between color schemas(save result in property file)
        // activateSystemColorSchema();

        JFrame.setDefaultLookAndFeelDecorated(true);

        createTable();
        createButtons();
        createPannels();
        createFrame();
    }

    private void activateSystemColorSchema() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame,
                    "Error while set the system color schema.\n"
                            + "Default color schema will be used.\n"
                            + e.getMessage(),
                    "System color schema error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createTable() {
        table = CustomJTable.create();
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table = (JTable) mouseEvent.getSource();
                if (CLICK_COUNT == mouseEvent.getClickCount()) {
                    DialogCaller.showObjDialog(frame,
                            Storage.getInstance().get(table.getSelectedRow()));
                }
            }
        });

        tableScrollPane = new JScrollPane(table);
        tableModel = (AbstractTableModel) table.getModel();
    }

    private void createButtons() {
        btnCar = CustomJButton.create(IGuiConsts.CAR);
        btnCar.addActionListener(actionEvent -> DialogCaller
                .showObjDialog(frame, DialogCar.class));

        btnTruck = CustomJButton.create(IGuiConsts.TRUCK);
        btnTruck.addActionListener(actionEvent -> DialogCaller
                .showObjDialog(frame, DialogTruck.class));

        btnMoto = CustomJButton.create(IGuiConsts.MOTO);
        btnMoto.addActionListener(actionEvent -> DialogCaller
                .showObjDialog(frame, DialogMoto.class));

        btnDel = CustomJButton.create(IGuiConsts.DEL);
        btnDel.addActionListener(actionEvent -> {
            try {
                Long[] idsMarkedForDel = Storage.getInstance().getAll().stream()
                        .filter(dataObj -> dataObj.isMarkedForDelete())
                        .map(dataObj -> Long.valueOf(dataObj.getId()))
                        .toArray(Long[]::new);
                if (idsMarkedForDel.length > 0) {
                    DaoSQLite.getInstance().delete(idsMarkedForDel);
                    Storage.getInstance().refresh(DaoSQLite.getInstance().readAll());
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(frame, "Ошибка при удалении из БД:\n"
                        + e.getMessage(), "Ошибка удаления", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
    }

    private void createPannels() {
        panelTable = CustomJPanel.create(PanelType.FRAME_TABLE_PANEL);
        panelTable.add(tableScrollPane, BorderLayout.CENTER);

        panelButton = CustomJPanel.create(PanelType.FRAME_BUTTON_PANEL);
        panelButton.add(btnCar);
        panelButton.add(btnTruck);
        panelButton.add(btnMoto);
        panelButton.add(btnDel);
    }

    private void createFrame() {
        frame = CustomJFrame.create();
        frame.getContentPane().add(panelTable, BorderLayout.CENTER);
        frame.getContentPane().add(panelButton, BorderLayout.EAST);
        frame.setVisible(true);
    }
}

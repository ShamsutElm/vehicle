package home.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.AbstractTableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import home.Settings;
import home.Storage;
import home.db.DbInitializer;
import home.db.dao.DaoSQLite;
import home.gui.components.CustomJButton;
import home.gui.components.CustomJFileChooser;
import home.gui.components.CustomJFrame;
import home.gui.components.CustomJPanel;
import home.gui.components.CustomJPanel.PanelType;
import home.gui.components.CustomJTable;
import home.gui.components.dialog.DialogCar;
import home.gui.components.dialog.DialogMoto;
import home.gui.components.dialog.DialogTruck;
import home.utils.Utils;

public class Gui {

    private static final Logger LOG = LoggerFactory.getLogger(Gui.class);

    private static final int CLICK_COUNT = 2;

    private static Gui instance;

    private JLabel dbLabel;

    private JTable table;
    private AbstractTableModel tableModel;
    private JScrollPane tableScrollPane;

    private JButton btnCar;
    private JButton btnTruck;
    private JButton btnMoto;
    private JButton btnDel;

    private JPanel panelTable;
    private JPanel panelButton;

    private JMenuBar menuBar;

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

    public void setDBLabel(String label) {
        dbLabel.setText(label);
    }

    public void buildGui() {
        setStyle(Settings.STYLE);

        createTable();
        createButtons();
        createPannels();
        createMenu();
        createFrame();
    }

    private void setStyle(String style) {
        try {
            UIManager.setLookAndFeel(style.equalsIgnoreCase(IGuiConsts.SYSTEM)
                    ? UIManager.getSystemLookAndFeelClassName()
                    : UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            Utils.logAndShowError(LOG, frame,
                    "Error while set the system color schema.\n"
                            + "Default color schema will be used.\n"
                            + e.getMessage(),
                    "System color schema error", e);
        }
    }

    private void createTable() {
        dbLabel = new JLabel(Settings.hasPathToDBFile() ? Settings.DB_FILE_PATH
                : IGuiConsts.CHOOSE_DB_FILE);

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
        panelTable.add(dbLabel, BorderLayout.NORTH);
        panelTable.add(tableScrollPane, BorderLayout.CENTER);

        panelButton = CustomJPanel.create(PanelType.FRAME_BUTTON_PANEL);
        panelButton.add(btnCar);
        panelButton.add(btnTruck);
        panelButton.add(btnMoto);
        panelButton.add(btnDel);
    }

    private void createMenu() {
        var createOrOpenItime = new JMenuItem(IGuiConsts.CREATE_OR_OPEN);
        createOrOpenItime.addActionListener(new CreateOrOpenActionListner(frame, dbLabel));
        var fileMenu = new JMenu(IGuiConsts.FILE);
        fileMenu.add(createOrOpenItime);

        var defaultItem = new JCheckBoxMenuItem(IGuiConsts.DEFAULT);
        defaultItem.setSelected(Settings.STYLE.equalsIgnoreCase(IGuiConsts.DEFAULT));
        var systemItem = new JCheckBoxMenuItem(IGuiConsts.SYSTEM);
        systemItem.setSelected(Settings.STYLE.equalsIgnoreCase(IGuiConsts.SYSTEM));
        defaultItem.addActionListener(actionEvent -> styleSelectAction(systemItem, defaultItem));
        systemItem.addActionListener(actionEvent -> styleSelectAction(defaultItem, systemItem));
        var styleMenu = new JMenu(IGuiConsts.STYLE);
        styleMenu.add(defaultItem);
        styleMenu.add(systemItem);

        var aboutItem = new JMenuItem(IGuiConsts.ABOUT);
        aboutItem.addActionListener(actionEvent -> JOptionPane.showMessageDialog(
                frame, IGuiConsts.ABOUT_TEXT, IGuiConsts.ABOUT_TITLE,
                JOptionPane.INFORMATION_MESSAGE));
        var helpMenu = new JMenu(IGuiConsts.HELP);
        helpMenu.add(aboutItem);

        menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(styleMenu);
        menuBar.add(helpMenu);
    }

    private void styleSelectAction(JCheckBoxMenuItem item1, JCheckBoxMenuItem item2) {
        try {
            item1.setSelected(!item2.isSelected());

            if (item1.isSelected()) {
                Settings.writeSettings(Settings.STYLE_SETTING_NAME,
                        item1.getText().toLowerCase(Locale.ROOT));
            } else {
                Settings.writeSettings(Settings.STYLE_SETTING_NAME,
                        item2.getText().toLowerCase(Locale.ROOT));
            }
            setStyle(Settings.STYLE);
            SwingUtilities.updateComponentTreeUI(frame);
        } catch (Exception e) {
            Utils.logAndShowError(LOG, frame, "Error while chose style", "Style error", e);
        }
    }

    private void createFrame() {
        frame = CustomJFrame.create();
        frame.setJMenuBar(menuBar);
        frame.getContentPane().add(panelTable, BorderLayout.CENTER);
        frame.getContentPane().add(panelButton, BorderLayout.EAST);
        frame.setVisible(true);
    }

    private static final class CreateOrOpenActionListner implements ActionListener {

        private final Component parent;
        private final JLabel dbLabel;

        public CreateOrOpenActionListner(Component parent, JLabel dbLabel) {
            this.parent = parent;
            this.dbLabel = dbLabel;
        }

        @Override
        public void actionPerformed(ActionEvent event) {
            // TODO make it in thread
            try {
                CustomJFileChooser.create(parent).showCreateOrOpen();
                DbInitializer.createTableIfNotExists();
                Storage.getInstance().refresh(DaoSQLite.getInstance().readAll());
                dbLabel.setText(Settings.DB_FILE_PATH);
            } catch (IOException e) {
                Utils.logAndShowError(LOG, parent, "Error while create/open DB file.",
                        "Create/Open file error", e);
                System.exit(1);
            } catch (SQLException e) {
                Utils.logAndShowError(LOG, parent, "Error while read selected DB file.\n"
                        + e.getMessage(),
                        "Read selected DB error", e);
                System.exit(1);
            }
        }
    }
}

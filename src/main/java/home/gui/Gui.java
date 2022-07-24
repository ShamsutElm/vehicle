package home.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

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

import home.Main;
import home.Settings;
import home.Settings.Setting;
import home.Storage;
import home.gui.components.CustomJButton;
import home.gui.components.CustomJFrame;
import home.gui.components.CustomJPanel;
import home.gui.components.CustomJPanel.PanelType;
import home.gui.components.CustomJTable;
import home.gui.components.dialog.DialogCar;
import home.gui.components.dialog.DialogMoto;
import home.gui.components.dialog.DialogTruck;
import home.gui.listener.CreateOrOpenActionListener;
import home.gui.listener.SaveActionListener;
import home.models.AbstractVehicle;
import home.utils.ThreadUtils;
import home.utils.Utils;

public enum Gui {

    INSTANCE;

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

    public void refreshTable() {
        tableModel.fireTableDataChanged();
    }

    public void setDBLabel(String label) {
        dbLabel.setText(label);
    }

    public void buildGui() {
        setStyle(Settings.getStyle());

        createTable();
        createButtons();
        createPannels();
        createMenu();
        createFrame();
    }

    private void setStyle(String style) {
        try {
            ColorSchema colorSchema = ColorSchema.getColorSchema(style);
            if (colorSchema == null) {
                throw new IllegalArgumentException("Incorrect style name: " + style);
            }
            UIManager.setLookAndFeel(colorSchema.getLookAndFeelClassName());
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                Settings.writeSetting(Setting.STYLE,
                        ColorSchema.CROSSPLATFORM.name().toLowerCase(Locale.ROOT));
                Utils.logAndShowError(LOG, frame,
                        "Error while set the system color schema.\n"
                                + ColorSchema.CROSSPLATFORM.getNameForGui()
                                + " color schema will be used.\n"
                                + e.getMessage(),
                        "System color schema error", e);
            } catch (Exception ex) {
                JFrame.setDefaultLookAndFeelDecorated(true);
                Utils.logAndShowError(LOG, frame,
                        "Error while set Default color schema.\n" + ex.getMessage(),
                        "System color schema error", ex);
            }
        }
    }

    private void createTable() {
        dbLabel = new JLabel(Settings.hasPathToDBFile() ? Settings.getDbFilePath()
                : IGuiConsts.CHOOSE_DB_FILE);

        table = CustomJTable.create();
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table = (JTable) mouseEvent.getSource();
                if (CLICK_COUNT == mouseEvent.getClickCount()) {
                    int selectedTableRow = table.getSelectedRow();
                    DialogCaller.showObjDialog(frame,
                            Storage.INSTANCE.get(selectedTableRow), selectedTableRow);
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
            ThreadUtils.runInThread(() -> {
                Thread.currentThread().setName("-> delete action");
                List<AbstractVehicle> objsMarkedForDel = Storage.INSTANCE.getAll().stream()
                        .filter(dataObj -> dataObj.isMarkedForDelete())
                        .collect(Collectors.toList());
                if (!objsMarkedForDel.isEmpty()) {
                    DataActionInGui.delete(objsMarkedForDel);
                }
            });
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
        menuBar = new JMenuBar();

        JMenuItem createOrOpenItime = createMenuItem(IGuiConsts.CREATE_OR_OPEN,
                new CreateOrOpenActionListener(frame, dbLabel, LOG));
        JMenuItem saveItem = createMenuItem(IGuiConsts.SAVE, new SaveActionListener(frame, dbLabel, false, LOG));
        JMenuItem saveAsItem = createMenuItem(IGuiConsts.SAVE_AS, new SaveActionListener(frame, dbLabel, true, LOG));
        var fileMenu = new JMenu(IGuiConsts.FILE);
        fileMenu.add(createOrOpenItime);
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        menuBar.add(fileMenu);

        menuBar.add(creatStyleMenu());

        JMenuItem aboutItem = createMenuItem(IGuiConsts.ABOUT, actionEvent -> JOptionPane.showMessageDialog(
                frame, IGuiConsts.ABOUT_TEXT.formatted(Main.appVersion),
                IGuiConsts.ABOUT_TITLE, JOptionPane.INFORMATION_MESSAGE));
        var helpMenu = new JMenu(IGuiConsts.HELP);
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);
    }

    private JMenuItem createMenuItem(String name, ActionListener listener) {
        var menuItem = new JMenuItem(name);
        menuItem.addActionListener(listener);
        return menuItem;
    }

    private JMenu creatStyleMenu() {
        var styleMenu = new JMenu(IGuiConsts.STYLE);
        var checkBoxItems = new ArrayList<JCheckBoxMenuItem>();
        for (ColorSchema colorSchema : ColorSchema.values()) {
            styleMenu.add(createCheckBoxMenuItem(colorSchema, checkBoxItems));
        }
        return styleMenu;
    }

    private JCheckBoxMenuItem createCheckBoxMenuItem(ColorSchema colorSchema,
            List<JCheckBoxMenuItem> checkBoxItems) {
        var checkBoxMenuItem = new JCheckBoxMenuItem(colorSchema.getNameForGui());
        checkBoxMenuItem.setSelected(colorSchema.name().equalsIgnoreCase(Settings.getStyle()));
        checkBoxItems.add(checkBoxMenuItem);
        checkBoxMenuItem.addActionListener(actionEvent -> styleSelectAction(actionEvent, checkBoxItems));
        return checkBoxMenuItem;
    }

    private void styleSelectAction(ActionEvent actionEvent,
            List<JCheckBoxMenuItem> checkBoxItems) {
        try {
            checkBoxItems.stream().forEach(item -> item.setSelected(false));

            var selectedItem = (JCheckBoxMenuItem) actionEvent.getSource();
            selectedItem.setSelected(true);

            Settings.writeSetting(Setting.STYLE,
                    selectedItem.getText().toLowerCase(Locale.ROOT));

            setStyle(Settings.getStyle());
            SwingUtilities.updateComponentTreeUI(frame);
        } catch (Exception e) {
            Utils.logAndShowError(LOG, frame, "Error while chose style", "Style error", e);
        }
    }

    private void createFrame() {
        frame = CustomJFrame.create(Main.appName);
        frame.setJMenuBar(menuBar);
        frame.getContentPane().add(panelTable, BorderLayout.CENTER);
        frame.getContentPane().add(panelButton, BorderLayout.EAST);
        frame.setVisible(true);
    }
}

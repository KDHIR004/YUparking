package yuparking.gui.Manager;

import yuparking.models.User;
import yuparking.services.ParkingLotService;
import yuparking.gui.Login.ManagerDashboardGUI;
import yuparking.database.Database;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ParkingSpaceGUI {
    private JFrame frame;
    private JPanel panel;
    private ParkingLotService parkingLotService;
    private Database db;
    private User currentUser;

    public ParkingSpaceGUI(User user) {
        this.currentUser = user;
        this.parkingLotService = new ParkingLotService();
        this.db = new Database();
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Parking Lots and Spaces");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] lotColumns = {"Lot ID", "Location", "Capacity", "Status"};
        DefaultTableModel lotModel = new DefaultTableModel(lotColumns, 0);
        JTable lotTable = new JTable(lotModel);
        JScrollPane lotScrollPane = new JScrollPane(lotTable);
        lotScrollPane.setBorder(BorderFactory.createTitledBorder("Parking Lots"));

        String[] spaceColumns = {"Space ID", "Lot ID", "Status"};
        DefaultTableModel spaceModel = new DefaultTableModel(spaceColumns, 0);
        JTable spaceTable = new JTable(spaceModel);
        JScrollPane spaceScrollPane = new JScrollPane(spaceTable);
        spaceScrollPane.setBorder(BorderFactory.createTitledBorder("Parking Spaces"));

        List<String[]> lots = db.retrieveData("parkinglots");
        for (int i = 1; i < lots.size(); i++) {
            String[] row = lots.get(i);
            lotModel.addRow(row);
        }

        List<String[]> spaces = db.retrieveData("parkingspaces");
        for (int i = 1; i < spaces.size(); i++) {
            String[] row = spaces.get(i);
            spaceModel.addRow(row);
        }

        JButton backButton = new JButton("Return to Dashboard");
        backButton.addActionListener(e -> {
            frame.dispose();
            new ManagerDashboardGUI(currentUser);
        });

        JPanel tablesPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        tablesPanel.add(lotScrollPane);
        tablesPanel.add(spaceScrollPane);

        panel.add(tablesPanel, BorderLayout.CENTER);
        panel.add(backButton, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setVisible(true);
    }
}

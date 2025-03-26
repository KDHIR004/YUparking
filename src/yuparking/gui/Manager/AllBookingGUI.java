package yuparking.gui.Manager;

import yuparking.models.User;
import yuparking.services.ManagementService;
import yuparking.gui.Login.ManagerDashboardGUI;
import yuparking.database.Database;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AllBookingGUI {
    private JFrame frame;
    private JPanel panel;
    private ManagementService managementService;
    private Database db;
    private User currentUser;

    public AllBookingGUI(User user) {
        this.currentUser = user;
        this.managementService = new ManagementService();
        this.db = new Database();
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("All Bookings");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columns = {"Booking ID", "User ID", "Space ID", "Start Time", "End Time", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        List<String[]> bookings = db.retrieveData("bookings");
        for (int i = 1; i < bookings.size(); i++) {
            String[] row = bookings.get(i);
            model.addRow(row);
        }

        JButton backButton = new JButton("Return to Dashboard");
        backButton.addActionListener(e -> {
            frame.dispose();
            new ManagerDashboardGUI(currentUser);
        });

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(backButton, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setVisible(true);
    }
}

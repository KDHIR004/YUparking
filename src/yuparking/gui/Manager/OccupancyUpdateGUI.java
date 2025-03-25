package yuparking.gui.Manager;

import yuparking.models.User;
import yuparking.services.ParkingSensorService;
import yuparking.services.ParkingLotService;
import yuparking.gui.Login.ManagerDashboardGUI;

import javax.swing.*;
import java.awt.*;

public class OccupancyUpdateGUI {
    private JFrame frame;
    private JPanel panel;
    private JTextField spaceIdField;
    private JComboBox<String> statusComboBox;
    private ParkingSensorService sensorService;
    private ParkingLotService parkingLotService;
    private User currentUser;

    public OccupancyUpdateGUI(User user) {
        this.currentUser = user;
        this.sensorService = new ParkingSensorService();
        this.parkingLotService = new ParkingLotService();
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Occupancy Update");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Space ID input
        panel.add(new JLabel("Space ID:"));
        spaceIdField = new JTextField();
        panel.add(spaceIdField);

        // Status dropdown
        panel.add(new JLabel("New Status:"));
        String[] statuses = {"occupied", "vacant", "maintenance"};
        statusComboBox = new JComboBox<>(statuses);
        panel.add(statusComboBox);

        // Simulate button
        JButton simulateButton = new JButton("Simulate Occupancy Update");
        simulateButton.addActionListener(e -> handleSimulateUpdate());
        panel.add(simulateButton);

        // Update status button
        JButton updateButton = new JButton("Update Space Status");
        updateButton.addActionListener(e -> handleManualUpdate());
        panel.add(updateButton);

        // Back button
        JButton backButton = new JButton("Return to Dashboard");
        backButton.addActionListener(e -> {
            frame.dispose();
            new ManagerDashboardGUI(currentUser);
        });
        panel.add(backButton);

        frame.add(panel);
        frame.setVisible(true);
    }

    private void handleSimulateUpdate() {
        sensorService.simulateOccupancyUpdate();
        JOptionPane.showMessageDialog(frame, 
            "Occupancy simulation completed!", 
            "Success", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleManualUpdate() {
        try {
            int spaceId = Integer.parseInt(spaceIdField.getText());
            String newStatus = (String) statusComboBox.getSelectedItem();
            parkingLotService.updateSpaceStatus(spaceId, newStatus);
            JOptionPane.showMessageDialog(frame, 
                "Space status updated successfully!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame,
                "Please enter a valid Space ID (number)",
                "Invalid Input",
                JOptionPane.ERROR_MESSAGE);
        }
    }
} 
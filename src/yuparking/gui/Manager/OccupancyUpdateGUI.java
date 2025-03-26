package yuparking.gui.Manager;

import yuparking.models.User;
import yuparking.services.ParkingSensorService;
import yuparking.gui.Login.ManagerDashboardGUI;

import javax.swing.*;
import java.awt.*;

public class OccupancyUpdateGUI {
    private JFrame frame;
    private JPanel panel;
    private ParkingSensorService sensorService;
    private User currentUser;

    public OccupancyUpdateGUI(User user) {
        this.currentUser = user;
        this.sensorService = new ParkingSensorService();
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Simulate Occupancy Update");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Simulate button
        JButton simulateButton = new JButton("Simulate Sensor Update");
        simulateButton.addActionListener(e -> handleSimulation());
        panel.add(simulateButton);

        // Back button
        JButton backButton = new JButton("Return to Dashboard");
        backButton.addActionListener(e -> frame.dispose());
        panel.add(backButton);

        frame.add(panel);
        frame.setVisible(true);
    }

    private void handleSimulation() {
        try {
            sensorService.simulateOccupancyUpdate();

            int choice = JOptionPane.showOptionDialog(frame,
                    "Occupancy statuses updated from simulated sensors.",
                    "Simulation Success",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new String[]{"Return to Home"},
                    "Return to Home");

            if (choice == 0) {
                frame.dispose();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame,
                    "Error simulating occupancy update: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
} 
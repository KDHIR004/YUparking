package yuparking.gui.Manager;

import yuparking.models.User;
import yuparking.services.ParkingLotService;
import yuparking.gui.Login.ManagerDashboardGUI;

import javax.swing.*;
import java.awt.*;

public class AddNewLotGUI {
    private JFrame frame;
    private JPanel panel;
    private JTextField lotIdField;
    private JTextField locationField;
    private JTextField capacityField;
    private ParkingLotService parkingLotService;
    private User currentUser;

    public AddNewLotGUI(User user) {
        this.currentUser = user;
        this.parkingLotService = new ParkingLotService();
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Add New Parking Lot");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Input fields
        panel.add(new JLabel("Lot ID:"));
        lotIdField = new JTextField();
        panel.add(lotIdField);

        panel.add(new JLabel("Location:"));
        locationField = new JTextField();
        panel.add(locationField);

        panel.add(new JLabel("Capacity:"));
        capacityField = new JTextField();
        panel.add(capacityField);

        // Add button
        JButton addButton = new JButton("Add Lot");
        addButton.addActionListener(e -> handleAddLot());
        panel.add(addButton);

        // Back button
        JButton backButton = new JButton("Return to Dashboard");
        backButton.addActionListener(e -> frame.dispose());
        panel.add(backButton);

        frame.add(panel);
        frame.setVisible(true);
    }

    private void handleAddLot() {
        try {
            int lotId = Integer.parseInt(lotIdField.getText());
            String location = locationField.getText();
            int capacity = Integer.parseInt(capacityField.getText());

            parkingLotService.addNewParkingLot(lotId, location, capacity);

            int choice = JOptionPane.showOptionDialog(frame,
                    "New parking lot added successfully!",
                    "Success",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new String[]{"Return to Home"},
                    "Return to Home");

            if (choice == 0) {
                frame.dispose();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame,
                    "Please enter valid numbers for Lot ID and Capacity",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame,
                    "Error adding parking lot: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
} 
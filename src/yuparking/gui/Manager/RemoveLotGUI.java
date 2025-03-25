package yuparking.gui.Manager;

import yuparking.models.User;
import yuparking.services.ParkingLotService;
import yuparking.gui.Login.ManagerDashboardGUI;

import javax.swing.*;
import java.awt.*;

public class RemoveLotGUI {
    private JFrame frame;
    private JPanel panel;
    private JTextField lotIdField;
    private ParkingLotService parkingLotService;
    private User currentUser;

    public RemoveLotGUI(User user) {
        this.currentUser = user;
        this.parkingLotService = new ParkingLotService();
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Remove Parking Lot");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Lot ID input
        panel.add(new JLabel("Lot ID:"));
        lotIdField = new JTextField();
        panel.add(lotIdField);

        // Remove button
        JButton removeButton = new JButton("Remove Lot");
        removeButton.addActionListener(e -> handleRemoveLot());
        panel.add(removeButton);

        // Back button
        JButton backButton = new JButton("Return to Dashboard");
        backButton.addActionListener(e -> frame.dispose());
        panel.add(backButton);

        frame.add(panel);
        frame.setVisible(true);
    }

    private void handleRemoveLot() {
        try {
            int lotId = Integer.parseInt(lotIdField.getText());
            
            parkingLotService.removeParkingLot(lotId);
            
            int choice = JOptionPane.showOptionDialog(frame,
                "Parking lot removed successfully!",
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
                "Please enter a valid Lot ID (number)",
                "Invalid Input",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame,
                "Error removing parking lot: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
} 
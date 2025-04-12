package yuparking.gui.Manager;

import yuparking.models.User;
import yuparking.services.ParkingLotService;
import yuparking.gui.Login.ManagerDashboardGUI;
import yuparking.database.Database;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ManualSpaceStatusGUI {
    private JFrame frame;
    private JPanel panel;
    private JTextField spaceIdField;
    private JComboBox<String> statusComboBox;
    private ParkingLotService parkingLotService;
    private Database db;
    private User currentUser;

    public ManualSpaceStatusGUI(User user) {
        this.currentUser = user;
        this.parkingLotService = new ParkingLotService();
        this.db = new Database();
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Manually Update Space Status");
        frame.setSize(400, 250);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Space ID input
        panel.add(new JLabel("Space ID:"));
        spaceIdField = new JTextField();
        panel.add(spaceIdField);

        // Status selection
        panel.add(new JLabel("New Status:"));
        String[] statuses = {"occupied", "vacant", "maintenance"};
        statusComboBox = new JComboBox<>(statuses);
        panel.add(statusComboBox);

        // Update button
        JButton updateButton = new JButton("Update Status");
        updateButton.addActionListener(e -> handleStatusUpdate());
        panel.add(updateButton);

        // Back button
        JButton backButton = new JButton("Return to Dashboard");
        backButton.addActionListener(e -> frame.dispose());
        panel.add(backButton);

        frame.add(panel);
        frame.setVisible(true);
    }

    private void handleStatusUpdate() {
        try {
            int spaceId = Integer.parseInt(spaceIdField.getText());
            String newStatus = (String) statusComboBox.getSelectedItem();

            // Check if space exists
            List<String[]> spaces = db.retrieveData("parkingspaces");
            boolean spaceFound = false;
            for (int i = 1; i < spaces.size(); i++) {
                if (Integer.parseInt(spaces.get(i)[0]) == spaceId) {
                    spaceFound = true;
                    break;
                }
            }

            if (!spaceFound) {
                JOptionPane.showMessageDialog(frame,
                        "Space not found in database.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            parkingLotService.updateSpaceStatus(spaceId, newStatus);

            int choice = JOptionPane.showOptionDialog(frame,
                    "Space status updated successfully!",
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
                    "Please enter a valid Space ID (number)",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame,
                    "Error updating space status: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
} 
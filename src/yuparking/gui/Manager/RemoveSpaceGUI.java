package yuparking.gui.Manager;

import yuparking.models.User;
import yuparking.services.ParkingLotService;
import yuparking.gui.Login.ManagerDashboardGUI;
import yuparking.database.Database;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RemoveSpaceGUI {
    private JFrame frame;
    private JPanel panel;
    private JTextField spaceIdField;
    private ParkingLotService parkingLotService;
    private Database db;
    private User currentUser;

    public RemoveSpaceGUI(User user) {
        this.currentUser = user;
        this.parkingLotService = new ParkingLotService();
        this.db = new Database();
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Remove Parking Space");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Space ID:"));
        spaceIdField = new JTextField();
        panel.add(spaceIdField);

        JButton removeButton = new JButton("Remove Space");
        removeButton.addActionListener(e -> handleRemoveSpace());
        panel.add(removeButton);

        JButton backButton = new JButton("Return to Dashboard");
        backButton.addActionListener(e -> {
            frame.dispose();
            new ManagerDashboardGUI(currentUser);
        });
        panel.add(backButton);

        frame.add(panel);
        frame.setVisible(true);
    }

    private void handleRemoveSpace() {
        try {
            int spaceId = Integer.parseInt(spaceIdField.getText());

            List<String[]> spaces = db.retrieveData("parkingspaces");
            boolean spaceExists = false;
            for (int i = 1; i < spaces.size(); i++) {
                String[] row = spaces.get(i);
                if (Integer.parseInt(row[0]) == spaceId) {
                    spaceExists = true;
                    break;
                }
            }

            if (!spaceExists) {
                JOptionPane.showMessageDialog(frame,
                        "Space ID " + spaceId + " not found.",
                        "Space Not Found",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            parkingLotService.removeSpace(spaceId);
            JOptionPane.showMessageDialog(frame,
                    "Space " + spaceId + " removed successfully!",
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

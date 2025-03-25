package yuparking.gui.Manager;

import yuparking.models.User;
import yuparking.services.ParkingLotService;
import yuparking.gui.Login.ManagerDashboardGUI;
import yuparking.database.Database;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AddSpaceGUI {
    private JFrame frame;
    private JPanel panel;
    private JTextField lotIdField;
    private JTextField numberOfSpacesField;
    private ParkingLotService parkingLotService;
    private Database db;
    private User currentUser;

    public AddSpaceGUI(User user) {
        this.currentUser = user;
        this.parkingLotService = new ParkingLotService();
        this.db = new Database();
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Add Parking Spaces");
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

        // Number of spaces input
        panel.add(new JLabel("Number of Spaces:"));
        numberOfSpacesField = new JTextField();
        panel.add(numberOfSpacesField);

        // Add spaces button
        JButton addButton = new JButton("Add Spaces");
        addButton.addActionListener(e -> handleAddSpaces());
        panel.add(addButton);

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

    private void handleAddSpaces() {
        try {
            int lotId = Integer.parseInt(lotIdField.getText());
            int numberOfSpaces = Integer.parseInt(numberOfSpacesField.getText());

            // Validate lot exists and is active
            List<String[]> lots = db.retrieveData("parkinglots");
            boolean lotExistsAndActive = false;
            for (int i = 1; i < lots.size(); i++) {
                String[] row = lots.get(i);
                if (Integer.parseInt(row[0]) == lotId && row[3].equalsIgnoreCase("active")) {
                    lotExistsAndActive = true;
                    break;
                }
            }

            if (!lotExistsAndActive) {
                JOptionPane.showMessageDialog(frame,
                    "Lot ID " + lotId + " either does not exist or is not active.",
                    "Invalid Lot",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Add spaces
            parkingLotService.addSpace(lotId, numberOfSpaces);
            JOptionPane.showMessageDialog(frame, 
                numberOfSpaces + " space(s) added successfully to lot " + lotId, 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame,
                "Please enter valid numbers for Lot ID and Number of Spaces",
                "Invalid Input",
                JOptionPane.ERROR_MESSAGE);
        }
    }
} 
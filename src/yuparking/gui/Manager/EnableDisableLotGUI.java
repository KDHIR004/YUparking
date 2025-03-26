package yuparking.gui.Manager;

import yuparking.models.User;
import yuparking.services.ParkingLotService;
import yuparking.gui.Login.ManagerDashboardGUI;

import javax.swing.*;
import java.awt.*;

public class EnableDisableLotGUI {
    private JFrame frame;
    private JPanel panel;
    private JTextField lotIdField;
    private ParkingLotService parkingLotService;
    private User currentUser;
    private boolean isEnable;

    public EnableDisableLotGUI(User user, boolean isEnable) {
        this.currentUser = user;
        this.parkingLotService = new ParkingLotService();
        this.isEnable = isEnable;
        initializeUI();
    }

    private void initializeUI() {
        String title = isEnable ? "Enable Parking Lot" : "Disable Parking Lot";
        frame = new JFrame(title);
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Lot ID:"));
        lotIdField = new JTextField();
        panel.add(lotIdField);

        String buttonText = isEnable ? "Enable Lot" : "Disable Lot";
        JButton actionButton = new JButton(buttonText);
        actionButton.addActionListener(e -> handleLotAction());
        panel.add(actionButton);

        JButton backButton = new JButton("Return to Dashboard");
        backButton.addActionListener(e -> frame.dispose());
        panel.add(backButton);

        frame.add(panel);
        frame.setVisible(true);
    }

    private void handleLotAction() {
        try {
            int lotId = Integer.parseInt(lotIdField.getText());

            if (isEnable) {
                parkingLotService.enableLot(lotId);
                int choice = JOptionPane.showOptionDialog(frame,
                        "Parking lot enabled successfully!",
                        "Success",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        new String[]{"Return to Home"},
                        "Return to Home");

                if (choice == 0) {
                    frame.dispose();
                }
            } else {
                parkingLotService.disableLot(lotId);
                int choice = JOptionPane.showOptionDialog(frame,
                        "Parking lot disabled successfully!",
                        "Success",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        new String[]{"Return to Home"},
                        "Return to Home");

                if (choice == 0) {
                    frame.dispose();
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame,
                    "Please enter a valid Lot ID (number)",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame,
                    "Error " + (isEnable ? "enabling" : "disabling") + " parking lot: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
} 

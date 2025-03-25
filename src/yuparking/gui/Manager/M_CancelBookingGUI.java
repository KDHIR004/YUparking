package yuparking.gui.Manager;

import yuparking.models.User;
import yuparking.services.ManagementService;
import yuparking.gui.Login.ManagerDashboardGUI;

import javax.swing.*;
import java.awt.*;

public class M_CancelBookingGUI {
    private JFrame frame;
    private JPanel panel;
    private JTextField bookingIdField;
    private ManagementService managementService;
    private User currentUser;

    public M_CancelBookingGUI(User user) {
        this.currentUser = user;
        this.managementService = new ManagementService();
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Cancel Any Booking");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Booking ID input
        panel.add(new JLabel("Booking ID:"));
        bookingIdField = new JTextField();
        panel.add(bookingIdField);

        // Cancel button
        JButton cancelBookingButton = new JButton("Cancel Booking");
        cancelBookingButton.addActionListener(e -> handleCancelBooking());
        panel.add(cancelBookingButton);

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

    private void handleCancelBooking() {
        try {
            int bookingId = Integer.parseInt(bookingIdField.getText());
            
            // Cancel the booking
            managementService.cancelAnyBooking(bookingId);
            
            JOptionPane.showMessageDialog(frame, 
                "Booking cancelled successfully!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            
            frame.dispose();
            new ManagerDashboardGUI(currentUser);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame,
                "Please enter a valid Booking ID (number)",
                "Invalid Input",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame,
                "Error cancelling booking: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
package yuparking.gui.Manager;

import yuparking.models.User;
import yuparking.services.ManagementService;
import yuparking.gui.Login.ManagerDashboardGUI;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class M_ModifyBookingGUI {
    private JFrame frame;
    private JPanel panel;
    private JTextField bookingIdField;
    private JTextField newDateField;
    private JTextField newStartTimeField;
    private JTextField newEndTimeField;
    private ManagementService managementService;
    private User currentUser;

    public M_ModifyBookingGUI(User user) {
        this.currentUser = user;
        this.managementService = new ManagementService();
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Modify Any Booking");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Booking ID input
        panel.add(new JLabel("Booking ID:"));
        bookingIdField = new JTextField();
        panel.add(bookingIdField);

        // New date input
        panel.add(new JLabel("New Date (yyyy-MM-dd):"));
        newDateField = new JTextField();
        panel.add(newDateField);

        // New start time input
        panel.add(new JLabel("Start Time (HH:mm):"));
        newStartTimeField = new JTextField();
        panel.add(newStartTimeField);

        // New end time input
        panel.add(new JLabel("End Time (HH:mm):"));
        newEndTimeField = new JTextField();
        panel.add(newEndTimeField);

        // Modify button
        JButton modifyButton = new JButton("Modify Booking");
        modifyButton.addActionListener(e -> handleModifyBooking());
        panel.add(modifyButton);

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

    private void handleModifyBooking() {
        try {
            int bookingId = Integer.parseInt(bookingIdField.getText());
            String newDate = newDateField.getText();
            String newStartTime = newStartTimeField.getText();
            String newEndTime = newEndTimeField.getText();

            // Validate date format
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            dateFormat.parse(newDate);

            // Validate time format
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            timeFormat.setLenient(false);
            timeFormat.parse(newStartTime);
            timeFormat.parse(newEndTime);

            // Format times to ensure HH:mm format
            String[] startTimeParts = newStartTime.split(":");
            String formattedStartHour = String.format("%02d", Integer.parseInt(startTimeParts[0]));
            String formattedStartMinute = String.format("%02d", Integer.parseInt(startTimeParts[1]));
            String formattedStartTime = formattedStartHour + ":" + formattedStartMinute;

            String[] endTimeParts = newEndTime.split(":");
            String formattedEndHour = String.format("%02d", Integer.parseInt(endTimeParts[0]));
            String formattedEndMinute = String.format("%02d", Integer.parseInt(endTimeParts[1]));
            String formattedEndTime = formattedEndHour + ":" + formattedEndMinute;

            // Combine date and times into ISO format
            String combinedStartDateTime = newDate + "T" + formattedStartTime + ":00";
            String combinedEndDateTime = newDate + "T" + formattedEndTime + ":00";

            // Modify the booking
            managementService.modifyAnyBooking(bookingId, combinedStartDateTime, combinedEndDateTime);

            JOptionPane.showMessageDialog(frame,
                "Booking modified successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

            frame.dispose();
            new ManagerDashboardGUI(currentUser);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame,
                "Please enter a valid Booking ID (number)",
                "Invalid Input",
                JOptionPane.ERROR_MESSAGE);
        } catch (java.text.ParseException e) {
            JOptionPane.showMessageDialog(frame,
                "Invalid date or time format. Please use yyyy-MM-dd for date and HH:mm for time.",
                "Invalid Format",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame,
                "Error modifying booking: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
package yuparking.gui.UserBooking;

import yuparking.models.User;
import yuparking.services.UserBookingService;
import yuparking.services.ParkingLotService;
import yuparking.database.Database;
import yuparking.gui.Login.BookingMenuGUI;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CancelBookingGUI {
    private JFrame frame;
    private JPanel panel;
    private JTextField bookingIdField;
    private UserBookingService userBookingService;
    private ParkingLotService parkingLotService;
    private User currentUser;
    private Database db;

    public CancelBookingGUI(User user) {
        this.currentUser = user;
        this.userBookingService = new UserBookingService();
        this.parkingLotService = new ParkingLotService();
        this.db = new Database();
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Cancel Booking");
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
        JButton backButton = new JButton("Return to Home");
        backButton.addActionListener(e -> {
            frame.dispose();
            new BookingMenuGUI(currentUser);
        });
        panel.add(backButton);

        frame.add(panel);
        frame.setVisible(true);
    }

    private void handleCancelBooking() {
        try {
            int bookingId = Integer.parseInt(bookingIdField.getText());

            // Check if booking exists and belongs to user
            List<String[]> bookings = db.retrieveData("bookings");
            boolean bookingFound = false;
            boolean belongsToUser = false;
            boolean isAlreadyCancelled = false;
            int spaceId = -1;
            int bookingIndex = -1;

            for (int i = 1; i < bookings.size(); i++) {
                String[] row = bookings.get(i);
                if (row[0].equals(String.valueOf(bookingId))) {
                    bookingFound = true;
                    bookingIndex = i;
                    int bookingUserId = Integer.parseInt(row[1]);
                    belongsToUser = (bookingUserId == currentUser.getUserID());
                    isAlreadyCancelled = row[5].equalsIgnoreCase("Cancelled");
                    spaceId = Integer.parseInt(row[2]);  // Get space ID from booking
                    break;
                }
            }

            if (!bookingFound) {
                JOptionPane.showMessageDialog(frame,
                        "Booking not found. Please check the Booking ID.",
                        "Invalid Booking",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!belongsToUser) {
                JOptionPane.showMessageDialog(frame,
                        "This booking does not belong to your account.",
                        "Unauthorized",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (isAlreadyCancelled) {
                JOptionPane.showMessageDialog(frame,
                        "This booking is already cancelled.",
                        "Already Cancelled",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Update booking status to Cancelled
            String[] bookingToUpdate = bookings.get(bookingIndex);
            bookingToUpdate[5] = "Cancelled";
            bookings.set(bookingIndex, bookingToUpdate);

            // Update the database
            db.confirmUpdate("bookings", bookings);

            // Update space status to vacant
            if (spaceId != -1) {
                parkingLotService.updateSpaceStatus(spaceId, "vacant");
            }

            JOptionPane.showMessageDialog(frame,
                    "Booking cancelled successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            frame.dispose();
            new BookingMenuGUI(currentUser);
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
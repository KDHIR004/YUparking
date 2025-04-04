package yuparking.gui.UserBooking;

import yuparking.models.User;
import yuparking.services.UserBookingService;
import yuparking.services.ParkingLotService;
import yuparking.database.Database;
import yuparking.gui.Login.BookingMenuGUI;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ModifyBookingGUI {
    private JFrame frame;
    private JPanel panel;
    private JTextField bookingIdField;
    private JTextField newDateField;
    private JTextField newStartTimeField;
    private JTextField newEndTimeField;
    private UserBookingService userBookingService;
    private ParkingLotService parkingLotService;
    private User currentUser;
    private Database db;

    public ModifyBookingGUI(User user) {
        this.currentUser = user;
        this.userBookingService = new UserBookingService();
        this.parkingLotService = new ParkingLotService();
        this.db = new Database();
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Modify Booking");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Booking ID:"));
        bookingIdField = new JTextField();
        panel.add(bookingIdField);

        panel.add(new JLabel("New Date (yyyy-MM-dd):"));
        newDateField = new JTextField();
        panel.add(newDateField);

        panel.add(new JLabel("Start Time (HH:mm):"));
        newStartTimeField = new JTextField();
        panel.add(newStartTimeField);

        panel.add(new JLabel("End Time (HH:mm):"));
        newEndTimeField = new JTextField();
        panel.add(newEndTimeField);

        JButton modifyButton = new JButton("Modify Booking");
        modifyButton.addActionListener(e -> handleModifyBooking());
        panel.add(modifyButton);

        JButton backButton = new JButton("Return to Home");
        backButton.addActionListener(e -> {
            frame.dispose();
            new BookingMenuGUI(currentUser);
        });
        panel.add(backButton);

        frame.add(panel);
        frame.setVisible(true);
    }

    private boolean isBookingEligibleForModification(int bookingId) {
        List<String[]> bookings = db.retrieveData("bookings");
        for (int i = 1; i < bookings.size(); i++) {
            String[] row = bookings.get(i);
            if (row[0].equals(String.valueOf(bookingId))) {
                int bookingUserId = Integer.parseInt(row[1]);
                if (bookingUserId == currentUser.getUserID() && !row[5].equalsIgnoreCase("Cancelled")) {
                    return true;
                }
            }
        }
        return false;
    }

    private void handleModifyBooking() {
        try {
            int bookingId = Integer.parseInt(bookingIdField.getText());
            String newDate = newDateField.getText();
            String newStartTime = newStartTimeField.getText();
            String newEndTime = newEndTimeField.getText();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            dateFormat.parse(newDate);

            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            timeFormat.setLenient(false);
            timeFormat.parse(newStartTime);
            timeFormat.parse(newEndTime);

            List<String[]> bookings = db.retrieveData("bookings");
            boolean found = false;
            int bookingIndex = -1;

            for (int i = 1; i < bookings.size(); i++) {
                String[] row = bookings.get(i);
                if (row[0].equals(String.valueOf(bookingId))) {
                    int bookingUserId = Integer.parseInt(row[1]);
                    if (bookingUserId == currentUser.getUserID() && !row[5].equalsIgnoreCase("Cancelled")) {
                        found = true;
                        bookingIndex = i;
                        break;
                    }
                }
            }

            if (!found) {
                JOptionPane.showMessageDialog(frame,
                        "Booking not found or is not eligible for modification. Only active bookings can be modified.",
                        "Invalid Booking",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String[] startTimeParts = newStartTime.split(":");
            String formattedStartHour = String.format("%02d", Integer.parseInt(startTimeParts[0]));
            String formattedStartMinute = String.format("%02d", Integer.parseInt(startTimeParts[1]));
            String formattedStartTime = formattedStartHour + ":" + formattedStartMinute;

            String[] endTimeParts = newEndTime.split(":");
            String formattedEndHour = String.format("%02d", Integer.parseInt(endTimeParts[0]));
            String formattedEndMinute = String.format("%02d", Integer.parseInt(endTimeParts[1]));
            String formattedEndTime = formattedEndHour + ":" + formattedEndMinute;

            String combinedStartDateTime = newDate + "T" + formattedStartTime + ":00";
            String combinedEndDateTime = newDate + "T" + formattedEndTime + ":00";

            String[] bookingToUpdate = bookings.get(bookingIndex);
            bookingToUpdate[3] = combinedStartDateTime; 
            bookingToUpdate[4] = combinedEndDateTime;    
            bookingToUpdate[5] = "Modified";             
            bookings.set(bookingIndex, bookingToUpdate);

            db.confirmUpdate("bookings", bookings);

            JOptionPane.showMessageDialog(frame,
                    String.format("Booking modified successfully!\n" +
                                    "Booking ID: %d\n" +
                                    "New Start Time: %s\n" +
                                    "New End Time: %s",
                            bookingId, formattedStartTime, formattedEndTime),
                    "Modification Confirmation",
                    JOptionPane.INFORMATION_MESSAGE);

            frame.dispose();
            new BookingMenuGUI(currentUser);
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

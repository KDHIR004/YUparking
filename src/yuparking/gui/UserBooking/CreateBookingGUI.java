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

public class CreateBookingGUI {
    private JFrame frame;
    private JPanel panel;
    private JTextField spaceIdField;
    private JTextField dateField;
    private JTextField timeField;
    private UserBookingService userBookingService;
    private ParkingLotService parkingLotService;
    private User currentUser;
    private Database db;

    public CreateBookingGUI(User user) {
        this.currentUser = user;
        this.userBookingService = new UserBookingService();
        this.parkingLotService = new ParkingLotService();
        this.db = new Database();
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Create Booking");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Space ID input
        panel.add(new JLabel("Space ID:"));
        spaceIdField = new JTextField();
        panel.add(spaceIdField);

        // Date input
        panel.add(new JLabel("Date (yyyy-MM-dd):"));
        dateField = new JTextField();
        panel.add(dateField);

        // Time input
        panel.add(new JLabel("Time (HH:mm):"));
        timeField = new JTextField();
        panel.add(timeField);

        // Create button
        JButton createButton = new JButton("Create Booking");
        createButton.addActionListener(e -> handleCreateBooking());
        panel.add(createButton);

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

    private boolean isSpaceAvailable(int spaceId) {
        List<String[]> spaces = db.retrieveData("parking_spaces");
        for (int i = 1; i < spaces.size(); i++) {
            String[] row = spaces.get(i);
            if (row[0].equals(String.valueOf(spaceId))) {
                return row[3].equalsIgnoreCase("vacant");
            }
        }
        return false;
    }

    private void handleCreateBooking() {
        try {
            int spaceId = Integer.parseInt(spaceIdField.getText());
            String date = dateField.getText();
            String time = timeField.getText();

            // Validate date format
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            dateFormat.parse(date);

            // Validate time format
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            timeFormat.setLenient(false);
            timeFormat.parse(time);

            // Check if user is verified
            if (!currentUser.isVerified()) {
                JOptionPane.showMessageDialog(frame,
                    "Please verify your email before creating a booking.",
                    "Unverified User",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check if space is available
            if (!isSpaceAvailable(spaceId)) {
                JOptionPane.showMessageDialog(frame,
                    "Selected parking space is not available.",
                    "Space Unavailable",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Format time to ensure HH:mm format
            String[] timeParts = time.split(":");
            String formattedHour = String.format("%02d", Integer.parseInt(timeParts[0]));
            String formattedMinute = String.format("%02d", Integer.parseInt(timeParts[1]));
            String formattedTime = formattedHour + ":" + formattedMinute;

            // Combine date and time into ISO format
            String combinedDateTime = date + "T" + formattedTime + ":00";

            // Create the booking
            userBookingService.createUserBooking(currentUser, spaceId, combinedDateTime, combinedDateTime);

            // Update space status to occupied
            parkingLotService.updateSpaceStatus(spaceId, "occupied");

            JOptionPane.showMessageDialog(frame,
                "Booking created successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

            frame.dispose();
            new BookingMenuGUI(currentUser);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame,
                "Please enter valid numbers for Space ID",
                "Invalid Input",
                JOptionPane.ERROR_MESSAGE);
        } catch (java.text.ParseException e) {
            JOptionPane.showMessageDialog(frame,
                "Invalid date or time format. Please use yyyy-MM-dd for date and HH:mm for time.",
                "Invalid Format",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame,
                "Error creating booking: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
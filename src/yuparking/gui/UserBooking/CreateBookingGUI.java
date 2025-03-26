package yuparking.gui.UserBooking;

import yuparking.models.User;
import yuparking.services.UserBookingService;
import yuparking.services.ParkingLotService;
import yuparking.database.Database;
import yuparking.gui.Login.BookingMenuGUI;
import yuparking.gui.UserBooking.PaymentGUI;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.io.File;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class CreateBookingGUI {
    private JFrame frame;
    private JPanel panel;
    private JTextField spaceIdField;
    private JTextField dateField;
    private JTextField timeField;
    private JTextField endTimeField;
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
        frame.setSize(400, 350);  // Increased height for new fields
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2, 5, 5));  // Added row for end time
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Space ID input
        panel.add(new JLabel("Space ID:"));
        spaceIdField = new JTextField();
        panel.add(spaceIdField);

        // Date input
        panel.add(new JLabel("Date (yyyy-MM-dd):"));
        dateField = new JTextField();
        panel.add(dateField);

        // Start Time input
        panel.add(new JLabel("Start Time (HH:mm):"));
        timeField = new JTextField();
        panel.add(timeField);

        // End Time input
        panel.add(new JLabel("End Time (HH:mm):"));
        endTimeField = new JTextField();
        panel.add(endTimeField);

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
        try {
            List<String[]> spaces = db.retrieveData("parkingspaces");
            if (spaces == null || spaces.isEmpty()) {
                System.out.println("Debug: No parking spaces data found");
                JOptionPane.showMessageDialog(frame,
                        "Error: Could not retrieve parking spaces data.",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            System.out.println("Debug: Found " + spaces.size() + " parking spaces");
            System.out.println("Debug: Looking for space ID: " + spaceId);

            for (int i = 1; i < spaces.size(); i++) {
                String[] row = spaces.get(i);
                System.out.println("Debug: Checking row " + i + ": " + String.join(", ", row));

                if (row.length > 0 && row[0].equals(String.valueOf(spaceId))) {
                    System.out.println("Debug: Found matching space ID");
                    // Check if the space exists and is not occupied
                    if (row.length > 2) {
                        String status = row[2].trim().toLowerCase();
                        System.out.println("Debug: Space status: " + status);
                        return status.equals("false");  // false means vacant
                    } else {
                        System.out.println("Debug: Row length is " + row.length + ", expected > 2");
                    }
                }
            }
            System.out.println("Debug: Space ID " + spaceId + " not found in database");
            return false;
        } catch (Exception e) {
            System.out.println("Debug: Exception occurred: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame,
                    "Error checking space availability: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void handleCreateBooking() {
        try {
            int spaceId = Integer.parseInt(spaceIdField.getText());
            String date = dateField.getText().trim();
            String startTime = timeField.getText().trim();
            String endTime = endTimeField.getText().trim();

            // Validate date format using LocalDate
            try {
                LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(frame,
                        "Invalid date format. Please use yyyy-MM-dd (e.g., 2024-03-25)",
                        "Invalid Date Format",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate start time format using LocalTime
            LocalTime parsedStartTime;
            try {
                parsedStartTime = LocalTime.parse(startTime, DateTimeFormatter.ofPattern("HH:mm"));
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(frame,
                        "Invalid start time format. Please use HH:mm (e.g., 14:30)",
                        "Invalid Time Format",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate end time format using LocalTime
            LocalTime parsedEndTime;
            try {
                parsedEndTime = LocalTime.parse(endTime, DateTimeFormatter.ofPattern("HH:mm"));
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(frame,
                        "Invalid end time format. Please use HH:mm (e.g., 15:30)",
                        "Invalid Time Format",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check if end time is after start time
            if (!parsedEndTime.isAfter(parsedStartTime)) {
                JOptionPane.showMessageDialog(frame,
                        "End time must be after start time.",
                        "Invalid Time Range",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check if user is verified
            if (!currentUser.isVerified()) {
                JOptionPane.showMessageDialog(frame,
                        "Please verify your email before creating a booking.",
                        "Unverified User",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check if space exists and is available
            if (!isSpaceAvailable(spaceId)) {
                JOptionPane.showMessageDialog(frame,
                        "Space " + spaceId + " is either not available or does not exist.\n" +
                                "Please check the space ID and try again.",
                        "Space Unavailable",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Format times to ensure HH:mm format
            String formattedStartTime = String.format("%02d:%02d", parsedStartTime.getHour(), parsedStartTime.getMinute());
            String formattedEndTime = String.format("%02d:%02d", parsedEndTime.getHour(), parsedEndTime.getMinute());

            // Combine date and times into ISO format
            String startDateTime = date + "T" + formattedStartTime + ":00";
            String endDateTime = date + "T" + formattedEndTime + ":00";

            // Get current bookings to determine next booking ID
            List<String[]> bookings = db.retrieveData("bookings");
            int nextBookingId = 1;  // Start from 1 if no bookings exist
            for (int i = 1; i < bookings.size(); i++) {
                int currentId = Integer.parseInt(bookings.get(i)[0]);
                if (currentId >= nextBookingId) {
                    nextBookingId = currentId + 1;
                }
            }

            // Create new booking record
            String[] newBooking = {
                    String.valueOf(nextBookingId),
                    String.valueOf(currentUser.getUserID()),
                    String.valueOf(spaceId),
                    startDateTime,
                    endDateTime,
                    "Booked"
            };
            bookings.add(newBooking);

            // Update the database
            db.confirmUpdate("bookings", bookings);

            // Update space status to occupied
            parkingLotService.updateSpaceStatus(spaceId, "occupied");

            // Show confirmation dialog
            JOptionPane.showMessageDialog(frame,
                    String.format("Booking created successfully!\n" +
                                    "Booking ID: %d\n" +
                                    "Space ID: %d\n" +
                                    "Date: %s\n" +
                                    "Start Time: %s\n" +
                                    "End Time: %s\n\n" +
                                    "Please go to Payment menu to complete your payment.",
                            nextBookingId, spaceId, date, formattedStartTime, formattedEndTime),
                    "Booking Confirmation",
                    JOptionPane.INFORMATION_MESSAGE);

            frame.dispose();
            new BookingMenuGUI(currentUser);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame,
                    "Please enter valid numbers for Space ID",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame,
                    "Error creating booking: " + e.getMessage() + "\n" +
                            "Please check if the database files are in the correct location.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
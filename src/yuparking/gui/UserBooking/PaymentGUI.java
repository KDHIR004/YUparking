package yuparking.gui.UserBooking;

import yuparking.models.User;
import yuparking.services.PaymentService;
import yuparking.services.BookingService;
import yuparking.services.UserBookingService;
import yuparking.gui.Login.BookingMenuGUI;
import yuparking.database.Database;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class PaymentGUI {
    private JFrame frame;
    private JPanel panel;
    private JTextField bookingIdField;
    private JComboBox<String> paymentMethodComboBox;
    private PaymentService paymentService;
    private BookingService bookingService;
    private UserBookingService userBookingService;
    private Database db;
    private User currentUser;

    public PaymentGUI(User user) {
        this.currentUser = user;
        this.paymentService = new PaymentService();
        this.bookingService = new BookingService();
        this.userBookingService = new UserBookingService();
        this.db = new Database();
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Payment");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        panel = new JPanel(new GridLayout(6, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Booking ID:"));
        bookingIdField = new JTextField();
        panel.add(bookingIdField);

        panel.add(new JLabel("Payment Method:"));
        String[] methods = {"Credit", "Debit", "Online"};
        paymentMethodComboBox = new JComboBox<>(methods);
        panel.add(paymentMethodComboBox);

        JButton showDetailsButton = new JButton("Show Booking Details");
        showDetailsButton.addActionListener(e -> showBookingDetails());
        panel.add(showDetailsButton);

        JButton processButton = new JButton("Process Payment");
        processButton.addActionListener(e -> processPayment());
        panel.add(processButton);

        JButton backButton = new JButton("Return to Menu");
        backButton.addActionListener(e -> {
            frame.dispose();
            new BookingMenuGUI(currentUser);
        });
        panel.add(backButton);

        frame.add(panel);
        frame.setVisible(true);
    }

    private void showBookingDetails() {
        try {
            int bookingId = Integer.parseInt(bookingIdField.getText());
            List<String[]> bookings = db.retrieveData("bookings");
            boolean found = false;

            for (int i = 1; i < bookings.size(); i++) {
                String[] booking = bookings.get(i);
                if (Integer.parseInt(booking[0]) == bookingId &&
                        Integer.parseInt(booking[1]) == currentUser.getUserID()) {
                    found = true;

                    String startTime = booking[3]; 
                    String endTime = booking[4];    

                    long minutes = ChronoUnit.MINUTES.between(
                            LocalDateTime.parse(startTime),
                            LocalDateTime.parse(endTime)
                    );
                    double hours = minutes / 60.0;

                    double fee = bookingService.calculateFeeForBooking(currentUser, hours);

                    List<String[]> payments = db.retrieveData("payments");
                    String paymentStatus = "Not Paid";
                    for (int j = 1; j < payments.size(); j++) {
                        String[] payment = payments.get(j);
                        if (Integer.parseInt(payment[1]) == bookingId) {
                            paymentStatus = payment[4];
                            break;
                        }
                    }

                    JOptionPane.showMessageDialog(frame,
                            String.format("Booking Details:\n" +
                                            "Space ID: %s\n" +
                                            "Start Time: %s\n" +
                                            "End Time: %s\n" +
                                            "Duration: %.1f hours\n" +
                                            "Fee: $%.2f\n" +
                                            "Payment Status: %s",
                                    booking[2], formatDateTime(startTime), formatDateTime(endTime),
                                    hours, fee, paymentStatus),
                            "Booking Details",
                            JOptionPane.INFORMATION_MESSAGE);
                    break;
                }
            }

            if (!found) {
                JOptionPane.showMessageDialog(frame,
                        "Booking not found or does not belong to you.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame,
                    "Please enter a valid Booking ID",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame,
                    "Error showing booking details: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void processPayment() {
        try {
            int bookingId = Integer.parseInt(bookingIdField.getText());
            String paymentMethod = (String) paymentMethodComboBox.getSelectedItem();

            List<String[]> bookings = db.retrieveData("bookings");
            boolean found = false;

            for (int i = 1; i < bookings.size(); i++) {
                String[] booking = bookings.get(i);
                if (Integer.parseInt(booking[0]) == bookingId &&
                        Integer.parseInt(booking[1]) == currentUser.getUserID()) {
                    found = true;

                    String startTime = booking[3];
                    String endTime = booking[4];
                    long minutes = ChronoUnit.MINUTES.between(
                            LocalDateTime.parse(startTime),
                            LocalDateTime.parse(endTime)
                    );
                    double hours = minutes / 60.0;

                    double fee = bookingService.calculateFeeForBooking(currentUser, hours);

                    List<String[]> payments = db.retrieveData("payments");
                    boolean alreadyPaid = false;
                    for (int j = 1; j < payments.size(); j++) {
                        String[] payment = payments.get(j);
                        if (Integer.parseInt(payment[1]) == bookingId &&
                                payment[4].equalsIgnoreCase("Completed")) {
                            alreadyPaid = true;
                            break;
                        }
                    }

                    if (alreadyPaid) {
                        JOptionPane.showMessageDialog(frame,
                                "This booking has already been paid.",
                                "Payment Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    paymentService.processPayment(bookingId, fee, paymentMethod);

                    JOptionPane.showMessageDialog(frame,
                            String.format("Payment processed successfully!\n" +
                                            "Amount: $%.2f\n" +
                                            "Method: %s",
                                    fee, paymentMethod),
                            "Payment Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    break;
                }
            }

            if (!found) {
                JOptionPane.showMessageDialog(frame,
                        "Booking not found or does not belong to you.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame,
                    "Please enter a valid Booking ID",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame,
                    "Error processing payment: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private String formatDateTime(String dateTime) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTimeObj = LocalDateTime.parse(dateTime, inputFormatter);
        return dateTimeObj.format(outputFormatter);
    }
} 

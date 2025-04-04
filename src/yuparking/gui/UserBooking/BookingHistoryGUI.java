package yuparking.gui.UserBooking;

import yuparking.models.User;
import yuparking.services.UserBookingService;
import yuparking.services.BookingService;
import yuparking.gui.Login.BookingMenuGUI;
import yuparking.gui.UserBooking.PaymentGUI;
import yuparking.database.Database;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class BookingHistoryGUI {
    private JFrame frame;
    private JPanel panel;
    private UserBookingService userBookingService;
    private BookingService bookingService;
    private Database db;
    private User currentUser;

    public BookingHistoryGUI(User user) {
        this.currentUser = user;
        this.userBookingService = new UserBookingService();
        this.bookingService = new BookingService();
        this.db = new Database();
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Booking History");
        frame.setSize(1000, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columns = {"Booking ID", "Space ID", "Start Time", "End Time", "Duration (hours)", "Fee", "Status", "Payment Status", "Action"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 8; 
            }
        };
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        List<String[]> bookings = db.retrieveData("bookings");
        List<String[]> payments = db.retrieveData("payments");

        for (int i = 1; i < bookings.size(); i++) {
            String[] booking = bookings.get(i);
            if (Integer.parseInt(booking[1]) == currentUser.getUserID()) {
                try {
                    String startTime = booking[3]; 
                    String endTime = booking[4];    
                    int hours = calculateHours(startTime, endTime);
                    double fee = bookingService.calculateFeeForBooking(currentUser, hours);

                    String paymentStatus = "Not Paid";
                    for (int j = 1; j < payments.size(); j++) {
                        String[] payment = payments.get(j);
                        if (Integer.parseInt(payment[1]) == Integer.parseInt(booking[0])) {
                            paymentStatus = payment[4];
                            break;
                        }
                    }

                    Object[] rowData = {
                            booking[0],                    
                            booking[2],                    
                            formatDateTime(startTime),     
                            formatDateTime(endTime),       
                            hours,                         
                            String.format("$%.2f", fee),   
                            booking[5],                    
                            paymentStatus,                 
                            paymentStatus.equals("Not Paid") ? "Pay Now" : ""  
                    };

                    model.addRow(rowData);
                } catch (Exception e) {
                    System.out.println("Error processing booking " + booking[0] + ": " + e.getMessage());
                }
            }
        }

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    String paymentStatus = (String) model.getValueAt(row, 7);
                    if (paymentStatus.equals("Not Paid")) {
                        int bookingId = Integer.parseInt((String) model.getValueAt(row, 0));
                        frame.dispose();
                        new PaymentGUI(currentUser);
                    }
                }
            }
        });

        JButton backButton = new JButton("Return to Menu");
        backButton.addActionListener(e -> {
            frame.dispose();
            new BookingMenuGUI(currentUser);
        });

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(backButton, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setVisible(true);
    }

    private int calculateHours(String startTime, String endTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime start = LocalDateTime.parse(startTime, formatter);
        LocalDateTime end = LocalDateTime.parse(endTime, formatter);
        return (int) ChronoUnit.HOURS.between(start, end);
    }

    private String formatDateTime(String dateTime) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTimeObj = LocalDateTime.parse(dateTime, inputFormatter);
        return dateTimeObj.format(outputFormatter);
    }
} 

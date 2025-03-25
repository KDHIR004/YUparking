package yuparking.gui;

import yuparking.models.User;

import javax.swing.*;
import java.awt.*;

public class ManagerDashboardGUI {
    private JFrame frame;
    private User user;

    public ManagerDashboardGUI(User user) {
        this.user = user;
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Manager Dashboard - " + user.getUserType());
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1));

        JLabel welcomeLabel = new JLabel("Welcome, " + user.getEmail(), SwingConstants.CENTER);
        panel.add(welcomeLabel);

        JButton viewBookingsButton = new JButton("View All Bookings");
        JButton modifyBookingButton = new JButton("Modify Any Booking");
        JButton cancelBookingButton = new JButton("Cancel Any Booking");
        JButton viewParkingLotsButton = new JButton("View Parking Lots");
        JButton logoutButton = new JButton("Logout");

        logoutButton.addActionListener(e -> frame.dispose());

        panel.add(viewBookingsButton);
        panel.add(modifyBookingButton);
        panel.add(cancelBookingButton);
        panel.add(viewParkingLotsButton);
        panel.add(logoutButton);

        frame.add(panel);
        frame.setVisible(true);
    }
}

package yuparking.gui;

import yuparking.models.User;

import javax.swing.*;
import java.awt.*;

public class BookingMenuGUI {
    private JFrame frame;
    private User user;

    public BookingMenuGUI(User user) {
        this.user = user;
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Booking Menu - " + user.getEmail());
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1));

        JLabel welcomeLabel = new JLabel("Welcome, " + user.getEmail(), SwingConstants.CENTER);
        panel.add(welcomeLabel);

        JButton createBookingButton = new JButton("Create Booking");
        JButton modifyBookingButton = new JButton("Modify Booking");
        JButton cancelBookingButton = new JButton("Cancel Booking");
        JButton viewBookingsButton = new JButton("View All Bookings");
        JButton logoutButton = new JButton("Logout");

        logoutButton.addActionListener(e -> frame.dispose());

        panel.add(createBookingButton);
        panel.add(modifyBookingButton);
        panel.add(cancelBookingButton);
        panel.add(viewBookingsButton);
        panel.add(logoutButton);

        frame.add(panel);
        frame.setVisible(true);
    }
}

package yuparking.gui;

import yuparking.gui.Booking.CancelBookingGUI;
import yuparking.gui.Booking.CreateBookingGUI;
import yuparking.gui.Booking.ModifyBookingGUI;
import yuparking.models.User;
import yuparking.services.UserBookingService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BookingMenuGUI {
    private JFrame frame;
    private JPanel panel;
    private JButton createBookingButton, modifyBookingButton, cancelBookingButton, viewBookingsButton, logoutButton;
    private User loggedInUser;
    private UserBookingService userBookingService;

    public BookingMenuGUI(User user) {
        this.loggedInUser = user;
        this.userBookingService = new UserBookingService();
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Booking Menu");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1));

        createBookingButton = new JButton("Create Booking");
        modifyBookingButton = new JButton("Modify Booking");
        cancelBookingButton = new JButton("Cancel Booking");
        viewBookingsButton = new JButton("View Bookings");
        logoutButton = new JButton("Logout");

        panel.add(createBookingButton);
        panel.add(modifyBookingButton);
        panel.add(cancelBookingButton);
        panel.add(viewBookingsButton);
        panel.add(logoutButton);

        frame.add(panel);

        createBookingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CreateBookingGUI(loggedInUser, userBookingService);
                frame.dispose();
            }
        });

        modifyBookingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ModifyBookingGUI(loggedInUser, userBookingService);
                frame.dispose();
            }
        });

        cancelBookingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CancelBookingGUI(loggedInUser, userBookingService);
                frame.dispose();
            }
        });

        viewBookingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userBookingService.showUserBookings(loggedInUser);
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Logging out...");
                frame.dispose();
            }
        });

        frame.setVisible(true);
    }
}
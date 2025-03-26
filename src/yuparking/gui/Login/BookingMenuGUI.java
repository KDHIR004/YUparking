package yuparking.gui.Login;

import yuparking.models.User;
import yuparking.gui.UserBooking.*;

import javax.swing.*;
import java.awt.*;

public class BookingMenuGUI {
    private JFrame frame;
    private JPanel panel;
    private User currentUser;

    public BookingMenuGUI(User user) {
        this.currentUser = user;
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Booking Menu");
        frame.setSize(400, 500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        panel = new JPanel();
        panel.setLayout(new GridLayout(7, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton createButton = new JButton("Create Booking");
        JButton modifyButton = new JButton("Modify Booking");
        JButton cancelButton = new JButton("Cancel Booking");
        JButton historyButton = new JButton("View Booking History");
        JButton paymentButton = new JButton("Make Payment");
        JButton logoutButton = new JButton("Logout");

        createButton.addActionListener(e -> {
            frame.dispose();
            new CreateBookingGUI(currentUser);
        });

        modifyButton.addActionListener(e -> {
            frame.dispose();
            new ModifyBookingGUI(currentUser);
        });

        cancelButton.addActionListener(e -> {
            frame.dispose();
            new CancelBookingGUI(currentUser);
        });

        historyButton.addActionListener(e -> {
            frame.dispose();
            new BookingHistoryGUI(currentUser);
        });

        paymentButton.addActionListener(e -> {
            frame.dispose();
            new PaymentGUI(currentUser);
        });

        logoutButton.addActionListener(e -> {
            frame.dispose();
            new LoginGUI();
        });

        panel.add(createButton);
        panel.add(modifyButton);
        panel.add(cancelButton);
        panel.add(historyButton);
        panel.add(paymentButton);
        panel.add(logoutButton);

        frame.add(panel);
        frame.setVisible(true);
    }
}

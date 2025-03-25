package yuparking.gui.Booking;

import yuparking.models.User;
import yuparking.services.UserBookingService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CancelBookingGUI {
    private UserBookingService userBookingService;
    private JFrame frame;
    private JTextField bookingIDField;
    private JButton cancelButton, exitButton;

    public CancelBookingGUI(User loggedInUser, UserBookingService userBookingService) {
        this.userBookingService = userBookingService;
        frame = new JFrame("Cancel Booking");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(2, 2));

        panel.add(new JLabel("Enter Booking ID:"));
        bookingIDField = new JTextField();
        panel.add(bookingIDField);

        cancelButton = new JButton("Cancel Booking");
        exitButton = new JButton("Exit");

        panel.add(cancelButton);
        panel.add(exitButton);

        frame.add(panel);

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int bookingID = Integer.parseInt(bookingIDField.getText());
                userBookingService.cancelUserBooking(loggedInUser, bookingID);
                JOptionPane.showMessageDialog(frame, "Booking cancelled successfully!");
                frame.dispose();
            }
        });

        exitButton.addActionListener(e -> frame.dispose());

        frame.setVisible(true);
    }
}

package yuparking.gui.Booking;

import yuparking.models.User;
import yuparking.services.UserBookingService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ModifyBookingGUI {
    private UserBookingService userBookingService;
    private JFrame frame;
    private JTextField bookingIDField, newStartTimeField, newEndTimeField;
    private JButton modifyButton, cancelButton;

    public ModifyBookingGUI(User loggedInUser, UserBookingService userBookingService) {
        this.userBookingService = userBookingService;
        frame = new JFrame("Modify Booking");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2));

        panel.add(new JLabel("Enter Booking ID:"));
        bookingIDField = new JTextField();
        panel.add(bookingIDField);

        panel.add(new JLabel("New Start Time (yyyy-MM-ddTHH:mm):"));
        newStartTimeField = new JTextField();
        panel.add(newStartTimeField);

        panel.add(new JLabel("New End Time (yyyy-MM-ddTHH:mm):"));
        newEndTimeField = new JTextField();
        panel.add(newEndTimeField);

        modifyButton = new JButton("Modify Booking");
        cancelButton = new JButton("Cancel");

        panel.add(modifyButton);
        panel.add(cancelButton);

        frame.add(panel);

        modifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int bookingID = Integer.parseInt(bookingIDField.getText());
                String newStartTime = newStartTimeField.getText();
                String newEndTime = newEndTimeField.getText();
                userBookingService.modifyUserBooking(loggedInUser, bookingID, newStartTime, newEndTime);
                JOptionPane.showMessageDialog(frame, "Booking modified successfully!");
                frame.dispose();
            }
        });

        cancelButton.addActionListener(e -> frame.dispose());

        frame.setVisible(true);
    }
}

package yuparking.gui.Booking;

import yuparking.models.User;
import yuparking.services.UserBookingService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateBookingGUI {
    private UserBookingService userBookingService;
    private JFrame frame;
    private JTextField spaceIDField, startTimeField, endTimeField;
    private JButton createButton, cancelButton;

    public CreateBookingGUI(User loggedInUser, UserBookingService userBookingService) {
        this.userBookingService = userBookingService;
        frame = new JFrame("Create Booking");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2));

        panel.add(new JLabel("Enter Space ID:"));
        spaceIDField = new JTextField();
        panel.add(spaceIDField);

        panel.add(new JLabel("Enter Start Time (yyyy-MM-ddTHH:mm):"));
        startTimeField = new JTextField();
        panel.add(startTimeField);

        panel.add(new JLabel("Enter End Time (yyyy-MM-ddTHH:mm):"));
        endTimeField = new JTextField();
        panel.add(endTimeField);

        createButton = new JButton("Create Booking");
        cancelButton = new JButton("Cancel");

        panel.add(createButton);
        panel.add(cancelButton);

        frame.add(panel);

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int spaceID = Integer.parseInt(spaceIDField.getText());
                String startTime = startTimeField.getText();
                String endTime = endTimeField.getText();
                userBookingService.createUserBooking(loggedInUser, spaceID, startTime, endTime);
                JOptionPane.showMessageDialog(frame, "Booking created successfully!");
                frame.dispose();
            }
        });

        cancelButton.addActionListener(e -> frame.dispose());

        frame.setVisible(true);
    }
}

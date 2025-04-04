package yuparking.gui.Login;

import yuparking.models.User;
import yuparking.gui.Manager.AllBookingGUI;
import yuparking.gui.Manager.M_ModifyBookingGUI;
import yuparking.gui.Manager.M_CancelBookingGUI;
import yuparking.gui.Manager.ParkingSpaceGUI;
import yuparking.gui.Manager.EnableDisableLotGUI;
import yuparking.gui.Manager.AddSpaceGUI;
import yuparking.gui.Manager.RemoveSpaceGUI;
import yuparking.gui.Manager.OccupancyUpdateGUI;
import yuparking.gui.Manager.ManualSpaceStatusGUI;

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
        frame.setSize(500, 800);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(12, 1));

        JLabel welcomeLabel = new JLabel("Welcome, " + user.getEmail(), SwingConstants.CENTER);
        panel.add(welcomeLabel);

        JButton viewBookingsButton = new JButton("1. View All Bookings");
        JButton modifyBookingButton = new JButton("2. Modify Any Booking");
        JButton cancelBookingButton = new JButton("3. Cancel Any Booking");
        JButton viewParkingLotsButton = new JButton("4. View Parking Lots");
        JButton enableLotButton = new JButton("5. Enable Parking Lot");
        JButton disableLotButton = new JButton("6. Disable Parking Lot");
        JButton addSpaceButton = new JButton("7. Add Parking Space");
        JButton removeSpaceButton = new JButton("8. Remove Parking Space");
        JButton occupancyUpdateButton = new JButton("9. Simulate Occupancy Update");
        JButton manualStatusButton = new JButton("10. Manually Update Space Status");
        JButton logoutButton = new JButton("11. Log out of manager dashboard");

        viewBookingsButton.addActionListener(e -> new AllBookingGUI(user));
        modifyBookingButton.addActionListener(e -> new M_ModifyBookingGUI(user));
        cancelBookingButton.addActionListener(e -> new M_CancelBookingGUI(user));
        viewParkingLotsButton.addActionListener(e -> new ParkingSpaceGUI(user));
        enableLotButton.addActionListener(e -> new EnableDisableLotGUI(user, true));
        disableLotButton.addActionListener(e -> new EnableDisableLotGUI(user, false));
        addSpaceButton.addActionListener(e -> new AddSpaceGUI(user));
        removeSpaceButton.addActionListener(e -> new RemoveSpaceGUI(user));
        occupancyUpdateButton.addActionListener(e -> new OccupancyUpdateGUI(user));
        manualStatusButton.addActionListener(e -> new ManualSpaceStatusGUI(user));
        logoutButton.addActionListener(e -> {
            frame.dispose();
            new LoginGUI();
        });

        panel.add(viewBookingsButton);
        panel.add(modifyBookingButton);
        panel.add(cancelBookingButton);
        panel.add(viewParkingLotsButton);
        panel.add(enableLotButton);
        panel.add(disableLotButton);
        panel.add(addSpaceButton);
        panel.add(removeSpaceButton);
        panel.add(occupancyUpdateButton);
        panel.add(manualStatusButton);
        panel.add(logoutButton);

        frame.add(panel);
        frame.setVisible(true);
    }
}

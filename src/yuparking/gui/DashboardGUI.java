package yuparking.gui;

import yuparking.models.User;

import javax.swing.*;
import java.awt.*;

public class DashboardGUI {
    private JFrame frame;
    private User user;

    public DashboardGUI(User user) {
        this.user = user;
        initializeUI();
    }

    private void initializeUI() {
        if (user.getUserType().equals("manager") || user.getUserType().equals("super_manager")) {
            new ManagerDashboardGUI(user);
        } else {
            new BookingMenuGUI(user);
        }
    }
}

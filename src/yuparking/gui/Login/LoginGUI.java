package yuparking.gui.Login;

import yuparking.factory.UserFactory;
import yuparking.gui.UserBooking.CancelBookingGUI;
import yuparking.models.User;
import yuparking.services.LoginService;
import yuparking.services.SignupService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginGUI {
    private LoginService loginService;
    private SignupService signupService;
    private JFrame frame;
    private JPanel panel;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton, signupButton, exitButton;

    public LoginGUI() {
        loginService = new LoginService();
        signupService = new SignupService();
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("YuParking System");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1));

        panel.add(new JLabel("Email:"));
        emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        loginButton = new JButton("Login");
        signupButton = new JButton("Sign Up");
        exitButton = new JButton("Exit");

        panel.add(loginButton);
        panel.add(signupButton);
        panel.add(exitButton);

        frame.add(panel);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSignup();
            }
        });

        exitButton.addActionListener(e -> System.exit(0));

        frame.setVisible(true);
    }

    private void handleLogin() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        User loggedInUser = loginService.login(email, password);
        if (loggedInUser != null) {
            JOptionPane.showMessageDialog(frame, "Welcome " + loggedInUser.getEmail());
            if (!loggedInUser.isVerified()) {
                JOptionPane.showMessageDialog(frame, "Your email is not verified. Simulating verification now...");
                loggedInUser.clickVerificationLink();
                loginService.updateVerificationInCSV(loggedInUser.getUserID());
            }
            frame.dispose(); // Close login window
            if (loggedInUser.getUserType().equals("manager") || loggedInUser.getUserType().equals("super_manager")) {
                new ManagerDashboardGUI(loggedInUser);
            } else {
                new BookingMenuGUI(loggedInUser);
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid email or password.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleSignup() {
        String email = JOptionPane.showInputDialog(frame, "Enter email:");
        String password = JOptionPane.showInputDialog(frame, "Enter password:");
        String[] userTypes = {"faculty", "staff", "student", "visitor"};
        String userType = (String) JOptionPane.showInputDialog(frame, "Select user type:", "User Type",
                JOptionPane.QUESTION_MESSAGE, null, userTypes, userTypes[0]);

        if (userType != null) {
            boolean success = signupService.signup(email, password, userType);
            if (success) {
                JOptionPane.showMessageDialog(frame, "Signup successful! Simulating verification...");
                int newUserId = signupService.getNextUserId() - 1;
                User newUser = UserFactory.createUser(newUserId, email, password, userType);
                newUser.clickVerificationLink();
            } else {
                JOptionPane.showMessageDialog(frame, "Signup failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        new LoginGUI();
    }
}
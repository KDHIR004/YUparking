package yuparking.gui.Login;

import yuparking.factory.UserFactory;
import yuparking.gui.UserBooking.CancelBookingGUI;
import yuparking.models.User;
import yuparking.services.LoginService;
import yuparking.services.SignupService;
import yuparking.database.Database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class LoginGUI {
    private LoginService loginService;
    private SignupService signupService;
    private JFrame frame;
    private JPanel panel;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton, signupButton, exitButton;
    private Database db;

    public LoginGUI() {
        loginService = new LoginService();
        signupService = new SignupService();
        db = new Database();
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
        String email = emailField.getText().trim();
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
            if (loggedInUser.getUserType().equals("super_manager")) {
                new SuperManagerDashboardGUI(loggedInUser);
            } else if (loggedInUser.getUserType().equals("manager")) {
                new ManagerDashboardGUI(loggedInUser);
            } else {
                new BookingMenuGUI(loggedInUser);
            }
        } else {
            JOptionPane.showMessageDialog(frame,
                    "Invalid email or password.\nPlease check your credentials and try again.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasSpecialChar = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowercase = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecialChar = true;
            }
        }

        return hasUppercase && hasLowercase && hasSpecialChar;
    }

    private void handleSignup() {
        new RegistrationGUI(signupService, db);
    }

    public static void main(String[] args) {
        new LoginGUI();
    }
}

class RegistrationGUI {
    private JFrame frame;
    private JPanel panel;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JComboBox<String> userTypeCombo;
    private SignupService signupService;
    private Database db;

    public RegistrationGUI(SignupService signupService, Database db) {
        this.signupService = signupService;
        this.db = db;
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Registration");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Email field
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        emailField = new JTextField(20);
        panel.add(emailField, gbc);

        // Password field
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        passwordField = new JPasswordField(20);
        panel.add(passwordField, gbc);

        // Confirm Password field
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Confirm Password:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        confirmPasswordField = new JPasswordField(20);
        panel.add(confirmPasswordField, gbc);

        // User Type combo box
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("User Type:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        String[] userTypes = {"faculty", "staff", "student", "visitor"};
        userTypeCombo = new JComboBox<>(userTypes);
        panel.add(userTypeCombo, gbc);

        // Password requirements label
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        JLabel requirementsLabel = new JLabel("<html>Password must contain:<br>" +
                "• At least 8 characters<br>" +
                "• At least one uppercase letter<br>" +
                "• At least one lowercase letter<br>" +
                "• At least one special character</html>");
        requirementsLabel.setForeground(Color.GRAY);
        panel.add(requirementsLabel, gbc);

        // Register button
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> handleRegistration());
        panel.add(registerButton, gbc);

        frame.add(panel);
        frame.setVisible(true);
    }

    private void handleRegistration() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String userType = (String) userTypeCombo.getSelectedItem();

        // Validate email format
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            JOptionPane.showMessageDialog(frame,
                    "Invalid email format.\nPlease enter a valid email address.",
                    "Invalid Email",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if email exists
        List<String[]> users = db.retrieveData("users");
        for (int i = 1; i < users.size(); i++) {
            if (users.get(i)[2].equalsIgnoreCase(email)) {
                JOptionPane.showMessageDialog(frame,
                        "This email is already registered.\nPlease use a different email.",
                        "Email Exists",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Validate password
        if (!isValidPassword(password)) {
            JOptionPane.showMessageDialog(frame,
                    "Password must contain:\n" +
                            "- At least 8 characters\n" +
                            "- At least one uppercase letter\n" +
                            "- At least one lowercase letter\n" +
                            "- At least one special character",
                    "Invalid Password",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(frame,
                    "Passwords do not match.",
                    "Password Mismatch",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Attempt registration
        boolean success = signupService.signup(email, password, userType);
        if (success) {
            JOptionPane.showMessageDialog(frame,
                    "Registration successful!\nPlease check your email for verification.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            // Simulate verification
            int newUserId = signupService.getNextUserId() - 1;
            User newUser = UserFactory.createUser(newUserId, email, password, userType);
            newUser.clickVerificationLink();

            frame.dispose();
        } else {
            JOptionPane.showMessageDialog(frame,
                    "Registration failed.\nPlease try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasSpecialChar = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowercase = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecialChar = true;
            }
        }

        return hasUppercase && hasLowercase && hasSpecialChar;
    }
}
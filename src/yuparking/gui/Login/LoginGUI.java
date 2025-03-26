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

        // Validate input
        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame,
                    "Please enter both email and password.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create a new LoginService instance to refresh the data
        loginService = new LoginService();
        User loggedInUser = loginService.login(email, password);

        if (loggedInUser != null) {
            // Show login success message first
            JOptionPane.showMessageDialog(frame, "Welcome " + loggedInUser.getEmail());

            // Then handle verification if needed
            if (!loggedInUser.isVerified()) {
                loggedInUser.clickVerificationLink();
                loginService.updateVerificationInCSV(loggedInUser.getUserID());
                // Refresh LoginService again after verification
                loginService = new LoginService();
                JOptionPane.showMessageDialog(frame, "Verification updated in the system for " + email);
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

    private void handleSignup() {
        RegistrationGUI registrationGUI = new RegistrationGUI(signupService, db, loginService);
        // After registration window is closed, refresh LoginService
        registrationGUI.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                loginService = new LoginService();
            }
        });
    }

    public static void main(String[] args) {
        new LoginGUI();
    }
}

class RegistrationGUI extends JFrame {
    private JPanel panel;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JComboBox<String> userTypeCombo;
    private SignupService signupService;
    private LoginService loginService;
    private Database db;

    public RegistrationGUI(SignupService signupService, Database db, LoginService loginService) {
        this.signupService = signupService;
        this.db = db;
        this.loginService = loginService;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Registration");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

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

        add(panel);
        setVisible(true);
    }

    private void handleRegistration() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String userType = (String) userTypeCombo.getSelectedItem();

        // Validate email format
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            JOptionPane.showMessageDialog(this,
                    "Invalid email format.\nPlease enter a valid email address.",
                    "Invalid Email Format",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate email domain based on user type
        if (!isValidYorkEmail(email, userType)) {
            String domainError = "";
            if (userType.equalsIgnoreCase("student")) {
                domainError = "Student emails must end with @my.yorku.ca";
            } else if (userType.equalsIgnoreCase("faculty") || userType.equalsIgnoreCase("staff")) {
                domainError = "Faculty/Staff emails must end with @yorku.ca";
            } else if (userType.equalsIgnoreCase("visitor")) {
                domainError = "Visitor emails cannot be from York University domains";
            }
            JOptionPane.showMessageDialog(this,
                    "Invalid email domain for user type.\n" + domainError,
                    "Invalid Email Domain",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if email exists
        List<String[]> users = db.retrieveData("users");
        for (int i = 1; i < users.size(); i++) {
            if (users.get(i)[1].equalsIgnoreCase(email)) {
                JOptionPane.showMessageDialog(this,
                        "This email is already registered.\nPlease use a different email or try logging in.",
                        "Email Already Exists",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Validate password length
        if (password.length() < 8) {
            JOptionPane.showMessageDialog(this,
                    "Password must be at least 8 characters long.",
                    "Invalid Password Length",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate password requirements
        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasSpecialChar = false;
        boolean hasNumber = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowercase = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecialChar = true;
            } else if (Character.isDigit(c)) {
                hasNumber = true;
            }
        }

        if (!hasUppercase || !hasLowercase || !hasSpecialChar || !hasNumber) {
            StringBuilder errorMessage = new StringBuilder("Password must contain:\n");
            if (!hasUppercase) errorMessage.append("- At least one uppercase letter\n");
            if (!hasLowercase) errorMessage.append("- At least one lowercase letter\n");
            if (!hasSpecialChar) errorMessage.append("- At least one special character\n");
            if (!hasNumber) errorMessage.append("- At least one number\n");
            
            JOptionPane.showMessageDialog(this,
                    errorMessage.toString(),
                    "Invalid Password Requirements",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this,
                    "Passwords do not match.\nPlease make sure both passwords are identical.",
                    "Password Mismatch",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Attempt registration
        boolean success = signupService.signup(email, password, userType);
        if (success) {
            // Get the newly created user
            int newUserId = signupService.getNextUserId() - 1;
            User newUser = UserFactory.createUser(newUserId, email, password, userType);

            // Auto-verify all users
            newUser.clickVerificationLink();
            loginService.updateVerificationInCSV(newUserId);

            // Refresh LoginService to get updated data
            loginService = new LoginService();

            JOptionPane.showMessageDialog(this,
                    "Registration successful!\nA verification email has been sent to your inbox.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Registration failed.\nPlease try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isValidYorkEmail(String email, String userType) {
        if (userType.equalsIgnoreCase("student")) {
            return email.endsWith("@my.yorku.ca");
        } else if (userType.equalsIgnoreCase("faculty") || userType.equalsIgnoreCase("staff")) {
            return email.endsWith("@yorku.ca");
        } else if (userType.equalsIgnoreCase("visitor")) {
            return !email.endsWith("@yorku.ca") && !email.endsWith("@my.yorku.ca");
        }
        return false;
    }
}

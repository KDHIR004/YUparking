package yuparking.services;

import yuparking.database.Database;

import java.util.Arrays;
import java.util.List;

public class SignupService {
    private Database db;
    private int nextUserId;
    private final List<String> allowedUserTypes = Arrays.asList("faculty", "staff", "student", "visitor");


    public SignupService() {
        db = new Database();
        nextUserId = calculateNextUserId();
    }

    private int calculateNextUserId() {
        List<String[]> users = db.retrieveData("users");
        return users.size(); // simple incremental user ID
    }

    public int getNextUserId() {
        return nextUserId;
    }

    public boolean signup(String email, String password, String userType) {
        if (!allowedUserTypes.contains(userType.toLowerCase())) {
            System.out.println("Invalid user type: " + userType + ". Allowed types are: faculty, staff, student, visitor.");
            return false;
        }
        if (!isValidYorkEmail(email, userType)) {
            System.out.println("Email does not match the required format for user type " + userType + ".");
            return false;
        }

        if (!isPasswordStrong(password)) {
            System.out.println("Password must be at least 8 characters long, with uppercase, lowercase, number, and special character.");
            return false;
        }
        List<String[]> users = db.retrieveData("users");
        for (String[] row : users) {
            if (row[1].equalsIgnoreCase(email)) {
                System.out.println("Email already exists. Login to Account");
                return false;
            }
        }
        String verificationStatus = "false";
        if (userType.equalsIgnoreCase("visitor")) {
            verificationStatus = "true";
        }
                String[] newUser = new String[]{
            String.valueOf(nextUserId),
            email,
            password,
            userType.toLowerCase(),
            verificationStatus
        };

        
        users.add(newUser);
        db.confirmUpdate("users", users);
        System.out.println("Account created for " + email + " as " + userType + ". Verification pending.");

        //After signup
        nextUserId++;
        return true;
        
    }
    private boolean isPasswordStrong(String password) {
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$");
    }

    private boolean isValidYorkEmail(String email, String userType) {
        if (userType.equalsIgnoreCase("student")) {
            return email.endsWith("@my.yorku.ca");
        } else if (userType.equalsIgnoreCase("faculty") || userType.equalsIgnoreCase("staff") || userType.equalsIgnoreCase("manager")) {
            return email.endsWith("@yorku.ca");
        } else if (userType.equalsIgnoreCase("visitor")) {
            return !email.endsWith("@yorku.ca") && !email.endsWith("@my.yorku.ca");
        }
        return false;
    }
}

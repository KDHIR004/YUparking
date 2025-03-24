package yuparking.services;

import yuparking.database.Database;
import java.util.List;

public class SignupService {
    private Database db;
    private int nextUserId;

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

    public void signup(String email, String password, String userType) {
        List<String[]> users = db.retrieveData("users");
        String[] newUser = new String[]{
                String.valueOf(nextUserId),
                email,
                password,
                userType.toLowerCase(),
                "false"  // verification
        };
        users.add(newUser);
        db.confirmUpdate("users", users);
        System.out.println("Account created for " + email + " as " + userType + ". Verification pending.");

        //After signup
        nextUserId++;
    }
}

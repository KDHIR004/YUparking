package yuparking;

import yuparking.models.*;
import yuparking.factory.UserFactory;

public class App {
    public static void main(String[] args) {
        // Simulate registering different types of users
        User facultyUser = UserFactory.createUser(1, "faculty@yorku.ca", "facpass", "faculty");
        User staffUser = UserFactory.createUser(2, "staff@yorku.ca", "staffpass", "staff");
        User studentUser = UserFactory.createUser(3, "student@my.yorku.ca", "studentpass", "student");
        User visitorUser = UserFactory.createUser(4, "visitor@yorku.ca", "visitorpass", "visitor");

        // Simulate system actions — showing user details
        System.out.println("Registered Users:");
        printUserDetails(facultyUser);
        printUserDetails(staffUser);
        printUserDetails(studentUser);
        printUserDetails(visitorUser);
        
        // Simulate login attempt (by email)
        loginUser("student@my.yorku.ca", "studentpass", new User[]{facultyUser, staffUser, studentUser, visitorUser});
    }

    private static void printUserDetails(User user) {
        System.out.println("----------------------------");
        System.out.println("User ID: " + user.getUserID());
        System.out.println("Email: " + user.getEmail());
        System.out.println("User Type: " + user.getUserType());
    }

    private static void loginUser(String email, String password, User[] users) {
        System.out.println("\nAttempting login for: " + email);
        for (User user : users) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                System.out.println("Login successful for user type: " + user.getUserType());
                return;
            }
        }
        System.out.println("Login failed: User not found or password incorrect.");
    }
}

package yuparking;

import yuparking.models.*;
import yuparking.factory.UserFactory;
import yuparking.services.LoginService;
import yuparking.services.SignupService;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        LoginService loginService = new LoginService();
        SignupService signupService = new SignupService();
        Scanner sc = new Scanner(System.in);

        User loggedInUser = null;

        while (true) {
            System.out.println("\n--- Welcome to YuParking System ---");
            System.out.println("1. Login");
            System.out.println("2. Sign Up");
            System.out.println("3. Exit");
            System.out.print("Choose option: ");
            int choice = sc.nextInt();
            sc.nextLine();  // consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter email: ");
                    String loginEmail = sc.nextLine();
                    System.out.print("Enter password: ");
                    String loginPassword = sc.nextLine();
                    loggedInUser = loginService.login(loginEmail, loginPassword);
                    if (loggedInUser != null) {
                        System.out.println("Welcome " + loggedInUser.getEmail() + " (" + loggedInUser.getUserType() + ")");
                        // Simulate verification click if user not verified (in object form)
                        if (!loggedInUser.isVerified()) {
                            System.out.println("Your email is not verified. Simulating verification now...");
                            loggedInUser.clickVerificationLink();
                        }
                    }
                    break;

                case 2:
                    System.out.print("Enter email: ");
                    String email = sc.nextLine();
                    System.out.print("Enter password: ");
                    String password = sc.nextLine();
                    System.out.print("Enter user type (student, faculty, staff, visitor): ");
                    String userType = sc.nextLine();

                    signupService.signup(email, password, userType);
                    // Simulate verification immediately after signup:
                    System.out.println("Simulating clicking verification link for this new user...");
                    int newUserId = signupService.getNextUserId() - 1;  // Id created
                    User newUser = UserFactory.createUser(newUserId, email, password, userType);
                    newUser.clickVerificationLink();

                case 3:
                    System.out.println("Exiting YuParking System. Goodbye!");
                    return;

                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
}

package yuparking;

import yuparking.models.*;
import yuparking.factory.UserFactory;
import yuparking.services.LoginService;
import yuparking.services.SignupService;
import yuparking.services.UserBookingService;
import yuparking.services.BookingService;

import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        LoginService loginService = new LoginService();
        SignupService signupService = new SignupService();
        Scanner sc = new Scanner(System.in);

        // Load users for booking service later
        List<User> users = loginService.getAllUsers();
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
                            loginService.updateVerificationInCSV(loggedInUser.getUserID());

                        }


                        // After successful login, open booking menu
                        showUserBookingMenu(loggedInUser);
                    }
                    break;

                case 2:
                    System.out.print("Enter email: ");
                    String email = sc.nextLine();
                    System.out.print("Enter password: ");
                    String password = sc.nextLine();
                    String userType = "vistor";
                    boolean success = false;
                    while (!success) {
                        System.out.print("Enter user type (faculty, staff, student, visitor): ");
                        userType = sc.nextLine();
                        success = signupService.signup(email, password, userType);
                        if (!success) {
                            System.out.println("Please enter a valid user type and try again.");
                        }
                    }

                    signupService.signup(email, password, userType);
                    // Simulate verification immediately after signup:
                    System.out.println("Simulating clicking verification link for this new user...");
                    int newUserId = signupService.getNextUserId() - 1;  // Id created
                    User newUser = UserFactory.createUser(newUserId, email, password, userType);
                    newUser.clickVerificationLink();
                    users.add(newUser); // Add newly created user to the list
                    break;

                case 3:
                    System.out.println("Exiting YuParking System. Goodbye!");
                    return;

                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    // Booking menu shown after login
    private static void showUserBookingMenu(User loggedInUser) {
        UserBookingService userBookingService = new UserBookingService();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Booking Menu ---");
            System.out.println("1. Create Booking");
            System.out.println("2. Modify Booking");
            System.out.println("3. Cancel Booking");
            System.out.println("4. View All Bookings");
            System.out.println("5. Log out of booking menu");
            System.out.print("Choose option: ");
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                System.out.print("Enter SpaceID: ");
                int spaceID = sc.nextInt();
                sc.nextLine();
                System.out.print("Enter Start Time (yyyy-MM-ddTHH:mm): ");
                String startTime = sc.nextLine();
                System.out.print("Enter End Time (yyyy-MM-ddTHH:mm): ");
                String endTime = sc.nextLine();
                userBookingService.createUserBooking(loggedInUser, spaceID, startTime, endTime);
                break;

                case 2:
                System.out.print("Enter Booking ID to modify: ");
                int bookingIdToModify = sc.nextInt();
                sc.nextLine();
                System.out.print("New Start Time (yyyy-MM-ddTHH:mm): ");
                String newStartTime = sc.nextLine();
                System.out.print("New End Time (yyyy-MM-ddTHH:mm): ");
                String newEndTime = sc.nextLine();
                userBookingService.modifyUserBooking(loggedInUser, bookingIdToModify, newStartTime, newEndTime);
                break;

                case 3:
                System.out.print("Enter Booking ID to cancel: ");
                int bookingIdToCancel = sc.nextInt();
                sc.nextLine();
                userBookingService.cancelUserBooking(loggedInUser, bookingIdToCancel);
                break;

                case 4:
                userBookingService.showUserBookings(loggedInUser);
                break;

                case 5:
                    System.out.println("Logging out of booking menu...");
                    return;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}

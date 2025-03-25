package yuparking;

import yuparking.models.*;
import yuparking.factory.UserFactory;
import yuparking.services.LoginService;
import yuparking.services.ManagementService;
import yuparking.services.ParkingLotService;
import yuparking.services.ParkingSensorService;
import yuparking.services.SignupService;
import yuparking.services.UserBookingService;

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
            try {
                int choice = sc.nextInt();
                sc.nextLine(); // consume newline
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

                            // Manager or super_manager login
                            if (loggedInUser.getUserType().equals("manager") || loggedInUser.getUserType().equals("super_manager")) {
                                showManagerDashboard(loggedInUser);
                            } else {
                                showUserBookingMenu(loggedInUser);
                            }
                        }
                        break;

                    case 2:
                    String userType;
                    boolean typeValid = false;
                
                    // Ask for user type first
                    while (!typeValid) {
                        System.out.print("Enter user type (faculty, staff, student, visitor): ");
                        userType = sc.nextLine().toLowerCase();
                
                        if (userType.equals("faculty") || userType.equals("staff") || userType.equals("student") || userType.equals("visitor")) {
                            typeValid = true;
                
                            // Now ask for email and password after type is confirmed
                            System.out.print("Enter email: ");
                            String email = sc.nextLine();
                
                            System.out.print("Enter password (8+ chars, upper/lowercase, number & special char): ");
                            String password = sc.nextLine();
                
                            boolean success = signupService.signup(email, password, userType);
                            if (success) {
                                System.out.println("Account created! Simulating verification...");
                                int newUserId = signupService.getNextUserId() - 1;
                                User newUser = UserFactory.createUser(newUserId, email, password, userType);
                                newUser.clickVerificationLink();
                                loginService.updateVerificationInCSV(newUserId);
                            } else {
                                System.out.println("Account creation failed. Please follow email & password rules and try again.");
                            }
                        } else {
                            System.out.println("Invalid user type. Please enter faculty, staff, student, or visitor.");
                        }
                    }
                    break;
                
                    case 3:
                        System.out.println("Exiting YuParking System. Goodbye!");
                        return;

                    default:
                        System.out.println("Invalid option. Try again.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number (1, 2, or 3).");
                sc.nextLine(); // consume invalid input 
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


    private static void showManagerDashboard(User managerUser) {
    ManagementService managementService = new ManagementService();
    ParkingLotService parkingLotService = new ParkingLotService();
    ParkingSensorService sensorService = new ParkingSensorService();
    Scanner sc = new Scanner(System.in);

    while (true) {
        System.out.println("\n--- Manager Dashboard (" + managerUser.getUserType() + ") ---");
        System.out.println("1. View All Bookings");
        System.out.println("2. Modify Any Booking");
        System.out.println("3. Cancel Any Booking");
        System.out.println("4. View Parking Lots");
        System.out.println("5. Enable Parking Lot");
        System.out.println("6. Disable Parking Lot");
        System.out.println("7. Add Parking Space");
        System.out.println("8. Remove Parking Space");
        System.out.println("9. Simulate Occupancy Update");
        System.out.println("10. Manually Update Space Status");
        System.out.println("11. Log out of manager dashboard");

        if (managerUser.getUserType().equals("super_manager")) {
            System.out.println("12. Add New Parking Lot (Super Manager Only)");
            System.out.println("13. Remove Parking Lot (Super Manager Only)");
        }

        System.out.print("Choose option: ");
        int choice = sc.nextInt();
        sc.nextLine(); // consume newline

        switch (choice) {
            case 1:
                managementService.viewDetailedHistory();
                break;
            case 2:
                System.out.print("Enter Booking ID to modify: ");
                int bookingIdModify = sc.nextInt();
                sc.nextLine();
                System.out.print("New Start Time (yyyy-MM-ddTHH:mm): ");
                String newStart = sc.nextLine();
                System.out.print("New End Time (yyyy-MM-ddTHH:mm): ");
                String newEnd = sc.nextLine();
                managementService.modifyAnyBooking(bookingIdModify, newStart, newEnd);
                break;
            case 3:
                System.out.print("Enter Booking ID to cancel: ");
                int bookingIdCancel = sc.nextInt();
                sc.nextLine();
                managementService.cancelAnyBooking(bookingIdCancel);
                break;
            case 4:
                parkingLotService.viewParkingLots();
                break;
            case 5:
                System.out.print("Enter Lot ID to enable: ");
                int lotIdEnable = sc.nextInt();
                sc.nextLine();
                parkingLotService.enableLot(lotIdEnable);
                break;
            case 6:
                System.out.print("Enter Lot ID to disable: ");
                int lotIdDisable = sc.nextInt();
                sc.nextLine();
                parkingLotService.disableLot(lotIdDisable);
                break;
            case 7:
                System.out.print("Enter Lot ID to add space to: ");
                int lotIdAddSpace = sc.nextInt();
                sc.nextLine();
                System.out.print("How many spaces to add? ");
                int numberOfSpaces = sc.nextInt();
                sc.nextLine();
                parkingLotService.addSpace(lotIdAddSpace, numberOfSpaces);
                break;
            case 8:
                System.out.print("Enter Space ID to remove: ");
                int spaceIdRemove = sc.nextInt();
                sc.nextLine();
                parkingLotService.removeSpace(spaceIdRemove);
                break;
            case 9:
                sensorService.simulateOccupancyUpdate();
                break;
            case 10:
                System.out.print("Enter Space ID to update: ");
                int spaceIdUpdate = sc.nextInt();
                sc.nextLine();
                System.out.print("Enter new status (occupied, vacant, maintenance): ");
                String newStatus = sc.nextLine();
                parkingLotService.updateSpaceStatus(spaceIdUpdate, newStatus);
                break;
            case 11:
                System.out.println("Logging out of manager dashboard...");
                return;
            case 12:
                if (managerUser.getUserType().equals("super_manager")) {
                    System.out.print("Enter new Lot ID: ");
                    int newLotID = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter Lot Location: ");
                    String location = sc.nextLine();
                    System.out.print("Enter capacity: ");
                    int capacity = sc.nextInt();
                    sc.nextLine();
                    parkingLotService.addNewParkingLot(newLotID, location, capacity);
                } else {
                    System.out.println("Only Super Managers can add new parking lots.");
                }
                break;
            case 13:
                if (managerUser.getUserType().equals("super_manager")) {
                    System.out.print("Enter Lot ID to remove: ");
                    int lotIdRemove = sc.nextInt();
                    sc.nextLine();
                    parkingLotService.removeParkingLot(lotIdRemove);
                } else {
                    System.out.println("Only Super Managers can remove parking lots.");
                }
                break;
            default:
                System.out.println("Invalid choice. Try again.");
        }
    }
}


}

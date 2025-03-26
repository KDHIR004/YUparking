package yuparking.services;

import yuparking.database.Database;

import java.time.LocalDateTime;
import java.util.List;

public class ManagementService {
    private final Database db;

    public ManagementService() {
        this.db = new Database();
    }

    public void viewDetailedHistory() {
        List<String[]> bookings = db.retrieveData("bookings");
        System.out.println("\nAll Bookings (Manager View):");
        for (int i = 1; i < bookings.size(); i++) { // skip header
            String[] row = bookings.get(i);
            System.out.println("BookingID: " + row[0] +
                    " | UserID: " + row[1] +
                    " | SpaceID: " + row[2] +
                    " | Start: " + row[3] +
                    " | End: " + row[4] +
                    " | Status: " + row[5]);
        }
    }

    public void modifyAnyBooking(int bookingID, String newStartTime, String newEndTime) {
        List<String[]> bookings = db.retrieveData("bookings");

        LocalDateTime newStart = LocalDateTime.parse(newStartTime);
        LocalDateTime newEnd = LocalDateTime.parse(newEndTime);

        if (newEnd.isBefore(newStart) || newEnd.equals(newStart)) {
            System.out.println("Invalid time range: End time must be after start time.");
            return;
        }
    
        for (int i = 1; i < bookings.size(); i++) {
            String[] row = bookings.get(i);
            if (row[0].equals(String.valueOf(bookingID))) {
                row[3] = newStartTime;
                row[4] = newEndTime;
                row[5] = "Modified by Manager";
                db.confirmUpdate("bookings", bookings);
                System.out.println("Booking " + bookingID + " modified by manager.");
                return;
            }
        }
        System.out.println("Booking ID not found.");
    }

    public void cancelAnyBooking(int bookingID) {
        List<String[]> bookings = db.retrieveData("bookings");
        for (int i = 1; i < bookings.size(); i++) {
            String[] row = bookings.get(i);
            if (row[0].equals(String.valueOf(bookingID)) && !row[5].equalsIgnoreCase("Cancelled")) {
                row[5] = "Cancelled by Manager";
                db.confirmUpdate("bookings", bookings);
                System.out.println("Booking " + bookingID + " cancelled by manager.");
                return;
            }
        }
        System.out.println("Booking not found or already cancelled.");
    }
    public void generateManagerAccount() {
        List<String[]> users = db.retrieveData("users");
        int nextId = users.size();
        String generatedUsername = "manager_auto_" + nextId;
        String generatedPassword = generateStrongPassword();
    
        String[] newManager = new String[]{
                String.valueOf(nextId),
                generatedUsername + "@yorku.ca",
                generatedPassword,
                "manager",
                "true"  // auto-verified
        };
    
        users.add(newManager);
        db.confirmUpdate("users", users);
    
        System.out.println("✅ New manager account generated:");
        System.out.println("Username: " + generatedUsername + "@yorku.ca");
        System.out.println("Password: " + generatedPassword);
    }
    
    private String generateStrongPassword() {
        // Example simple random password generator (8 chars)
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%";
        StringBuilder sb = new StringBuilder();
        java.util.Random rand = new java.util.Random();
        for (int i = 0; i < 10; i++) {
            sb.append(chars.charAt(rand.nextInt(chars.length())));
        }
        return sb.toString();
    }    
}

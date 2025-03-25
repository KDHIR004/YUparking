package yuparking.services;

import yuparking.database.Database;

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
}
